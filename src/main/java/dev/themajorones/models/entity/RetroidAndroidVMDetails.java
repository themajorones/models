package dev.themajorones.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RetroidAndroidVMDetails {

    private Integer width;
    private Integer height;
    private Integer dpi;
    private String accelerationMode;
}
