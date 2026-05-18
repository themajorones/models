package dev.themajorones.models.mapper;

import java.util.LinkedHashMap;
import java.util.Map;

import dev.themajorones.models.dto.CreateAndroidVMRequest;
import dev.themajorones.models.entity.AndroidVM;
import dev.themajorones.models.entity.AndroidVMRecord;
import dev.themajorones.models.entity.RetroidAndroidVM;
import dev.themajorones.models.entity.RetroidAndroidVMDetails;
import tools.jackson.databind.ObjectMapper;

public final class AndroidVmMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private AndroidVmMapper() {
    }

    public static AndroidVMRecord toRecord(AndroidVM vm) {
        if (vm == null) {
            return null;
        }
        AndroidVMRecord record = new AndroidVMRecord()
            .setId(vm.getId())
            .setDocker(vm.getDocker())
            .setVmType(vm.getVmType())
            .setName(vm.getName())
            .setImage(vm.getImage())
            .setContainerId(vm.getContainerId())
            .setContainerName(vm.getContainerName())
            .setAdbHost(vm.getAdbHost())
            .setAdbPort(vm.getAdbPort())
            .setStatus(vm.getStatus())
            .setDetailsJson("{}");
        if (vm instanceof RetroidAndroidVM retroid) {
            record.setDetailsJson(writeJson(retroid.getDetails()));
        }
        return record;
    }

    public static AndroidVM fromRecord(AndroidVMRecord record) {
        if (record == null) {
            return null;
        }
        String vmType = normalize(record.getVmType());
        if (vmType == null || RetroidAndroidVM.VM_TYPE.equals(vmType)) {
            RetroidAndroidVM vm = new RetroidAndroidVM()
                .setId(record.getId())
                .setDocker(record.getDocker())
                .setName(record.getName())
                .setImage(record.getImage())
                .setContainerId(record.getContainerId())
                .setContainerName(record.getContainerName())
                .setAdbHost(record.getAdbHost())
                .setAdbPort(record.getAdbPort())
                .setStatus(record.getStatus());
            vm.setDetails(readDetails(record.getDetailsJson()));
            return vm;
        }
        throw new IllegalArgumentException("Unsupported Android VM type: " + record.getVmType());
    }

    public static Map<String, Object> toFlatMap(AndroidVM vm) {
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("id", vm.getId());
        values.put("vmType", vm.getVmType());
        values.put("dockerId", vm.getDocker().getId());
        values.put("dockerName", vm.getDocker().getName());
        values.put("name", vm.getName());
        values.put("image", vm.getImage());
        values.put("containerId", vm.getContainerId());
        values.put("containerName", vm.getContainerName());
        values.put("adbHost", vm.getAdbHost());
        values.put("adbPort", vm.getAdbPort());
        values.put("status", vm.getStatus());
        if (vm instanceof RetroidAndroidVM retroid) {
            values.put("accelerationMode", retroid.getAccelerationMode());
            values.put("width", retroid.getWidth());
            values.put("height", retroid.getHeight());
            values.put("dpi", retroid.getDpi());
        }
        return values;
    }

    public static Map<String, Object> toFlatMap(AndroidVMRecord record) {
        return toFlatMap(fromRecord(record));
    }

    public static RetroidAndroidVM fromRequest(CreateAndroidVMRequest request, dev.themajorones.models.entity.Docker docker, String status) {
        return new RetroidAndroidVM()
            .setDocker(docker)
            .setName(request.getName())
            .setImage(request.getImage())
            .setAccelerationMode(request.getAccelerationMode())
            .setWidth(request.getWidth())
            .setHeight(request.getHeight())
            .setDpi(request.getDpi())
            .setStatus(status);
    }

    private static RetroidAndroidVMDetails readDetails(String json) {
        if (json == null || json.isBlank()) {
            return new RetroidAndroidVMDetails();
        }
        try {
            return OBJECT_MAPPER.readValue(json, RetroidAndroidVMDetails.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to parse Android VM details JSON", ex);
        }
    }

    private static String writeJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to serialize Android VM details JSON", ex);
        }
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim().toUpperCase();
    }
}
