package com.kotak.battleship;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BattleShipApplication {

    public static void main(String[] args) {
        SpringApplication.run(BattleShipApplication.class, args);
        System.out.println(">>> Battleship Game Engine is running on http://localhost:8080");
        System.out.println(">>> H2 Console available at http://localhost:8080/h2-console");
    }
}