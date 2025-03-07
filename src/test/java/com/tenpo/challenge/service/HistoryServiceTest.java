package com.tenpo.challenge.service;

import com.tenpo.challenge.model.History;
import com.tenpo.challenge.repository.HistoryRepository;
import com.tenpo.challenge.service.impl.HistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HistoryServiceTest {

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private HistoryRepository historyRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveHistory() {
        // Arrange
        History history = new History();
        history.setTimestamp(LocalDateTime.now());
        history.setEndpoint("/api/test");
        history.setParameters("{\"param\":\"value\"}");
        history.setResponse("{\"result\":\"success\"}");
        history.setError(false);
        history.setErrorMessage(null);


        when(historyRepository.save(any(History.class))).thenReturn(history);

        // Act
        historyService.saveHistory("/api/test", "{\"param\":\"value\"}", "{\"result\":\"success\"}", false, null);

        // Assert
        verify(historyRepository, times(1)).save(any(History.class));
    }

    @Test
    public void testGetHistory() {
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

        Page<History> historyPage = new PageImpl<>(historyEntries, PageRequest.of(0, 10), historyEntries.size());
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(historyPage);

        // Act
        Page<History> result = historyService.getHistory(PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertEquals(historyEntries, result.getContent());
    }

    @Test
    public void testGetHistoryWithEmptyResult() {

        Page<History> emptyHistoryPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(historyRepository.findAll(any(Pageable.class))).thenReturn(emptyHistoryPage);


        Page<History> result = historyService.getHistory(PageRequest.of(0, 10));


        assertEquals(0, result.getTotalElements());
        assertEquals(List.of(), result.getContent());
    }
}
