package com.tenpo.challenge.service;

import com.tenpo.challenge.exception.ExternalServiceException;

/**
 * Service for retrieving and caching percentage values from an external source
 */
public interface PercentageService {

    /**
     * Gets the percentage from the external service or cache.
     * If service fails and no cached value is available, throws ExternalServiceException.
     * 
     * @return The percentage to apply
     * @throws ExternalServiceException when the service is unavailable and no cached value exists
     */
    Double getPercentage();
}