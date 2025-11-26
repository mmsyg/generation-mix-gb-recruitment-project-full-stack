import Slider from "@mui/material/Slider";
import type { OptimalWindowReport } from "../types";

interface WindowSectionProps {
  duration: number;
  setDuration: (val: number) => void;
  onSearch: () => void;
  result: OptimalWindowReport | null;
}

/**
 * Component: Intelligent Advisor (Window Section)
 * * Allows the user to input a duration via Slider and triggers the search 
 * for the optimal energy consumption window. Displays the result if available.
 */
export default function WindowSection({ duration, setDuration, onSearch, result }: WindowSectionProps) {
  return (
    <section className="window-section">
      <div className="card window-card">
        <h2>Finding the Best Clean-Energy Charging Window</h2>
        <p>Next 48h</p>

        <div className="window-input-group">
          {/*Input: Time Duration Slider */}
          <div className="slider-container">
            <Slider
              aria-label="hours"
              value={duration}
              onChange={(_, newValue) => setDuration(newValue as number)}
              valueLabelDisplay="auto"
              step={1}
              marks
              min={1}
              max={6}
              color="success"
              sx={{ color: "#4ade80" }}
            />
          </div>
          
          {/*Controls: Display Label + Search Button*/}
          <div className="window-controls">
            <span className="window-hours-label">
              {duration} hours
            </span>
            <button onClick={onSearch}>Search</button>
          </div>
        </div>

        {/*Result Display (Rendered only if API returns data)*/}
        {result && (
          <div className="result-box">
            <h3>Best Clean-Energy Charging Window</h3>
            
            <div className="time-display">
              {/* Start Time */}
              {new Date(result.start).toLocaleTimeString("en-GB", {
                hour: "2-digit", minute: "2-digit", timeZone: "Europe/London",
              })}
              {" to "}
              {/* End Time */}
              {new Date(result.end).toLocaleTimeString("en-GB", {
                hour: "2-digit", minute: "2-digit", timeZone: "Europe/London",
              })}
            </div>
            
            <p className="green-text">
              Day:{" "}
              {new Date(result.start).toLocaleDateString("en-GB", {
                weekday: "long", day: "numeric", month: "long", timeZone: "Europe/London",
              })}
            </p>
            
            <p>
              Clean-Energy: <strong>{result.avgCleanEnergyPercent.toFixed(1)}%</strong>
            </p>
          </div>
        )}
      </div>
    </section>
  );
}