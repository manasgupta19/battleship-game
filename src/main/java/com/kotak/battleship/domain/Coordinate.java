package com.kotak.battleship.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public record Coordinate(int x, int y) {
    // No-args constructor required by JPA for Embeddables
    public Coordinate() {
        this(0, 0);
    }
}