package com.tenpo.challenge.controller;

import com.tenpo.challenge.model.History;
import com.tenpo.challenge.service.HistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryController.class)
class HistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        // No need for MockitoAnnotations.openMocks() with @WebMvcTest
    }

    @Test
    void testGetHistory() throws Exception {
        // Arrange
        LocalDateTime now = LocalDateTime.now();
        History history1 = new History();
        history1.setId(1L);
        history1.setTimestamp(now);
        history1.setEndpoint("/api/calculate");
        history1.setParameters("{\"num1\":10,\"num2\":20}");
        history1.setResponse("{\"result\":33.0}");
        history1.setError(false);
        history1.setErrorMessage(null);

        History history2 = new History();
        history2.setId(2L);
        history2.setTimestamp(now.minusMinutes(5));
        history2.setEndpoint("/api/calculate");
        history2.setParameters("{\"num1\":5,\"num2\":15}");
        history2.setResponse("{\"result\":22.0}");
        history2.setError(false);
        history2.setErrorMessage(null);

        List<History> historyEntries = List.of(history1, history2);
        
        Page<History> historyPage = new PageImpl<>(historyEntries, 
                                           PageRequest.of(0, 10), 
                                           historyEntries.size());
        
        when(historyService.getHistory(any(Pageable.class))).thenReturn(historyPage);
now.getNano();
        // Act & Assert
        mockMvc.perform(get("/api/history")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))

                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].timestamp").value(now.withNano(now.getNano()).toString()))
                .andExpect(jsonPath("$.content[0].endpoint").value("/api/calculate"))
                .andExpect(jsonPath("$.content[0].parameters").value("{\"num1\":10,\"num2\":20}"))
                .andExpect(jsonPath("$.content[0].response").value("{\"result\":33.0}"))
                .andExpect(jsonPath("$.content[0].isError").value(false))

                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].timestamp").value(now.minusMinutes(5).withNano(now.minusMinutes(5).getNano()).toString()))
                .andExpect(jsonPath("$.content[1].endpoint").value("/api/calculate"))
                .andExpect(jsonPath("$.content[1].parameters").value("{\"num1\":5,\"num2\":15}"))
                .andExpect(jsonPath("$.content[1].response").value("{\"result\":22.0}"))//;
                .andExpect(jsonPath("$.content[1].isError").value(false));
    }

    @Test
    void testGetHistoryEmpty() throws Exception {
        // Arrange
        Page<History> emptyPage = new PageImpl<>(List.of(), 
                                        PageRequest.of(0, 10), 
                                        0);
        
        when(historyService.getHistory(any(Pageable.class))).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/api/history")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0));
    }
}