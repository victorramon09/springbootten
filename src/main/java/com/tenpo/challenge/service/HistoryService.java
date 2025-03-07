package com.tenpo.challenge.service;

import com.tenpo.challenge.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing API call history
 */
public interface HistoryService {
    /**
     * Saves API call details to history
     *
     * @param endpoint the API endpoint called
     * @param parameters the request parameters as string
     * @param response the response body as string
     * @param isError whether the call resulted in an error
     * @param errorMessage details of the error if any
     */
    void saveHistory(String endpoint, String parameters, String response, boolean isError, String errorMessage);
    
    /**
     * Get paginated history of API calls
     * 
     * @param pageable Pagination information
     * @return Page of history entries
     */
    Page<History> getHistory(Pageable pageable);
}