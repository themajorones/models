package dev.themajorones.models.client;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import dev.themajorones.models.dto.CreateAndroidVMRequest;
import dev.themajorones.models.dto.DockerCapability;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class DockerClient {

    private static final String ADB_PORT = "5555/tcp";

    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DockerClient(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    public DockerCapability capabilities(String baseUrl) {
        String versionBody = get(baseUrl, "/version");
        String infoBody = get(baseUrl, "/info");
        try {
            JsonNode version = objectMapper.readTree(versionBody == null ? "{}" : versionBody);
            JsonNode info = objectMapper.readTree(infoBody == null ? "{}" : infoBody);
            DockerCapability capability = new DockerCapability()
                .setVersion(text(version, "Version"))
                .setApiVersion(text(version, "ApiVersion"))
                .setOs(firstText(version, info, "Os", "OSType", "OperatingSystem"))
                .setArch(firstText(version, info, "Arch", "Architecture"));

            capability.setNvidiaRuntimeAvailable(info.path("Runtimes").has("nvidia"));
            List<String> gpuDevices = new ArrayList<>();
            for (JsonNode device : info.path("DiscoveredDevices")) {
                String id = text(device, "ID");
                if (id != null && id.contains("nvidia.com/gpu")) {
                    gpuDevices.add(id);
                }
            }
            capability.setGpuDevices(gpuDevices);
            return capability;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse Docker engine capability", ex);
        }
    }

    public boolean isHealthy(String baseUrl) {
        capabilities(baseUrl);
        return true;
    }

    public boolean imageExists(String baseUrl, String image) {
        try {
            request(baseUrl).get()
                .uri("/images/{image}/json", image)
                .retrieve()
                .toBodilessEntity();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void pullImage(String baseUrl, String image) {
        String encoded = URLEncoder.encode(requireText(image, "Docker image"), StandardCharsets.UTF_8);
        request(baseUrl).post()
            .uri("/images/create?fromImage=" + encoded)
            .retrieve()
            .body(String.class);
    }

    public String createAndroidContainer(String baseUrl, Integer androidVMId, CreateAndroidVMRequest vmRequest) {
        String containerName = "tmos-android-vm-" + androidVMId;
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("Image", requireText(vmRequest.getImage(), "Android VM image"));
        body.put("Labels", Map.of(
            "tmos.managed", "true",
            "tmos.androidVMId", String.valueOf(androidVMId)
        ));
        body.put("ExposedPorts", Map.of(ADB_PORT, Map.of()));
        body.put("Env", redroidEnvironment(vmRequest));
        body.put("HostConfig", Map.of(
            "Privileged", true,
            "PortBindings", Map.of(ADB_PORT, List.of(Map.of("HostIp", "0.0.0.0", "HostPort", "")))
        ));

        String response = request(baseUrl).post()
            .uri("/containers/create?name={name}", containerName)
            .body(body)
            .retrieve()
            .body(String.class);

        try {
            return requireText(objectMapper.readTree(response == null ? "{}" : response).path("Id").asString(), "Docker container id");
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse Docker container create response", ex);
        }
    }

    public void startContainer(String baseUrl, String containerId) {
        request(baseUrl).post()
            .uri("/containers/{id}/start", requireText(containerId, "Docker container id"))
            .retrieve()
            .toBodilessEntity();
    }

    public void stopContainer(String baseUrl, String containerId) {
        request(baseUrl).post()
            .uri("/containers/{id}/stop?t=10", requireText(containerId, "Docker container id"))
            .retrieve()
            .toBodilessEntity();
    }

    public void removeContainer(String baseUrl, String containerId) {
        request(baseUrl).delete()
            .uri("/containers/{id}?force=true", requireText(containerId, "Docker container id"))
            .retrieve()
            .toBodilessEntity();
    }

    public String inspectContainerJson(String baseUrl, String containerId) {
        return get(baseUrl, "/containers/" + url(containerId) + "/json");
    }

    public boolean isContainerRunning(String baseUrl, String containerId) {
        try {
            JsonNode inspect = objectMapper.readTree(inspectContainerJson(baseUrl, containerId));
            return inspect.path("State").path("Running").asBoolean(false);
        } catch (Exception ex) {
            return false;
        }
    }

    public Integer mappedAdbPort(String baseUrl, String containerId) {
        try {
            JsonNode inspect = objectMapper.readTree(inspectContainerJson(baseUrl, containerId));
            for (JsonNode binding : inspect.path("NetworkSettings").path("Ports").path(ADB_PORT)) {
                int port = binding.path("HostPort").asInt(0);
                if (port > 0) {
                    return port;
                }
            }
            return null;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to read mapped ADB port", ex);
        }
    }

    public boolean isTcpPortReachable(String host, Integer port, Duration timeout) {
        if (host == null || host.isBlank() || port == null || port <= 0) {
            return false;
        }
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), Math.toIntExact(timeout.toMillis()));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String hostFromBaseUrl(String baseUrl) {
        return URI.create(normalizeBaseUrl(baseUrl)).getHost();
    }

    public String normalizeBaseUrl(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Docker base URL is required");
        }
        URI uri = URI.create(value.trim());
        if (uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalArgumentException("Docker base URL must include scheme and host");
        }
        String normalized = uri.toString();
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }

    private String get(String baseUrl, String path) {
        return request(baseUrl).get().uri(path).retrieve().body(String.class);
    }

    private RestClient request(String baseUrl) {
        return restClientBuilder
            .baseUrl(normalizeBaseUrl(baseUrl))
            .defaultStatusHandler(HttpStatusCode::isError, (request, response) -> {
                throw new IllegalStateException("Docker API request failed with status " + response.getStatusCode());
            })
            .build();
    }

    private List<String> redroidEnvironment(CreateAndroidVMRequest vmRequest) {
        List<String> env = new ArrayList<>();
        if (vmRequest.getWidth() != null) {
            env.add("ro.redroid.width=" + vmRequest.getWidth());
        }
        if (vmRequest.getHeight() != null) {
            env.add("ro.redroid.height=" + vmRequest.getHeight());
        }
        if (vmRequest.getDpi() != null) {
            env.add("ro.redroid.dpi=" + vmRequest.getDpi());
        }
        String mode = vmRequest.getAccelerationMode() == null ? CreateAndroidVMRequest.DEFAULT_ACCELERATION_MODE : vmRequest.getAccelerationMode();
        env.add("androidboot.redroid_gpu_mode=" + switch (mode.toUpperCase()) {
            case "HOST" -> "host";
            case "AUTO" -> "auto";
            default -> "guest";
        });
        return env;
    }

    private String firstText(JsonNode first, JsonNode second, String... names) {
        for (String name : names) {
            String value = text(first, name);
            if (value != null) {
                return value;
            }
            value = text(second, name);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    private String text(JsonNode node, String property) {
        String value = node.path(property).asString("");
        return value == null || value.isBlank() ? null : value;
    }

    private String requireText(String value, String description) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(description + " is required");
        }
        return value.trim();
    }

    private String url(String value) {
        return URLEncoder.encode(requireText(value, "URL value"), StandardCharsets.UTF_8);
    }
}
