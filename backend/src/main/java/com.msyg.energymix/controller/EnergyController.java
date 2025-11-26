package com.msyg.energymix.controller;

import com.msyg.energymix.model.EnergyReport;
import com.msyg.energymix.model.OptimalWindowReport;
import com.msyg.energymix.service.EnergyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/energy")
@RequiredArgsConstructor
public class EnergyController {

    private final EnergyService energyService;

    /**
     * Retrieves the energy mix forecast for the next 3 days.
     */
    @GetMapping("/mix")
    public List<EnergyReport> getMix() {
        return energyService.getThreeDayForecast();
    }

    /**
     * Calculates the optimal time window for high energy consumption based on user input.
     * @param hours duration of the window in hours (1-6).
     */
    @GetMapping("/optimal")
    public OptimalWindowReport getOptimalWindow(@RequestParam int hours) {
        // Input validation: constrain hours to the range (1 to 6)
        if (hours < 1) hours = 1;
        if (hours > 6) hours = 6;


        return energyService.findOptimalWindow(hours);
    }
}