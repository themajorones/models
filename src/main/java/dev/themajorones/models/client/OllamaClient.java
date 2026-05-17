package dev.themajorones.models.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.client.RestClient;

import dev.themajorones.models.dto.OllamaModelSummary;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public class OllamaClient {

    private final RestClient.Builder restClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OllamaClient(RestClient.Builder restClientBuilder) {
        this.restClientBuilder = restClientBuilder;
    }

    public List<OllamaModelSummary> listModels(String baseUrl) {
        String body = restClientBuilder.build()
            .get()
            .uri(normalizeBaseUrl(baseUrl) + "/api/tags")
            .retrieve()
            .body(String.class);

        try {
            JsonNode root = objectMapper.readTree(body == null ? "{}" : body);
            List<OllamaModelSummary> models = new ArrayList<>();
            for (JsonNode model : root.path("models")) {
                JsonNode details = model.path("details");
                models.add(new OllamaModelSummary()
                    .setName(text(model, "name"))
                    .setModel(text(model, "model"))
                    .setSize(model.path("size").longValue(0L))
                    .setDigest(text(model, "digest"))
                    .setFamily(text(details, "family"))
                    .setParameterSize(text(details, "parameter_size"))
                    .setQuantizationLevel(text(details, "quantization_level")));
            }
            return models;
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse Ollama model list", ex);
        }
    }

    public boolean isHealthy(String baseUrl) {
        return !listModels(baseUrl).isEmpty();
    }

    public String normalizeBaseUrl(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Ollama base URL is required");
        }
        URI uri = URI.create(value.trim());
        if (uri.getScheme() == null || uri.getHost() == null) {
            throw new IllegalArgumentException("Ollama base URL must include scheme and host");
        }
        String normalized = uri.toString();
        return normalized.endsWith("/") ? normalized.substring(0, normalized.length() - 1) : normalized;
    }

    private String text(JsonNode node, String property) {
        String value = node.path(property).asString("");
        return value == null || value.isBlank() ? null : value;
    }
}
