# React + TypeScript + Vite

A modern, interactive dashboard built with React and TypeScript that helps users visualize the UK energy mix and find the eco-friendliest time to use electricity.

## Features

* **Intelligent Advisor:** Interactive slider to select window of optimal charging using renewable energy.
* **Data Visualization:** Beautiful Donut Charts (using MUI X Charts) showing the renewable vs. fossil fuel breakdown.
* **Dark Mode UI:** Sleek, responsive design styled with custom CSS and CSS variables.
* **Dynamic Coloring:** Visual indicators (Green for renewable, Grey for non-renewable) consistent across charts and lists.

## Tech Stack

* **Framework:** React 18 (Vite)
* **Language:** TypeScript
* **Visualization:** MUI X Charts
* **Components:** Material UI (Slider)
* **Deployment:** Render (Static Site)

## How to Run

### Prerequisites
* Node.js (v18 or newer)
* NPM

### Installation

1. Navigate to frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

### Local Development

1. Ensure the Backend is running on port 8080.
2. Start the frontend:
   ```bash
   npm run dev
   ```
   The app will be available at `http://localhost:5173`.

## Environment Variables

| Variable | Description | Default |
| :--- | :--- | :--- |
| `VITE_API_URL` | The base URL of the Java Backend. | `http://localhost:8080` |

## Project Structure

* `src/components`: Reusable UI components (ForecastCard, WindowSection).
* `src/types.ts`: TypeScript interfaces matching the Java DTOs.
* `src/utils.ts`: Helper functions (e.g., fuel color mapping logic).
