package com.bside405.perfume.project.perfume;

import com.bside405.perfume.project.clova.PerfumeNameDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    @Query("SELECT new com.bside405.perfume.project.clova.PerfumeNameDTO(p.name, p.englishName)" +
            "FROM Perfume p " +
            "WHERE p.id = :perfumeId")
    PerfumeNameDTO findNameAndENameById(@Param("perfumeId") Long perfumeId);
}
