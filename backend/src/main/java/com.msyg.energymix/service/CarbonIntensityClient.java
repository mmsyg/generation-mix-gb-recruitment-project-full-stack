package com.msyg.energymix.service;

import com.msyg.energymix.model.external.ExternalApiResponse;
import com.msyg.energymix.model.external.GenerationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * Client responsible for communication with the Carbon Intensity API (UK).
 */
@Component // Registers this class as a Spring Bean
@RequiredArgsConstructor
public class CarbonIntensityClient {

    private static final String API_BASE_URL = "https://api.carbonintensity.org.uk/generation";
    private final RestClient restClient = RestClient.create();

    public List<GenerationData> fetchForecastData() {
        // Calculate date range: from Now to +3 Days
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysForward = now.plusDays(3);

        // Format dates to match API requirements (ISO format)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");
        String from = now.format(formatter);
        String to = threeDaysForward.format(formatter);

        // Construct dynamic URL
        String dynamicUrl = String.format("%s/%s/%s", API_BASE_URL, from, to);

        try {
            // Execute HTTP GET request
            ExternalApiResponse response = restClient.get()
                    .uri(dynamicUrl).retrieve().body(ExternalApiResponse.class);

            // Validate response
            if (response == null || response.getData() == null) return Collections.emptyList();

            return response.getData();

        } catch (Exception e) {
            // Log error and return empty list to avoid crashing the app
            System.out.println("API Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}