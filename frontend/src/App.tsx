import { useEffect, useState } from "react";
import "./App.css";
import type { EnergyReport, OptimalWindowReport } from "./types";
import WindowSection from "./components/WindowSection"; 
import ForecastCard from "./components/ForecastCard";
import StatusScreen from "./components/StatusScreen";

/**
 * Main Application Component
 */
export default function App() {
  // --- STATE MANAGEMENT ---
  const [reports, setReports] = useState<EnergyReport[]>([]);
  const [optimalWindow, setOptimalWindow] = useState<OptimalWindowReport | null>(null);
  const [duration, setDuration] = useState<number>(2);

  // UI States for Render.com cold start handling
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isError, setIsError] = useState<boolean>(false);

  // --- INITIAL DATA FETCH ---
  // Fetches the general energy mix forecast when the app loads.
  useEffect(() => {
    // Use environment variable for Production (Render) or localhost for Development
    const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
    
    // Start loading
    setIsLoading(true);

    fetch(`${API_URL}/api/energy/mix`)
      .then((res) => {
        if (!res.ok) throw new Error("Network response was not ok");
        return res.json();
      })
      .then((data) => {
        setReports(data);
        setIsLoading(false); // Stop loading on success
      })
      .catch((err) => {
        console.error("Forecast API error:", err);
        setIsError(true);
        setIsLoading(false); // Stop loading on error
      });
  }, []);

  // --- USER ACTIONS ---
  // Triggers the algorithm on the backend to find the best time window based on user input
  const findBestTime = () => {
    const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080";
    
    fetch(`${API_URL}/api/energy/optimal?hours=${duration}`)
      .then((res) => res.json())
      .then((data) => setOptimalWindow(data))
      .catch((err) => console.error("Advisor API error:", err));
  };

  // --- RENDER: LOADING STATE (Cold Start) ---
  if (isLoading || isError) {
    return (
      <StatusScreen 
        isLoading={isLoading} 
        isError={isError} 
        onRetry={() => window.location.reload()} 
      />
    );
  }


  // --- RENDER: MAIN APP ---
  return (
    <div className="container">
      <header>
        <h1>Generation Mix for the GB power system</h1>
      </header>

      {/*Intelligent Advisor (Interactive) */}
      <WindowSection 
        duration={duration} 
        setDuration={setDuration} 
        onSearch={findBestTime} 
        result={optimalWindow} 
      />

      {/*Forecast Display*/}
      <section className="forecast-section">
        <h2>Current and Forecasted UK Energy Mix</h2>
        <div className="grid">
          {reports.map((report, index) => (
            <ForecastCard key={index} report={report} />
          ))}
        </div>
      </section>
    </div>
  );
}