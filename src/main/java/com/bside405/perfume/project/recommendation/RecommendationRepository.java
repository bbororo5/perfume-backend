package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.Hashtag;
import com.bside405.perfume.project.perfume.Perfume;
import com.bside405.perfume.project.perfume.PerfumeHashtag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<PerfumeHashtag, Long> {
    @Query("SELECT ph.perfume FROM PerfumeHashtag ph WHERE ph.hashtag IN :hashtags GROUP BY ph.perfume ORDER BY COUNT(ph.perfume) DESC")
    List<Perfume> findTopPerfumeByHashtags(@Param("hashtags") List<Hashtag> hashtags, Pageable pageable);
}
