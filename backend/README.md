# Energy Mix Backend API

A Spring Boot application that provides real-time and forecasted energy mix data for the Great Britain power system. It serves as the computational core for the Energy Dashboard, offering intelligent suggestions for optimal energy usage times.

## Key Features

* **External API Integration:** Fetches real-time data from the Carbon Intensity API (UK).
* **Data Aggregation:** Uses Java Streams to process 30-minute resolution data into daily averages.
* **Optimization Algorithm:** Implements a Sliding Window Algorithm to find the best time window (e.g., 2 hours) for energy consumption based on renewable availability.
* **Robust Error Handling:** Includes fallback mechanisms and data normalization.
* **Time Zone Awareness:** Handles UTC (ZonedDateTime) to ensure accurate global timing.

## Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.2
* **Build Tool:** Maven
* **HTTP Client:** Spring RestClient
* **Testing:** JUnit 5, Mockito, MockMvc
* **Deployment:** Docker / Render.com

## API Endpoints

The API exposes the following endpoints under `/api/energy`:

| Method | Endpoint | Description | Query Params |
| :--- | :--- | :--- | :--- |
| `GET` | `/mix` | Returns a 3-day forecast (Today + 2 days) with aggregated daily energy mix. | - |
| `GET` | `/optimal` | Finds the best time window to use energy within the next 48h. | `?hours=N` (Duration, default: 2) |

### Example Response (`/api/energy/optimal?hours=2`)

```json
{
  "start": "2025-11-27T13:00:00Z",
  "end": "2025-11-27T15:00:00Z",
  "avgCleanEnergyPercent": 68.5
}
```

## How to Run

### Prerequisites
* Java 21 SDK
* Maven (optional, wrapper included)

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/mmsyg/generation-mix-gb-recruitment-project-full-stack.git
   ```

2. Navigate to backend:
   ```bash
   cd backend
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The server will start on `http://localhost:8080`.

### Running Tests

```bash
./mvnw test
```

## Architecture

The project follows a clean layered architecture:
* **Controller:** Handles HTTP requests and input validation.
* **Service:** Contains business logic (Sliding Window, Aggregation).
* **Client:** Encapsulates external API communication.
