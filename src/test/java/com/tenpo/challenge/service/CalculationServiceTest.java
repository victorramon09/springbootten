package com.tenpo.challenge.service;

import com.tenpo.challenge.exception.ExternalServiceException;
import com.tenpo.challenge.model.CalculationRequest;
import com.tenpo.challenge.model.CalculationResponse;
import com.tenpo.challenge.service.impl.CalculationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class CalculationServiceTest {

    @InjectMocks
    private CalculationServiceImpl calculationService;

    @Mock
    private PercentageService percentageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateWithValidPercentage() throws ExternalServiceException {
        // Arrange
        double num1 = 100;
        double num2 = 200;
        double percentage = 10; // 10%
        double expectedResult = (num1 + num2) + ((num1 + num2) * percentage / 100); // 330

        when(percentageService.getPercentage()).thenReturn(percentage);

        CalculationRequest request = new CalculationRequest();
        request.setNum1(num1);
        request.setNum2(num2);

        // Act
        CalculationResponse response = calculationService.calculate(request);
        
        // Assert
        assertEquals(expectedResult, response.getResult(), 0.01, "Result should be sum plus percentage");
    }

    @Test
    public void testCalculateWithExternalServiceFailure() {
        // Arrange
        double num1 = 100;
        double num2 = 200;
        CalculationRequest request = new CalculationRequest();
        request.setNum1(num1);
        request.setNum2(num2);

        when(percentageService.getPercentage()).thenThrow(new ExternalServiceException("Service failed"));

        // Act & Assert
        assertThrows(ExternalServiceException.class, () -> {
            calculationService.calculate(request);
        }, "Should throw ExternalServiceException when percentage service fails");
    }
}