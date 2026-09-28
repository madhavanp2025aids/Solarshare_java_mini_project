package com.example.solarshare.repository;

import com.example.solarshare.model.GenerationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GenerationLogRepository extends JpaRepository<GenerationLog, Long> {
    Optional<GenerationLog> findByInstallationIdAndDate(Long installationId, LocalDate date);
    List<GenerationLog> findByInstallationIdAndDateBetween(Long installationId, LocalDate startDate, LocalDate endDate);
}
