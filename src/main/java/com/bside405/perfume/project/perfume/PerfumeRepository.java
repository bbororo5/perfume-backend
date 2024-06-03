package com.bside405.perfume.project.perfume;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    @Query("SELECT p.name, p.eName FROM Perfume p WHERE p.id = :perfumeId")
    Object[] findNameAndENameById(@Param("perfumeId") Long perfumeId);
}
