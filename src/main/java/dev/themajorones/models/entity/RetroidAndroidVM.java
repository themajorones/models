package dev.themajorones.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RetroidAndroidVM implements AndroidVM {

    public static final String VM_TYPE = "RETROID";

    private Integer id;
    private Docker docker;
    private String name;
    private String image;
    private String containerId;
    private String containerName;
    private String adbHost;
    private Integer adbPort;
    private String status;
    private RetroidAndroidVMDetails details = new RetroidAndroidVMDetails();

    @Override
    public String getVmType() {
        return VM_TYPE;
    }

    public String getAccelerationMode() {
        return details == null ? null : details.getAccelerationMode();
    }

    public RetroidAndroidVM setAccelerationMode(String accelerationMode) {
        details().setAccelerationMode(accelerationMode);
        return this;
    }

    public Integer getWidth() {
        return details == null ? null : details.getWidth();
    }

    public RetroidAndroidVM setWidth(Integer width) {
        details().setWidth(width);
        return this;
    }

    public Integer getHeight() {
        return details == null ? null : details.getHeight();
    }

    public RetroidAndroidVM setHeight(Integer height) {
        details().setHeight(height);
        return this;
    }

    public Integer getDpi() {
        return details == null ? null : details.getDpi();
    }

    public RetroidAndroidVM setDpi(Integer dpi) {
        details().setDpi(dpi);
        return this;
    }

    public RetroidAndroidVMDetails getDetails() {
        return details;
    }

    public RetroidAndroidVM setDetails(RetroidAndroidVMDetails details) {
        this.details = details == null ? new RetroidAndroidVMDetails() : details;
        return this;
    }

    private RetroidAndroidVMDetails details() {
        if (details == null) {
            details = new RetroidAndroidVMDetails();
        }
        return details;
    }
}
