package com.tenpo.challenge.model;

import lombok.Data;

@Data
public class CalculationResponse {
    private double result;

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
