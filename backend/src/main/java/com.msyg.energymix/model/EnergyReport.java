package com.msyg.energymix.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents a daily summary of the energy mix.
 * This object is the result of aggregating raw data and is sent to the frontend.
 */
@Data
@Builder
public class EnergyReport {
    private LocalDate date;
    private List<EnergySource> sources;
    private double cleanEnergyPercent;
}