package dev.themajorones.models.entity;

public interface AndroidVM {

    Integer getId();

    AndroidVM setId(Integer id);

    Docker getDocker();

    AndroidVM setDocker(Docker docker);

    String getName();

    AndroidVM setName(String name);

    String getImage();

    AndroidVM setImage(String image);

    String getContainerId();

    AndroidVM setContainerId(String containerId);

    String getContainerName();

    AndroidVM setContainerName(String containerName);

    String getAdbHost();

    AndroidVM setAdbHost(String adbHost);

    Integer getAdbPort();

    AndroidVM setAdbPort(Integer adbPort);

    String getStatus();

    AndroidVM setStatus(String status);

    String getVmType();
}
