package com.tenpo.challenge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.tenpo.challenge.model.Percentage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.tenpo.challenge.exception.ExternalServiceException;
import com.tenpo.challenge.service.impl.PercentageServiceImpl;

import java.util.concurrent.TimeUnit;

public class PercentageServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PercentageServiceImpl percentageServiceImpl;

    private final double mockPercentage = 10.0;
    
    @Value("${percentage.service.url}")
    private String percentageServiceUrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        percentageServiceImpl = new PercentageServiceImpl(restTemplate);
        percentageServiceImpl.setPercentageServiceUrl(percentageServiceUrl);
    }

    @Test
    public void testGetPercentageFromService() {
        Percentage mockResponse = new Percentage();
        mockResponse.setPercentage(mockPercentage);
        when(restTemplate.getForObject(percentageServiceUrl, Percentage.class)).thenReturn(mockResponse);

        double result = percentageServiceImpl.getPercentage();
        assertEquals(mockPercentage, result);
    }

    @Test
    public void testGetCachedPercentage() throws InterruptedException {
        Percentage mockResponse = new Percentage();
        mockResponse.setPercentage(mockPercentage);
        when(restTemplate.getForObject(percentageServiceUrl, Percentage.class)).thenReturn(mockResponse);
        
        percentageServiceImpl.getPercentage(); // Cache the value
        
        TimeUnit.SECONDS.sleep(5);
        when(restTemplate.getForObject(percentageServiceUrl, Percentage.class)).thenThrow(new ExternalServiceException("Failed to get percentage from external service"));

        double result = percentageServiceImpl.getPercentage();
        assertEquals(mockPercentage, result);
    }

    @Test
    public void testHandleExternalServiceFailure() {
        when(restTemplate.getForObject(percentageServiceUrl, Percentage.class)).thenThrow(new ExternalServiceException("Service failed"));

        assertThrows(ExternalServiceException.class, () -> {
            percentageServiceImpl.getPercentage();
        });
    }
}