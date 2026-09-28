package com.example.solarshare.dto;

import java.math.BigDecimal;
import java.util.List;

public class MonthlySummary {
    private Long householdId;
    private int year;
    private int month;
    private BigDecimal totalGeneratedShare;
    private BigDecimal totalConsumed;
    private BigDecimal totalNetExported;
    private List<DailyUsage> dailyUsages;

    // Getters and setters
    public Long getHouseholdId() { return householdId; }
    public void setHouseholdId(Long householdId) { this.householdId = householdId; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public int getMonth() { return month; }
    public void setMonth(int month) { this.month = month; }
    public BigDecimal getTotalGeneratedShare() { return totalGeneratedShare; }
    public void setTotalGeneratedShare(BigDecimal totalGeneratedShare) { this.totalGeneratedShare = totalGeneratedShare; }
    public BigDecimal getTotalConsumed() { return totalConsumed; }
    public void setTotalConsumed(BigDecimal totalConsumed) { this.totalConsumed = totalConsumed; }
    public BigDecimal getTotalNetExported() { return totalNetExported; }
    public void setTotalNetExported(BigDecimal totalNetExported) { this.totalNetExported = totalNetExported; }
    public List<DailyUsage> getDailyUsages() { return dailyUsages; }
    public void setDailyUsages(List<DailyUsage> dailyUsages) { this.dailyUsages = dailyUsages; }
}
