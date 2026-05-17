package dev.themajorones.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "docker")
public class Docker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 1024)
    private String baseUrl;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false, length = 64)
    private String status;

    @Column(length = 64)
    private String apiVersion;

    @Column(length = 128)
    private String os;

    @Column(length = 128)
    private String arch;

    @Column(nullable = false)
    private boolean nvidiaRuntimeAvailable;

    @Lob
    @Column(columnDefinition = "nvarchar(max)")
    private String gpuDevicesJson;
}
