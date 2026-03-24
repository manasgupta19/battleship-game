package com.kotak.battleship.repository;

import com.kotak.battleship.domain.GameState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameState, Long> {
    // Basic CRUD operations are inherited
}