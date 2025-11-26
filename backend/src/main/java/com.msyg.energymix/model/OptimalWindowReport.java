package com.msyg.energymix.model;

import lombok.Builder;
import lombok.Data;
import java.time.ZonedDateTime;

@Data
@Builder
public class OptimalWindowReport {
    private ZonedDateTime start;
    private ZonedDateTime end;
    private double avgCleanEnergyPercent;
}