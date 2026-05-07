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
    name = "github_workflow_run",
    indexes = @Index(
        name = "idx_github_workflow_run_repo_workflow_sha",
        columnList = "repo_id, workflow_id, head_sha"
    )
)
public class GitHubWorkflowRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private Long githubRunId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "repo_id", nullable = false)
    private GitHubRepo repo;

    @Column(nullable = false)
    private Long workflowId;

    @Column(name = "head_sha", nullable = false, length = 64)
    private String headSha;

    @Column(nullable = false, length = 64)
    private String status;

    @Column(nullable = false)
    private Long syncedAt;
}
