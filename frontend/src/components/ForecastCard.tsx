import { PieChart } from "@mui/x-charts/PieChart";
import type { EnergyReport } from "../types";
import { getFuelColor } from "../utils";

interface ForecastCardProps {
  report: EnergyReport;
}

/**
 * Component responsible for displaying a single day's energy forecast.
 * Includes a summary percentage, a pie chart visualization, and a detailed list.
 */
export default function ForecastCard({ report }: ForecastCardProps) {
  return (
    <div className="card forecast-card">
      
      {/*Header: Date formatted for UK locale */}
      <h3>
        {new Date(report.date).toLocaleDateString("en-GB", {
          weekday: "long", day: "numeric", month: "short", timeZone: "Europe/London",
        })}
      </h3>

      {/*Key Metric: Total Renewable Percentage*/}
      {/* Color coding: Green if > 50%, Yellow otherwise for visual feedback */}
      <div className="main-percent-container" style={{ color: report.cleanEnergyPercent > 50 ? "#4ade80" : "#facc15" }}>
        {report.cleanEnergyPercent.toFixed(0)}%
        <span className="main-percent-label">Renewable Energy</span>
      </div>

      {/*Visualization: Pie Chart */}
      <div className="chart-container">
        <PieChart
          series={[
            {
              data: report.sources.map((source, idx) => ({
                id: idx,
                value: source.percentage,
                label: source.name, 
                color: getFuelColor(source.name, source.renewable), 
              })),
              innerRadius: 0,
              outerRadius: 90,
              paddingAngle: 0,
              cornerRadius: 0,
              faded: { innerRadius: 30, additionalRadius: -30, color: 'gray' },
            },
          ]}
          // hide ledend default legend via CSS override
          sx={{ "& .MuiChartsLegend-root": { display: "none !important" } }}
          width={300}
          height={200}
        />
      </div>

      {/*Detailed Breakdown List*/}
      <div className="details-container">
        <p className="details-header">Energy Mix Details</p>

        <ul className="details-list">
          {report.sources
            // Sort desc by percentage to show main contributors first
            .sort((a, b) => b.percentage - a.percentage)
            .map((source) => {
              // Resolve color for the list bullet point
              const itemColor = getFuelColor(source.name, source.renewable);
              
              return (
                <li key={source.name} className="details-item">
                  <div className="details-item-left">
                    {/* Colored Dot Indicator */}
                    <div 
                      className="color-dot" 
                      style={{ backgroundColor: itemColor}}
                    ></div>
                    <span className="fuel-name">{source.name}</span>
                  </div>
                  
                  <span className="details-percent" >
                    {source.percentage.toFixed(1)}%
                  </span>
                </li>
              );
            })}
        </ul>
      </div>
    </div>
  );
}