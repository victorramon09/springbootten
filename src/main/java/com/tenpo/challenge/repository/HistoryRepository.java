package com.tenpo.challenge.repository;

import com.tenpo.challenge.model.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    @NonNull
    Page<History> findAll(@NonNull Pageable pageable);
}
