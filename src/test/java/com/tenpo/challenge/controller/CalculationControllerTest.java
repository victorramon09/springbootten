package com.tenpo.challenge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.model.CalculationRequest;
import com.tenpo.challenge.model.CalculationResponse;
import com.tenpo.challenge.service.CalculationService;
import com.tenpo.challenge.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CalculationController.class)
public class CalculationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CalculationService calculationService;

    @MockBean
    private HistoryService historyService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        doNothing().when(historyService).saveHistory(anyString(), anyString(), anyString(), any(Boolean.class), anyString());
    }

    @Test
    public void testCalculateWithValidInput() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setNum1(10.0);
        request.setNum2(20.0);
        
        CalculationResponse response = new CalculationResponse();
        response.setResult(39.0); // Assuming a fixed percentage of 30%

        when(calculationService.calculate(any(CalculationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(39.0));
    }

    @Test
    public void testCalculateWithExternalServiceFailure() throws Exception {
        CalculationRequest request = new CalculationRequest();
        request.setNum1(10.0);
        request.setNum2(20.0);

        when(calculationService.calculate(any(CalculationRequest.class))).thenThrow(new RuntimeException("External service failed"));

        mockMvc.perform(post("/api/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());
    }
}