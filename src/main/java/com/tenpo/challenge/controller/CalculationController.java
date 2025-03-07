package com.tenpo.challenge.controller;

import com.tenpo.challenge.model.CalculationRequest;
import com.tenpo.challenge.model.CalculationResponse;
import com.tenpo.challenge.service.CalculationService;
import com.tenpo.challenge.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/calculate")
public class CalculationController {

    private static final Logger log = LoggerFactory.getLogger(CalculationController.class);
    private final CalculationService calculationService;
    private final HistoryService historyService;
    private final ObjectMapper objectMapper;
    
    // Standard constructor-based dependency injection
    public CalculationController(CalculationService calculationService, 
                               HistoryService historyService, 
                               ObjectMapper objectMapper) {
        this.calculationService = calculationService;
        this.historyService = historyService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    @Operation(summary = "Calculate sum with percentage", description = "Adds two numbers and applies a percentage")
    @ApiResponse(responseCode = "200", description = "Calculation successful",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CalculationResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
        log.info("Received calculation request: {}", request);
        
        try {
            CalculationResponse response = calculationService.calculate(request);
            
            // Asynchronously save the request to history
            try {
                historyService.saveHistory(
                    "/api/calculate",
                    objectMapper.writeValueAsString(request),
                    objectMapper.writeValueAsString(response),
                    false,
                    null
                );
            } catch (JsonProcessingException e) {
                log.error("Failed to serialize request/response for history", e);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing calculation", e);
            
            // Save error to history
            try {
                historyService.saveHistory(
                    "/api/calculate",
                    objectMapper.writeValueAsString(request),
                    e.toString(),
                    true,
                    e.getMessage()
                );
            } catch (Exception ex) {
                log.error("Failed to save error to history", ex);
            }
            
            CalculationResponse errorResponse = new CalculationResponse();
            errorResponse.setResult(0);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
