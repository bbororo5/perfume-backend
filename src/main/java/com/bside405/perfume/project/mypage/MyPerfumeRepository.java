package com.bside405.perfume.project.mypage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPerfumeRepository extends JpaRepository<MyPerfume, Long> {
    Page<MyPerfume> findAllByUserId(Long userId, Pageable pageable);
    boolean existsByUserIdAndPerfumeId(Long userId, Long perfumeId);
}
