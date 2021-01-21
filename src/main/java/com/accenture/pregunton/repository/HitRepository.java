package com.accenture.pregunton.repository;

import com.accenture.pregunton.model.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HitRepository extends JpaRepository<Hit, Long> {
}
