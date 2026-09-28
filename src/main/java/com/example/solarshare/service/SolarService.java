package com.example.solarshare.service;

import com.example.solarshare.dto.DailyUsage;
import com.example.solarshare.dto.MonthlySummary;
import com.example.solarshare.model.ConsumptionLog;
import com.example.solarshare.model.GenerationLog;
import com.example.solarshare.model.Household;
import com.example.solarshare.model.Installation;
import com.example.solarshare.repository.ConsumptionLogRepository;
import com.example.solarshare.repository.GenerationLogRepository;
import com.example.solarshare.repository.HouseholdRepository;
import com.example.solarshare.repository.InstallationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolarService {

    @Autowired
    private InstallationRepository installationRepository;
    
    @Autowired
    private HouseholdRepository householdRepository;
    
    @Autowired
    private GenerationLogRepository generationLogRepository;
    
    @Autowired
    private ConsumptionLogRepository consumptionLogRepository;

    @Transactional
    public Installation createInstallation(Installation installation) {
        return installationRepository.save(installation);
    }

    @Transactional
    public Household addHousehold(Long installationId, Household household) {
        Installation installation = installationRepository.findById(installationId)
                .orElseThrow(() -> new IllegalArgumentException("Installation not found"));
        
        List<Household> existingHouseholds = householdRepository.findByInstallationId(installationId);
        BigDecimal currentTotalRatio = existingHouseholds.stream()
                .map(Household::getAllocationRatio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        if (currentTotalRatio.add(household.getAllocationRatio()).compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Total allocated shares across households must not exceed the total units generated for that day. (Sum of allocation ratios cannot exceed 1.0)");
        }
        
        household.setInstallation(installation);
        System.out.println("Notification: Household " + household.getName() + " added with allocation ratio " + household.getAllocationRatio());
        return householdRepository.save(household);
    }

    @Transactional
    public GenerationLog logGeneration(Long installationId, GenerationLog log) {
        Installation installation = installationRepository.findById(installationId)
                .orElseThrow(() -> new IllegalArgumentException("Installation not found"));
                
        // Optional: Ensure we don't log multiple times per day.
        Optional<GenerationLog> existing = generationLogRepository.findByInstallationIdAndDate(installationId, log.getDate());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Generation log for this date already exists");
        }

        log.setInstallation(installation);
        System.out.println("Notification: Solar generation of " + log.getUnitsGenerated() + " units logged for " + log.getDate());
        return generationLogRepository.save(log);
    }

    @Transactional
    public ConsumptionLog logConsumption(Long householdId, ConsumptionLog log) {
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));
                
        Optional<ConsumptionLog> existing = consumptionLogRepository.findByHouseholdIdAndDate(householdId, log.getDate());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Consumption log for this date already exists");
        }

        log.setHousehold(household);
        System.out.println("Notification: Consumption of " + log.getUnitsConsumed() + " units logged for household " + household.getName() + " on " + log.getDate());
        return consumptionLogRepository.save(log);
    }

    public MonthlySummary getMonthlySummary(Long householdId, int year, int month) {
        Household household = householdRepository.findById(householdId)
                .orElseThrow(() -> new IllegalArgumentException("Household not found"));
        
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        
        List<GenerationLog> generationLogs = generationLogRepository
                .findByInstallationIdAndDateBetween(household.getInstallation().getId(), startDate, endDate);
        
        List<ConsumptionLog> consumptionLogs = consumptionLogRepository
                .findByHouseholdIdAndDateBetween(householdId, startDate, endDate);
                
        Map<LocalDate, BigDecimal> genMap = generationLogs.stream()
                .collect(Collectors.toMap(GenerationLog::getDate, GenerationLog::getUnitsGenerated));
                
        Map<LocalDate, BigDecimal> consMap = consumptionLogs.stream()
                .collect(Collectors.toMap(ConsumptionLog::getDate, ConsumptionLog::getUnitsConsumed));
                
        MonthlySummary summary = new MonthlySummary();
        summary.setHouseholdId(householdId);
        summary.setYear(year);
        summary.setMonth(month);
        
        List<DailyUsage> dailyUsages = new ArrayList<>();
        BigDecimal totalGeneratedShare = BigDecimal.ZERO;
        BigDecimal totalConsumed = BigDecimal.ZERO;
        BigDecimal totalExported = BigDecimal.ZERO;
        
        for (int day = 1; day <= startDate.lengthOfMonth(); day++) {
            LocalDate date = startDate.withDayOfMonth(day);
            BigDecimal dailyGen = genMap.getOrDefault(date, BigDecimal.ZERO);
            BigDecimal dailyCons = consMap.getOrDefault(date, BigDecimal.ZERO);
            
            BigDecimal generatedShare = dailyGen.multiply(household.getAllocationRatio());
            BigDecimal netExported = generatedShare.subtract(dailyCons);
            if (netExported.compareTo(BigDecimal.ZERO) < 0) {
                netExported = BigDecimal.ZERO; // Floored at zero
            }
            
            if (dailyGen.compareTo(BigDecimal.ZERO) > 0 || dailyCons.compareTo(BigDecimal.ZERO) > 0) {
                dailyUsages.add(new DailyUsage(day, generatedShare, dailyCons, netExported));
                totalGeneratedShare = totalGeneratedShare.add(generatedShare);
                totalConsumed = totalConsumed.add(dailyCons);
                totalExported = totalExported.add(netExported);
            }
        }
        
        summary.setDailyUsages(dailyUsages);
        summary.setTotalGeneratedShare(totalGeneratedShare);
        summary.setTotalConsumed(totalConsumed);
        summary.setTotalNetExported(totalExported);
        
        return summary;
    }
}
