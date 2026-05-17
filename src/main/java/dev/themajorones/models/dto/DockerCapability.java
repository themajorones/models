package dev.themajorones.models.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DockerCapability {

    private String version;
    private String apiVersion;
    private String os;
    private String arch;
    private boolean nvidiaRuntimeAvailable;
    private List<String> gpuDevices = new ArrayList<>();
}
