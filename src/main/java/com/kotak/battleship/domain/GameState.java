package com.kotak.battleship.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GameState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int gridSize;
    private boolean isGameOver;
    private String winner;
    private String currentTurnPlayer; // "PlayerA" or "PlayerB"
}