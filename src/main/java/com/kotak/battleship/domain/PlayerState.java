package com.kotak.battleship.domain;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerState {
    private String name;
    private int minX;
    private int maxX;
    
    // Domain collections for the current game session
    private List<Ship> fleet = new ArrayList<>();
    private Set<Coordinate> firedShots = new HashSet<>();
    private Map<Coordinate, Ship> gridMap = new HashMap<>();

    public PlayerState(String name, int minX, int maxX) {
        this.name = name;
        this.minX = minX;
        this.maxX = maxX;
    }

    /**
     * Staff-level helper: Check if a coordinate belongs to this player's territory
     */
    public boolean isCoordinateInTerritory(Coordinate c, int gridSize) {
        return c.x() >= minX && c.x() <= maxX && c.y() >= 0 && c.y() < gridSize;
    }
}