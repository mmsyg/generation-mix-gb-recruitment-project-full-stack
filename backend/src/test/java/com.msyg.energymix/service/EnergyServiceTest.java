package com.msyg.energymix.service;

import com.msyg.energymix.model.OptimalWindowReport;
import com.msyg.energymix.model.external.GenerationData;
import com.msyg.energymix.model.external.GenerationItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enables Mockito
class EnergyServiceTest {

    @Mock
    private CarbonIntensityClient apiClient; // Mock: The fake API client

    @InjectMocks
    private EnergyService energyService; // The real service we are testing

    @Test
    void shouldFindBestWindowIdeally() {
        // GIVEN: Scenario where energy is low at first, then high
        List<GenerationData> fakeData = createMockDataSequence();

        when(apiClient.fetchForecastData()).thenReturn(fakeData);

        // WHEN: Run the algorithm for a 1h window (2 slots)
        OptimalWindowReport result = energyService.findOptimalWindow(1);

        // THEN: Check the results
        assertNotNull(result);

        // Expected the high energy window (100% wind) which is at 12:30
        // Our fake data starts at 12:00.
        // Slot 0 (12:00): 0% clean
        // Slot 1 (12:30): 100% clean
        // Slot 2 (13:00): 100% clean

        // The best 1h window (2 slots) is 12:30 - 13:30 (Average 100%)
        assertEquals(100.0, result.getAvgCleanEnergyPercent());

        // Check times (Mock data uses "2025-01-01")
        assertEquals("2025-01-01T12:30Z", result.getStart().toString());
    }

    @Test
    void shouldReturnNullWhenNotEnoughData() {
        // GIVEN: API returns empty list
        when(apiClient.fetchForecastData()).thenReturn(Collections.emptyList());

        // WHEN
        OptimalWindowReport result = energyService.findOptimalWindow(2);

        // THEN
        assertNull(result);
    }

    // --- Helper to create fake data ---
    private List<GenerationData> createMockDataSequence() {
        List<GenerationData> list = new ArrayList<>();

        // Slot 1: 12:00 -> Dirty energy (Coal only)
        list.add(createSlot("2025-01-01T12:00Z", "2025-01-01T12:30Z", "coal", 100));

        // Slot 2: 12:30 -> Clean energy (Wind only)
        list.add(createSlot("2025-01-01T12:30Z", "2025-01-01T13:00Z", "wind", 100));

        // Slot 3: 13:00 -> Clean energy (Wind only)
        list.add(createSlot("2025-01-01T13:00Z", "2025-01-01T13:30Z", "wind", 100));

        return list;
    }

    private GenerationData createSlot(String from, String to, String fuel, double perc) {
        GenerationData data = new GenerationData();
        data.setFrom(from);
        data.setTo(to);

        GenerationItem item = new GenerationItem();
        item.setFuel(fuel);
        item.setPerc(perc);

        data.setGenerationMix(List.of(item));
        return data;
    }
}