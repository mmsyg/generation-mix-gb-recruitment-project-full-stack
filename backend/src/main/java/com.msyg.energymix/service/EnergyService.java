package com.msyg.energymix.service;

import com.msyg.energymix.model.EnergyReport;
import com.msyg.energymix.model.EnergySource;
import com.msyg.energymix.model.OptimalWindowReport;
import com.msyg.energymix.model.external.GenerationData;
import com.msyg.energymix.model.external.GenerationItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class EnergyService {

    private final CarbonIntensityClient apiClient;

    /**
     * Retrieves energy mix forecast including today and the following 2 days, aggregated by day
     */
    public List<EnergyReport> getThreeDayForecast() {
        // Fetch raw data from external API
        List<GenerationData> forecastData = apiClient.fetchForecastData();

        if (forecastData.isEmpty()) return Collections.emptyList();
        // Group 30-min intervals by Date
        Map<LocalDate, List<GenerationData>> groupedByDay = forecastData.stream()
                .collect(Collectors.groupingBy(item ->
                        ZonedDateTime.parse(item.getFrom()).toLocalDate()
                ));

        // Calculate daily averages and sort chronologically
        return groupedByDay.entrySet().stream()
                .map(entry -> calculateDailyAverage(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(EnergyReport::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Finds the optimal time window, with the highest renewable energy %
     */
    public OptimalWindowReport findOptimalWindow(int durationInHours) {
        List<GenerationData> timeSeries = apiClient.fetchForecastData();

        if (timeSeries.isEmpty()) return null;

        // Convert requested hours to 30-min slots
        int requiredSlots = durationInHours * 2;

        // Validation: Ensure we have enough data points
        if (timeSeries.size() < requiredSlots) return null;

        double maxAverage = -1.0;
        GenerationData bestStartSlot = null;
        GenerationData bestEndSlot = null;

        // Sliding Window Algorithm
        for (int i = 0; i <= timeSeries.size() - requiredSlots; i++) {
            double sumPercentInWindow = 0.0;

            for (int j = 0; j < requiredSlots; j++) {
                GenerationData slot = timeSeries.get(i + j);
                sumPercentInWindow += calculateCleanPercentForSlot(slot);
            }

            double currentAverage = sumPercentInWindow / requiredSlots;

            if (currentAverage > maxAverage) {
                maxAverage = currentAverage;
                bestStartSlot = timeSeries.get(i);
                bestEndSlot = timeSeries.get(i + requiredSlots - 1);
            }
        }

        // Build result object
        if (bestStartSlot != null && bestEndSlot != null) {
            ZonedDateTime start = ZonedDateTime.parse(bestStartSlot.getFrom());
            ZonedDateTime end = ZonedDateTime.parse(bestEndSlot.getTo());

            return OptimalWindowReport.builder()
                    .start(start)
                    .end(end)
                    .avgCleanEnergyPercent(maxAverage)
                    .build();
        }
        return null;
    }

    // --- HELPER METHODS ---

    private EnergyReport calculateDailyAverage(LocalDate date, List<GenerationData> dayData) {
        // Flatten the list and calculate average per fuel type
        Map<String, Double> averageMap = dayData.stream()
                .flatMap(period -> period.getGenerationMix().stream())
                .collect(Collectors.groupingBy(
                        GenerationItem::getFuel,
                        Collectors.averagingDouble(GenerationItem::getPerc)
                ));

        List<EnergySource> sources = averageMap.entrySet().stream()
                .map(entry -> new EnergySource(
                        entry.getKey(),
                        entry.getValue(),
                        isRenewable(entry.getKey())
                ))
                .collect(Collectors.toList());

        // Normalize percentages to ensure they sum up to 100%
        List<EnergySource> normalizedSources = normalizeTo100Percent(sources);

        double cleanPercent = normalizedSources.stream()
                .filter(EnergySource::isRenewable)
                .mapToDouble(EnergySource::getPercentage)
                .sum();

        return EnergyReport.builder()
                .date(date)
                .sources(normalizedSources)
                .cleanEnergyPercent(cleanPercent)
                .build();
    }

    private double calculateCleanPercentForSlot(GenerationData slot) {
        double total = 0;
        double renewable = 0;

        for (GenerationItem item : slot.getGenerationMix()) {
            total += item.getPerc();
            if (isRenewable(item.getFuel())) {
                renewable += item.getPerc();
            }
        }
        return total > 0 ? (renewable / total) * 100 : 0;
    }

    private boolean isRenewable(String fuel) {
        return switch (fuel.toLowerCase()) {
            case "wind", "solar", "hydro", "biomass" -> true;
            default -> false;
        };
    }

    /**
     * Fixes rounding errors to ensure the pie chart adds up to exactly 100%
     */
    private List<EnergySource> normalizeTo100Percent(List<EnergySource> sources) {
        if (sources.isEmpty()) return sources;

        double totalSum = sources.stream()
                .mapToDouble(EnergySource::getPercentage)
                .sum();

        // Ignore small rounding errors (e.g. 99.9% or 100.1%)
        if (Math.abs(totalSum - 100.0) < 0.5) {
            return sources;
        }

        // Prevent division by zero
        if (totalSum == 0) return sources;

        // Scale values: (Value / Total) * 100
        return sources.stream()
                .map(source -> new EnergySource(
                        source.getName(),
                        (source.getPercentage() / totalSum) * 100.0,
                        source.isRenewable()
                ))
                .collect(Collectors.toList());
    }
}