package com.msyg.energymix.model.external;

import lombok.Data;
import java.util.List;

/**
 * Root response object from the Carbon Intensity API.
 * The API wraps all results inside a "data" list
 */
@Data
public class ExternalApiResponse {
    private List<GenerationData> data;
}