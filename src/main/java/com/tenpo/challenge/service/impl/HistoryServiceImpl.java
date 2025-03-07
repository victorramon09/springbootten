package com.tenpo.challenge.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenpo.challenge.model.History;
import com.tenpo.challenge.repository.HistoryRepository;
import com.tenpo.challenge.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger log = LoggerFactory.getLogger(HistoryServiceImpl.class);
    private final HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    @Async
    public void saveHistory(String endpoint, String parameters, String response, boolean isError, String errorMessage) {
        try {
            History history = new History();
            history.setEndpoint(endpoint);
            history.setParameters(parameters);
            history.setResponse(response);
            history.setError(isError);
            history.setErrorMessage(errorMessage);
            history.setTimestamp(LocalDateTime.now());
            
            historyRepository.save(history);
            log.info("History saved for endpoint: {}", endpoint);
        } catch (Exception e) {
            log.error("Failed to save API call history", e);
        }
    }
    
    @Override
    public Page<History> getHistory(Pageable pageable) {
        Page<History> historyEntries = historyRepository.findAll(pageable);
        return historyEntries;
    }
}
