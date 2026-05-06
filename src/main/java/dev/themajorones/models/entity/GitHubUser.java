package dev.themajorones.models.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "github_user")
public class GitHubUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 255)
    private String githubId;
    
    @Column(nullable = false, unique = true, length = 255)
    private String username;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar", nullable = false)
    private Image avatar;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_token", nullable = false)
    private GitHubAuthentication accessToken;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token", nullable = true)
    private GitHubAuthentication refreshToken;
}