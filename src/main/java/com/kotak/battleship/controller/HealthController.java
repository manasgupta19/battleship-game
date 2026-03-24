package com.kotak.battleship.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of(
            "status", "UP",
            "message", "Battleship System Initialized",
            "db", "H2 In-Memory connected"
        );
    }
}