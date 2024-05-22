package com.bside405.perfume.project.recommendation;

import com.bside405.perfume.project.perfume.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Hashtag findByName(String name);
}
