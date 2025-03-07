package com.tenpo.challenge.service;

import com.tenpo.challenge.model.CalculationRequest;
import com.tenpo.challenge.model.CalculationResponse;

public interface CalculationService {
    CalculationResponse calculate(CalculationRequest request);
}
