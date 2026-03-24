package com.kotak.battleship.controller;

import com.kotak.battleship.dto.ShipRequest;
import com.kotak.battleship.service.BattleShipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class BattleShipController {

    private final BattleShipService gameService;

    @PostMapping("/init")
    public ResponseEntity<String> initGame(@RequestParam int n) {
        gameService.initializeGame(n);
        return ResponseEntity.ok("Battlefield of size " + n + "x" + n + " initialized.");
    }

    @PostMapping("/ship")
    public ResponseEntity<String> addShip(@RequestBody ShipRequest request) {
        gameService.addShip(request);
        return ResponseEntity.ok("Ship " + request.getId() + " added to both fleets.");
    }

    @PostMapping("/start")
    public ResponseEntity<List<String>> startGame() {
        List<String> logs = gameService.playGame();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/view")
    public ResponseEntity<String> viewBattlefield() {
        return ResponseEntity.ok(gameService.getBattlefieldView());
    }
}
