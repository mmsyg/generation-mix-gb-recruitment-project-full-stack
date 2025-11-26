package com.msyg.energymix.model;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Represents the result of the energy optimization algorithm.
 */
@Data
@AllArgsConstructor
public class EnergySource {
    private String name;
    private double percentage;
    private boolean renewable;
}