# Problem Statement: Battleship Game Engine

## Overview
Design and implement a turn-based battleship game played between two players (PlayerA and PlayerB) on an $N \times N$ square grid.

## Core Requirements
1. **The Battlefield**:
   - The grid is $N \times N$.
   - Divided vertically: Left half ($0$ to $N/2 - 1$) belongs to PlayerA; Right half ($N/2$ to $N-1$) belongs to PlayerB.
2. **Ships**:
   - Square-shaped and stationary.
   - Placement: Given by center $(X, Y)$ and Size $S$. 
   - **Area Logic**: A ship of size $S$ at $(X, Y)$ occupies coordinates from $(X - S/2)$ to $(X + S/2)$ in both axes. 
   - *Example*: Size 4 at (2, 2) occupies points (0,0) through (4,4).
   - Constraints: No overlaps allowed. Ships can touch boundaries.
3. **Gameplay**:
   - Strategy: "Random Coordinate Fire".
   - Turn-based: PlayerA starts first. Turn alternates regardless of Hit/Miss.
   - Constraints: No coordinate can be attacked twice in the same game.
4. **Win Condition**:
   - A ship is destroyed if any of its occupied coordinates are hit.
   - A player loses when all their ships are destroyed.

## Mandatory APIs
- `initGame(int n)`: Initialize grid and players.
- `addShip(String id, int size, int xA, int yA, int xB, int yB)`: Add a ship to both fleets.
- `startGame()`: Run the simulation until a winner emerges.
- `viewBattleField()`: Display the current grid state.