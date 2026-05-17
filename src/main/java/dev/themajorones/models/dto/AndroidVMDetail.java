package dev.themajorones.models.dto;

import dev.themajorones.models.entity.AndroidVM;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AndroidVMDetail {

    private AndroidVM androidVM;
    private String dockerInspectJson;
}
