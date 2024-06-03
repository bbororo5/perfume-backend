package com.bside405.perfume.project.mypage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyPerfumeRepository extends JpaRepository<MyPerfume, Long> {
    Page<MyPerfume> findAllByUserId(Long userId, Pageable pageable);
    Optional<MyPerfume> findByUserIdAndPerfumeId(Long userId, Long perfumeId);
    @Query("select p.perfume.id from MyPerfume p where p.user.id = :userId AND p.perfume.id in :perfumeIds ")
    List<Long> findExistingPerfumeIds(@Param("userId") Long userId, @Param("perfumeIds") List<Long> perfumeIds);
}
