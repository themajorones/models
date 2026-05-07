package dev.themajorones.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(
    name = "github_artifact",
    indexes = @Index(
        name = "idx_github_artifact_run_expires_at",
        columnList = "workflow_run_id, expires_at"
    )
)
public class GitHubArtifact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Long githubArtifactId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repo_id", nullable = false)
    private GitHubRepo repo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workflow_run_id", nullable = false)
    private GitHubWorkflowRun workflowRun;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private Long sizeInBytes;

    @Column(name = "expires_at", nullable = false)
    private Long expiresAt;
}
