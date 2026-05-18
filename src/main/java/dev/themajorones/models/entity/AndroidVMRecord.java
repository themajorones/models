package dev.themajorones.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "android_vm")
public class AndroidVMRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "docker_id", nullable = false)
    private Docker docker;

    @Column(length = 32)
    private String vmType;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 512)
    private String image;

    @Column(length = 128, nullable = true)
    private String containerId;

    @Column(length = 255, nullable = true)
    private String containerName;

    @Column(length = 255, nullable = true)
    private String adbHost;

    @Column(nullable = true)
    private Integer adbPort;

    @Column(nullable = false, length = 64)
    private String status;

    @Lob
    @Column(name = "details_json", columnDefinition = "nvarchar(max)")
    private String detailsJson;
}
