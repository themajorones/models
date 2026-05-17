package dev.themajorones.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OllamaModelSummary {

    private String name;
    private String model;
    private Long size;
    private String digest;
    private String family;
    private String parameterSize;
    private String quantizationLevel;
}
