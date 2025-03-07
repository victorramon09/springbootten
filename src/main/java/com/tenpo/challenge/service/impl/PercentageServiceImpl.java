package com.tenpo.challenge.service.impl;

import com.tenpo.challenge.exception.ExternalServiceException;
import com.tenpo.challenge.model.Percentage;
import com.tenpo.challenge.service.PercentageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
public class PercentageServiceImpl implements PercentageService {

    private static final Logger log = LoggerFactory.getLogger(PercentageServiceImpl.class);

    private final RestTemplate restTemplate;
    private Double cachedPercentage;
    private LocalDateTime lastFetchTime;
    private static final int CACHE_MINUTES = 30;

    @Value("${percentage.service.url}")
    private String percentageServiceUrl;

    public PercentageServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    // @Cacheable("percentageCache")
    public Double getPercentage() {
        try {
            log.info("Fetching percentage from external service: {}", percentageServiceUrl);
            Percentage response = restTemplate.getForObject(
                percentageServiceUrl,
                Percentage.class
            );
            log.info("Fetched percentage response: {}", response);

            if (response == null || response.getPercentage() == null) {
                throw new ExternalServiceException("Invalid response from external service");
            }

            Double percentage = response.getPercentage();

            // Update cache
            cachedPercentage = percentage;
            lastFetchTime = LocalDateTime.now();

            log.info("Storing cached percentage: {}", cachedPercentage);
            return percentage;
        } catch (Exception e) {
            if (isCacheValid()) {
                log.info("Returning cached percentage: {}", cachedPercentage);
                return cachedPercentage;
            } else {
                throw new ExternalServiceException("Failed to get percentage from external service", e);
            }
        }
    }

    private boolean isCacheValid() {
        return cachedPercentage != null &&
                lastFetchTime != null &&
                lastFetchTime.plusMinutes(CACHE_MINUTES).isAfter(LocalDateTime.now());
    }

    // Setter for testing purposes
    public void setPercentageServiceUrl(String percentageServiceUrl) {
        this.percentageServiceUrl = percentageServiceUrl;
    }
}
