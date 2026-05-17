package dev.themajorones.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TaskCommandEnvelope {

    private Integer taskLogId;
    private String type;
    private String content;
}
