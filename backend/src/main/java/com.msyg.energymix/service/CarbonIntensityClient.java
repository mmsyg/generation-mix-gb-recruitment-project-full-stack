package com.msyg.energymix.service;

import com.msyg.energymix.model.external.ExternalApiResponse;
import com.msyg.energymix.model.external.GenerationData;
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
public class CarbonIntensityClient {

    private static final String API_BASE_URL = "https://api.carbonintensity.org.uk/generation";

    // Configured RestClient with User-Agent header to bypass API blocking on cloud servers
    private final RestClient restClient = RestClient.builder()
            .baseUrl(API_BASE_URL)
            .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .defaultHeader("Accept", "application/json")
            .build();

    public List<GenerationData> fetchForecastData() {
        //Calculate date range: from Now to +3 Days
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysForward = now.plusDays(3);

        //Format dates to match API requirements (ISO format)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'");
        String from = now.format(formatter);
        String to = threeDaysForward.format(formatter);

        //Construct RELATIVE dynamic URL (Base URL is already configured in the client)
        String urlPath = String.format("/%s/%s", from, to);

        try {
            //Execute HTTP GET request
            ExternalApiResponse response = restClient.get()
                    .uri(urlPath)
                    .retrieve()
                    .body(ExternalApiResponse.class);

            // Validate response
            if (response == null || response.getData() == null) return Collections.emptyList();

            return response.getData();

        } catch (Exception e) {
            // Log error and return empty list to avoid crashing the app
            System.out.println("API Error: " + e.getMessage());
            // Optional: Print stack trace only if needed for deep debugging
            //e.printStackTrace();
            return Collections.emptyList();
        }
    }
}