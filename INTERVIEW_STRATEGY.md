# 120-Minute Execution Strategy (Staff Level)

## Phase 1: Requirements & Discovery (15 Mins)
**Goal: Show "System Thinking".**
- **Clarify Ship Math**: Confirm if $S$ is always even. If $S=3$ and center is $(2,2)$, is the range $[1,3]$ or $[0.5, 3.5]$? (Assume integers: $X \pm S/2$).
- **Clarify Hit Logic**: Does one hit destroy the *entire* ship (as per the doc) or just that cell? (Doc says: "In case of a hit, the opponent's ship is destroyed"). This is a huge simplification—confirm it!
- **Edge Cases**: What if $N$ is odd? (Suggest $N$ must be even for fair split).

## Phase 2: Coding - The "Clean Core" (75 Mins)
**Goal: Working, Readable, and Robust Code.**
- **0-10m**: Define Domain Models (`Coordinate`, `Ship`, `Player`). Use Records if using Java 17+.
- **10-30m**: Implement `addShip` with validation. This is where most candidates fail. Ensure you check for **Boundary Overlap** and **Inter-ship Overlap**.
- **30-60m**: Implement `startGame` loop. Use a `Set<Coordinate>` for tracking fired shots to satisfy the "No repeat shots" constraint efficiently ($O(1)$).
- **60-75m**: Implement `viewBattleField` and the `Main` driver.

## Phase 3: LLD & Schema Discussion (30 Mins)
**Goal: Defend your Design.**
- **Patterns Used**: 
    - **Strategy Pattern**: For firing logic (allows switching from Random to Smart AI).
    - **State Pattern**: Could be used for Game Phases (Init, Active, Over).
- **Concurrency**: Discuss how to make this thread-safe if it were a multiplayer server (e.g., `ReadWriteLock` on the Player state).
- **Schema**: 
    - `Game`: `id, n, status, winner_id`
    - `Ship`: `id, game_id, owner_id, min_x, max_x, min_y, max_y, is_destroyed`