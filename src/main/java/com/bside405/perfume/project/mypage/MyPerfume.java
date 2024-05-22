package com.bside405.perfume.project.mypage;


import com.bside405.perfume.project.perfume.Perfume;
import com.bside405.perfume.project.oauth2.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "my_perfumes")
public class MyPerfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "added_date", updatable = false)
    private LocalDateTime addedDate = LocalDateTime.now();
}
