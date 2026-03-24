package com.kotak.battleship.strategy;

import com.kotak.battleship.domain.Coordinate;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class RandomFireStrategy implements FireStrategy {
    @Override
    public Coordinate getTarget(int minX, int maxX, int minY, int maxY, Set<Coordinate> firedShots) {
        Coordinate coord;
        int maxAttempts = 1000; // Prevent infinite loops if field is full
        int attempts = 0;

        do {
            int x = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
            int y = ThreadLocalRandom.current().nextInt(minY, maxY + 1);
            coord = new Coordinate(x, y);
            attempts++;
        } while (firedShots.contains(coord) && attempts < maxAttempts);

        return coord;
    }
}