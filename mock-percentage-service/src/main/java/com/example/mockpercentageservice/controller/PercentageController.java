package com.example.mockpercentageservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PercentageController {

    @GetMapping("/percentage")
    public Map<String, Double> getPercentage() {
        Map<String, Double> response = new HashMap<>();
        response.put("percentage", 10.0);
        return response;
    }
}
