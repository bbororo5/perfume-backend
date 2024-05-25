package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.PerfumeHashtag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<PerfumeHashtag, Long> {
    @Query("SELECT ph.perfume.id FROM PerfumeHashtag ph WHERE ph.hashtag.id IN :hashtagIds GROUP BY ph.perfume.id ORDER BY COUNT(ph.perfume.id) DESC")
    List<Long> findTopPerfumeIdsByHashtagIds(@Param("hashtagIds") List<Long> hashtagIds, Pageable pageable);
}
