package com.kotak.battleship.domain;

import java.util.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SHIP")
@Getter
@NoArgsConstructor
public class Ship {

    @Id
    private String id; // Will store "A-SH1", "B-SH1", etc.

    private String originalId; // Stores "SH1"
    private String owner;      // "PlayerA" or "PlayerB"
    private int size;

    // Bounding Box Coordinates
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    public Ship(String playerShortName, String originalId, String owner, int size, int centerX, int centerY) {
        this.id = playerShortName + "-" + originalId; 
        this.originalId = originalId;
        this.owner = owner;
        this.size = size;
        
        // Calculate Bounding Box based on Size S and Center (X, Y)
        int offset = size / 2;
        this.minX = centerX - offset;
        this.maxX = centerX + offset;
        this.minY = centerY - offset;
        this.maxY = centerY + offset;
    }

    public Set<Coordinate> getOccupiedCoordinates() {
        Set<Coordinate> coordinates = new HashSet<>();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                coordinates.add(new Coordinate(x, y));
            }
        }
        return coordinates;
    }

    /**
     * Staff-level DDD: The entity knows if it has been hit.
     */
    public boolean isHit(int x, int y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }
}