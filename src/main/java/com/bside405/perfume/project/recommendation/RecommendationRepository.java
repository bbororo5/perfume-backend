package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.Perfume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Perfume, Long> {

    @Query("SELECT p.id FROM Perfume p JOIN p.perfumeHashtags ph JOIN ph.hashtag h WHERE h.id IN :hashtagIds GROUP BY p.id ORDER BY COUNT(h.id) DESC, p.id ASC")
    List<Long> findPerfumeIdsByHashtagIdsOrderedByMatchCount(List<Long> hashtagIds, Pageable pageable);
}