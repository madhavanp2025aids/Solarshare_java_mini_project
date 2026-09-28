package com.example.solarshare.repository;

import com.example.solarshare.model.ConsumptionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ConsumptionLogRepository extends JpaRepository<ConsumptionLog, Long> {
    Optional<ConsumptionLog> findByHouseholdIdAndDate(Long householdId, LocalDate date);
    List<ConsumptionLog> findByHouseholdIdAndDateBetween(Long householdId, LocalDate startDate, LocalDate endDate);
}
