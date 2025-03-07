package com.tenpo.challenge.service.impl;

import com.tenpo.challenge.exception.ExternalServiceException;
import com.tenpo.challenge.model.CalculationRequest;
import com.tenpo.challenge.model.CalculationResponse;
import com.tenpo.challenge.service.CalculationService;
import com.tenpo.challenge.service.PercentageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link CalculationService} that calculates the result
 * by adding two numbers and applying a percentage obtained from {@link PercentageService}.
 */
@Service
public class CalculationServiceImpl implements CalculationService {

    private static final Logger log = LoggerFactory.getLogger(CalculationServiceImpl.class);
    private final PercentageService percentageService;
    
    // Standard constructor-based dependency injection
    public CalculationServiceImpl(PercentageService percentageService) {
        this.percentageService = percentageService;
    }

    @Override
    public CalculationResponse calculate(CalculationRequest request) {
        double num1 = request.getNum1();
        double num2 = request.getNum2();
        double sum = num1 + num2;
        
        double percentage;
        try {
            percentage = percentageService.getPercentage();
        } catch (ExternalServiceException e) {
            log.error("Failed to get percentage, no cached value available", e);
            throw e;
        }
        
        double percentageAmount = sum * (percentage / 100);
        double result = sum + percentageAmount;
        
        log.info("Calculation performed: {} + {} with {}% = {}", num1, num2, percentage, result);
        
        CalculationResponse response = new CalculationResponse();
        response.setResult(result);
        return response;
    }
}
