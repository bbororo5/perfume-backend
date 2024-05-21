package com.bside405.perfume.project.perfume;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "hashtags")
@EntityListeners(AuditingEntityListener.class)
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "hashtag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PerfumeHashtag> perfumeHashtags = new HashSet<>();
}
