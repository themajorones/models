package dev.themajorones.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "authentication")
public class GitHubAuthentication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    public GitHubAuthentication(String content, Long expireAt) {
        this.content = content;
        this.expireAt = expireAt;
    }

    @Column(nullable = false, length = 2048)
    private String content;
    
    @Column(nullable = false)
    private Long expireAt;
}
