package com.tenpo.challenge.model;

import lombok.Data;

@Data
public class Percentage {
    private Double percentage;

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
