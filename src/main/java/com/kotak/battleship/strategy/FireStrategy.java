package com.kotak.battleship.strategy;

import java.util.Set;

import com.kotak.battleship.domain.Coordinate;

public interface FireStrategy {
    Coordinate getTarget(int minX, int maxX, int minY, int maxY, Set<Coordinate> firedShots);
}
