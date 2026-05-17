package dev.themajorones.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateAndroidVMRequest {

    public static final String DEFAULT_IMAGE = "redroid/redroid:15.0.0_64only-latest";
    public static final String DEFAULT_ACCELERATION_MODE = "GUEST";

    private Integer dockerId;
    private String name;
    private String image = DEFAULT_IMAGE;
    private String accelerationMode = DEFAULT_ACCELERATION_MODE;
    private Integer width;
    private Integer height;
    private Integer dpi;
}
