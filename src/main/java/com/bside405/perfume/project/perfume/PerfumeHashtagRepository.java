package com.bside405.perfume.project.perfume;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumeHashtagRepository extends JpaRepository<PerfumeHashtag, Long > {
    @Query("SELECT h.name FROM Hashtag h JOIN PerfumeHashtag ph ON h.id = ph.hashtag.id WHERE ph.perfume.id = :perfumeId")
    List<String> findHashtagNamesByPerfumeId(@Param("perfumeId") Long perfumeId);
}
