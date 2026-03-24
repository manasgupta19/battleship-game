package com.kotak.battleship.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class GameException extends RuntimeException {
    private final HttpStatus status;

    public GameException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}