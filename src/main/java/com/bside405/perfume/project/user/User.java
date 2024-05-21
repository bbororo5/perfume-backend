package com.bside405.perfume.project.user;

import com.bside405.perfume.project.perfume.Perfume;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String providerId;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Perfume> userPerfumes = new ArrayList<>();

    public void addPerfume(Perfume perfume) {
        userPerfumes.add(perfume);
        perfume.setUser(this);
    }

    public void removePerfume(Perfume perfume) {
        userPerfumes.remove(perfume);
    }

    public List<Perfume> getUserPerfumes() {
        return userPerfumes;
    }
}
