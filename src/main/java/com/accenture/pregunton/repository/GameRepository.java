package com.accenture.pregunton.repository;

import com.accenture.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  Optional<Game> findByCode(String code);

  @Query(value = "SELECT g.hit FROM Games g WHERE g.code = :code", nativeQuery = true)
  Optional<String> getHitByCode(String code);
}
