package dev.themajorones.models.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

import dev.themajorones.models.entity.AndroidVMRecord;
import dev.themajorones.models.entity.Docker;
import dev.themajorones.models.entity.RetroidAndroidVM;

class AndroidVmMapperTest {

    @Test
    void roundTripsRetroidVmThroughRecordJson() {
        Docker docker = new Docker()
            .setId(7)
            .setName("dock")
            .setBaseUrl("http://docker.example:2375");

        RetroidAndroidVM vm = new RetroidAndroidVM()
            .setId(11)
            .setDocker(docker)
            .setName("retroid-1")
            .setImage("redroid/redroid:15.0.0_64only-latest")
            .setContainerId("abc123")
            .setContainerName("tmos-android-vm-11")
            .setAdbHost("192.168.0.10")
            .setAdbPort(5555)
            .setStatus("READY")
            .setAccelerationMode("HOST")
            .setWidth(1280)
            .setHeight(720)
            .setDpi(320);

        AndroidVMRecord record = AndroidVmMapper.toRecord(vm);

        assertEquals("RETROID", record.getVmType());
        assertNotNull(record.getDetailsJson());

        RetroidAndroidVM restored = assertInstanceOf(RetroidAndroidVM.class, AndroidVmMapper.fromRecord(record));
        assertEquals(vm.getId(), restored.getId());
        assertEquals(vm.getDocker().getId(), restored.getDocker().getId());
        assertEquals(vm.getName(), restored.getName());
        assertEquals(vm.getImage(), restored.getImage());
        assertEquals(vm.getContainerId(), restored.getContainerId());
        assertEquals(vm.getContainerName(), restored.getContainerName());
        assertEquals(vm.getAdbHost(), restored.getAdbHost());
        assertEquals(vm.getAdbPort(), restored.getAdbPort());
        assertEquals(vm.getStatus(), restored.getStatus());
        assertEquals(vm.getAccelerationMode(), restored.getAccelerationMode());
        assertEquals(vm.getWidth(), restored.getWidth());
        assertEquals(vm.getHeight(), restored.getHeight());
        assertEquals(vm.getDpi(), restored.getDpi());
    }

    @Test
    void flattensRetroidVmForApiResponses() {
        Docker docker = new Docker()
            .setId(7)
            .setName("dock")
            .setBaseUrl("http://docker.example:2375");

        RetroidAndroidVM vm = new RetroidAndroidVM()
            .setDocker(docker)
            .setName("retroid-1")
            .setImage("redroid/redroid:15.0.0_64only-latest")
            .setStatus("RUNNING")
            .setAccelerationMode("AUTO")
            .setWidth(1920)
            .setHeight(1080)
            .setDpi(480);

        Map<String, Object> flat = AndroidVmMapper.toFlatMap(vm);

        assertEquals("RETROID", flat.get("vmType"));
        assertEquals(7, flat.get("dockerId"));
        assertEquals("dock", flat.get("dockerName"));
        assertEquals("AUTO", flat.get("accelerationMode"));
        assertEquals(1920, flat.get("width"));
        assertEquals(1080, flat.get("height"));
        assertEquals(480, flat.get("dpi"));
    }
}
