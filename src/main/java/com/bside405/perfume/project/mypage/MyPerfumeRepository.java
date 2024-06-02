package com.bside405.perfume.project.mypage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MyPerfumeRepository extends JpaRepository<MyPerfume, Long> {
    Page<MyPerfume> findAllByUserId(Long userId, Pageable pageable);
    Optional<MyPerfume> findByUserIdAndPerfumeId(Long userId, Long perfumeId);
}
