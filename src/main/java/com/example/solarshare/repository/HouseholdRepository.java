package com.example.solarshare.repository;

import com.example.solarshare.model.Household;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseholdRepository extends JpaRepository<Household, Long> {
    List<Household> findByInstallationId(Long installationId);
}
