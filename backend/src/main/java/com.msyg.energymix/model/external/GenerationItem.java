package com.msyg.energymix.model.external;

import lombok.Data;

/**
 * Represents a single energy source and its contribution (%)
 */
@Data
public class GenerationItem {
    private String fuel;
    private double perc;
}