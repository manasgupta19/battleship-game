
# ⚓ Battleship Game Engine

A production-grade Spring Boot implementation of the classic Battleship game. This engine simulates a turn-based battle between two players on a shared $N \times N$ battlefield, featuring bounding-box collision detection, pluggable firing strategies, and a persistent audit trail.

## 🚀 Quick Start (VS Code)

### Prerequisites
* **Java 17+** (OpenJDK recommended)
* **Maven 3.8+** (or use the included `mvnw` wrapper)
* **VS Code Extensions**: 
    * *Extension Pack for Java*
    * *REST Client* (by Huachao Mao)

### Installation & Running
1. **Clone the project** and open the root folder in VS Code.
2. **Clean and Build**:
   ```powershell
   .\mvnw clean compile
   ```
3. **Run the Application**:
   ```powershell
   .\mvnw spring-boot:run
   ```
4. The server will start at `http://localhost:8080`.

---

## 🛠 Architecture & Design Decisions

This project follows **Domain-Driven Design (DDD)** principles and a **Layered Architecture** to ensure maintainability and scalability.

### 1. Bounding Box Model ($O(1)$ Optimization)
Instead of storing every coordinate a ship occupies in the database ($O(S^2)$ storage), we utilize a **Bounding Box** model (`minX`, `maxX`, `minY`, `maxY`). 
* **Benefit**: Reduces database rows significantly.
* **Logic**: Hit detection and boundary validation are performed using simple range comparisons rather than collection lookups.

### 2. Strategy Pattern for Firing logic
The "Random Coordinate Fire" is implemented using a `FireStrategy` interface. 
* **Evolvability**: This allows the engine to support `ProbabilityFireStrategy` or `TargetedStrikeStrategy` in the future without modifying the core game service (Open/Closed Principle).

### 3. Dual-Write State Management
* **Fast Path (In-Memory)**: Active game state (grid maps, fleet health) is kept in `ConcurrentHashMap` for sub-millisecond simulation speed.
* **Audit Path (Database)**: Every ship and game outcome is persisted to an **H2 In-Memory Database** for record-keeping and observability.

---

## 📡 API Reference

### 1. Initialize Game
`POST /api/v1/game/init?n={size}`
Initializes an $N \times N$ battlefield divided equally between Player A and Player B.

### 2. Add Ship
`POST /api/v1/game/ship`
Adds a square ship to both players.
**Payload:**
```json
{
    "id": "SH1",
    "size": 2,
    "xA": 2, "yA": 2,
    "xB": 12, "yB": 2
}
```

### 3. Start Simulation
`POST /api/v1/game/start`
Executes the game loop until one player's fleet is entirely destroyed. Returns a chronological log of events.

### 4. View Battlefield
`GET /api/v1/game/view`
Returns a text-based grid representation of the current battlefield state.

---

## 🧪 Testing

### Automated API Testing
The project includes an `api_tests.http` file. 
1. Open the file in VS Code.
2. Click **"Send Request"** above any endpoint to execute the test.
3. Observe the `200 OK` responses and JSON payloads in the split window.

### Database Inspection
To view the underlying data structures during or after a game:
1. Navigate to `http://localhost:8080/h2-console`.
2. **JDBC URL**: `jdbc:h2:mem:battleshipdb`
3. **User**: `sa` | **Password**: `password`
4. Run `SELECT * FROM SHIP;` to see the bounding box data.

---

## ⚠️ Known Constraints
* **Grid Parity**: $N$ must be an even integer to ensure equal territory distribution.
* **Ship Geometry**: Ships are strictly square as per the current requirement spec.
* **Persistence**: Data is volatile (In-memory H2) and resets upon application restart.

---