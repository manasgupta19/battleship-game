package com.kotak.battleship.repository;

import com.kotak.battleship.domain.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipRepository extends JpaRepository<Ship, String> {
}