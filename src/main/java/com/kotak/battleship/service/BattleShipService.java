package com.kotak.battleship.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kotak.battleship.domain.Coordinate;
import com.kotak.battleship.domain.GameState;
import com.kotak.battleship.domain.PlayerState;
import com.kotak.battleship.domain.Ship;
import com.kotak.battleship.dto.ShipRequest;
import com.kotak.battleship.exception.GameException;
import com.kotak.battleship.repository.GameRepository;
import com.kotak.battleship.repository.ShipRepository;
import com.kotak.battleship.strategy.FireStrategy;
import com.kotak.battleship.strategy.RandomFireStrategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BattleShipService {

    private final GameRepository gameRepository;
    private final ShipRepository shipRepository;
    private final FireStrategy fireStrategy = new RandomFireStrategy();

    private GameState currentGameState;
    // Updated to use the new PlayerState domain object
    private final Map<String, PlayerState> playerStates = new ConcurrentHashMap<>();

    public void initializeGame(int n) {
        if (n <= 0 || n % 2 != 0) {
            throw new GameException("Grid size N must be positive and even.", HttpStatus.BAD_REQUEST);
        }

        currentGameState = GameState.builder()
                .gridSize(n)
                .isGameOver(false)
                .currentTurnPlayer("PlayerA")
                .build();
        
        gameRepository.save(currentGameState);

        // Initialize using the new PlayerState class
        playerStates.put("PlayerA", new PlayerState("PlayerA", 0, n / 2 - 1));
        playerStates.put("PlayerB", new PlayerState("PlayerB", n / 2, n - 1));
        
        log.info("Game initialized for PlayerA and PlayerB");
    }

    public void addShip(ShipRequest request) {
        log.info("Received Ship Request: ID={}, Size={}, xA={}, yA={}", 
             request.getId(), request.getSize(), request.getXA(), request.getYA());
        validateGameInitialized();
        addShipToPlayer("PlayerA", request.getId(), request.getSize(), request.getXA(), request.getYA());
        addShipToPlayer("PlayerB", request.getId(), request.getSize(), request.getXB(), request.getYB());
    }

    private void addShipToPlayer(String playerName, String id, int size, int x, int y) {
        PlayerState p = playerStates.get(playerName);
        
        // Create ship with "A-" or "B-" prefix for DB Primary Key
        String shortName = playerName.equals("PlayerA") ? "A" : "B";
        Ship ship = new Ship(shortName, id, playerName, size, x, y);

        // Validation using Bounding Box
        validateShipPlacement(p, ship);

        // Save to unified SHIP table
        shipRepository.save(ship);

        // Memory optimization: We still populate the gridMap for O(1) hit lookup in the loop
        p.getFleet().add(ship);
        for (int i = ship.getMinX(); i <= ship.getMaxX(); i++) {
            for (int j = ship.getMinY(); j <= ship.getMaxY(); j++) {
                p.getGridMap().put(new Coordinate(i, j), ship);
            }
        }
        
        log.info("Persisted Ship {} for {} with bounds [X:{}-{}, Y:{}-{}]", 
                 ship.getId(), playerName, ship.getMinX(), ship.getMaxX(), ship.getMinY(), ship.getMaxY());
    }

    private void validateShipPlacement(PlayerState p, Ship ship) {
        // Check corners of the bounding box
        Coordinate[] corners = {
            new Coordinate(ship.getMinX(), ship.getMinY()),
            new Coordinate(ship.getMaxX(), ship.getMaxY())
        };

        for (Coordinate c : corners) {
            if (!p.isCoordinateInTerritory(c, currentGameState.getGridSize())) {
                throw new GameException(p.getName() + " ship out of bounds at (" + c.x() + "," + c.y() + ")", HttpStatus.BAD_REQUEST);
            }
        }

        // Check for overlaps with existing ships in memory
        for (int i = ship.getMinX(); i <= ship.getMaxX(); i++) {
            for (int j = ship.getMinY(); j <= ship.getMaxY(); j++) {
                if (p.getGridMap().containsKey(new Coordinate(i, j))) {
                    throw new GameException("Ship overlap at (" + i + "," + j + ")", HttpStatus.CONFLICT);
                }
            }
        }
    }

    public List<String> playGame() {
        validateGameInitialized();
        List<String> logs = new ArrayList<>();
        
        PlayerState attacker = playerStates.get("PlayerA");
        PlayerState defender = playerStates.get("PlayerB");

        while (!currentGameState.isGameOver()) {
            Coordinate target = fireStrategy.getTarget(
                defender.getMinX(), defender.getMaxX(), 0, currentGameState.getGridSize() - 1, attacker.getFiredShots()
            );
            attacker.getFiredShots().add(target);

            String turnResult = attacker.getName() + "'s turn: Missile fired at (" + target.x() + ", " + target.y() + "). ";

            if (defender.getGridMap().containsKey(target)) {
                Ship hitShip = defender.getGridMap().get(target);
                turnResult += "\"Hit\". " + defender.getName() + "'s ship with id \"" + hitShip.getId() + "\" destroyed.";
                
                defender.getFleet().remove(hitShip);
                hitShip.getOccupiedCoordinates().forEach(defender.getGridMap()::remove);
            } else {
                turnResult += "\"Miss\".";
            }

            logs.add(turnResult);

            if (defender.getFleet().isEmpty()) {
                currentGameState.setGameOver(true);
                currentGameState.setWinner(attacker.getName());
                logs.add(attacker.getName() + " wins the game!");
                gameRepository.save(currentGameState);
                break;
            }

            // Swap roles
            PlayerState temp = attacker;
            attacker = defender;
            defender = temp;
        }
        return logs;
    }

    public String getBattlefieldView() {
        validateGameInitialized();
        StringBuilder sb = new StringBuilder();
        int n = currentGameState.getGridSize();

        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                Coordinate c = new Coordinate(x, y);
                String cell = ".    ";
                if (playerStates.get("PlayerA").getGridMap().containsKey(c)) {
                    cell = "A-" + playerStates.get("PlayerA").getGridMap().get(c).getId() + " ";
                } else if (playerStates.get("PlayerB").getGridMap().containsKey(c)) {
                    cell = "B-" + playerStates.get("PlayerB").getGridMap().get(c).getId() + " ";
                }
                sb.append(cell);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void validateGameInitialized() {
        if (currentGameState == null) {
            throw new GameException("Game not initialized.", HttpStatus.PRECONDITION_FAILED);
        }
    }

}