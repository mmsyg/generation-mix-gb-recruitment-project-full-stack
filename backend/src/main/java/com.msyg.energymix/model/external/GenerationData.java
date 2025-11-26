package com.msyg.energymix.model.external;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Represents a specific time interval (30 minutes) containing generation data
 * Matches an element inside the "data" array from the API
 */
@Data
public class GenerationData {
    private String from;    // Start time of the interval (ISO 8601 format)
    private String to;   // End time of the interval

    // @JsonProperty because the API returns "generationmix" in lowercase
    @JsonProperty("generationmix")
    private List<GenerationItem> generationMix;
}