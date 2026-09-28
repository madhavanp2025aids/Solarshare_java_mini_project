package com.example.solarshare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class GenerationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Units generated is required")
    @PositiveOrZero(message = "Units generated must be zero or positive")
    private BigDecimal unitsGenerated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installation_id", nullable = false)
    @JsonIgnore
    private Installation installation;

    public GenerationLog() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getUnitsGenerated() {
        return unitsGenerated;
    }

    public void setUnitsGenerated(BigDecimal unitsGenerated) {
        this.unitsGenerated = unitsGenerated;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }
}
