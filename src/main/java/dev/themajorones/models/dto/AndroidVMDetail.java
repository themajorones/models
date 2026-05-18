package dev.themajorones.models.dto;

import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class AndroidVMDetail {

    private Map<String, Object> androidVM;
    private String dockerInspectJson;
}
