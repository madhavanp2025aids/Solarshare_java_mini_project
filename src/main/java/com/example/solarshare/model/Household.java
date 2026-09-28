package com.example.solarshare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Household {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Allocation ratio is required")
    @Positive(message = "Allocation ratio must be positive")
    private BigDecimal allocationRatio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "installation_id", nullable = false)
    @JsonIgnore
    private Installation installation;

    public Household() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAllocationRatio() {
        return allocationRatio;
    }

    public void setAllocationRatio(BigDecimal allocationRatio) {
        this.allocationRatio = allocationRatio;
    }

    public Installation getInstallation() {
        return installation;
    }

    public void setInstallation(Installation installation) {
        this.installation = installation;
    }
}
