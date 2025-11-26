package com.msyg.energymix.controller;

import com.msyg.energymix.service.EnergyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EnergyController.class) // Test only the Controller layer
class EnergyControllerTest {

    @Autowired
    private MockMvc mockMvc; // Tool to simulate HTTP requests

    @MockBean
    private EnergyService energyService; // Mock the service (we don't test logic here)

    @Test
    void shouldReturn200ForForecast() throws Exception {
        // GIVEN
        when(energyService.getThreeDayForecast()).thenReturn(Collections.emptyList());

        // WHEN & THEN
        mockMvc.perform(get("/api/energy/mix"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn200ForOptimalWindow() throws Exception {
        // WHEN & THEN
        mockMvc.perform(get("/api/energy/optimal?hours=2"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldValidateInputParameters() throws Exception {
        // Even if user sends hours=-5, controller should handle it (logic inside controller/service)
        // Here we just check if it doesn't crash (500 error)
        mockMvc.perform(get("/api/energy/optimal?hours=-5"))
                .andExpect(status().isOk());
    }
}