package com.bside405.perfume.project.perfume;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "perfumes")
@EntityListeners(AuditingEntityListener.class)
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perfume_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(unique = true, nullable = false, name = "e_name")
    private String englishName;

    @Column(unique = true, nullable = false, name = "image_url")
    private String imageUrl;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "last_modified_date")
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL)
    private Set<PerfumeHashtag> perfumeHashtags = new HashSet<>();
}
