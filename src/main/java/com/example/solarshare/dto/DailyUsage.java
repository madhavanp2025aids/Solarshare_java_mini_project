package com.example.solarshare.dto;

import java.math.BigDecimal;

public class DailyUsage {
    private int dayOfMonth;
    private BigDecimal generatedShare;
    private BigDecimal consumed;
    private BigDecimal netExported;

    public DailyUsage(int dayOfMonth, BigDecimal generatedShare, BigDecimal consumed, BigDecimal netExported) {
        this.dayOfMonth = dayOfMonth;
        this.generatedShare = generatedShare;
        this.consumed = consumed;
        this.netExported = netExported;
    }

    // Getters and setters
    public int getDayOfMonth() { return dayOfMonth; }
    public void setDayOfMonth(int dayOfMonth) { this.dayOfMonth = dayOfMonth; }
    public BigDecimal getGeneratedShare() { return generatedShare; }
    public void setGeneratedShare(BigDecimal generatedShare) { this.generatedShare = generatedShare; }
    public BigDecimal getConsumed() { return consumed; }
    public void setConsumed(BigDecimal consumed) { this.consumed = consumed; }
    public BigDecimal getNetExported() { return netExported; }
    public void setNetExported(BigDecimal netExported) { this.netExported = netExported; }
}
