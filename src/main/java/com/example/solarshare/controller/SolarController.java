package com.example.solarshare.controller;

import com.example.solarshare.dto.MonthlySummary;
import com.example.solarshare.model.ConsumptionLog;
import com.example.solarshare.model.GenerationLog;
import com.example.solarshare.model.Household;
import com.example.solarshare.model.Installation;
import com.example.solarshare.service.SolarService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class SolarController {

    @Autowired
    private SolarService solarService;

    @PostMapping("/installations")
    public ResponseEntity<Installation> createInstallation(@Valid @RequestBody Installation installation) {
        return new ResponseEntity<>(solarService.createInstallation(installation), HttpStatus.CREATED);
    }

    @PostMapping("/installations/{installationId}/households")
    public ResponseEntity<Household> addHousehold(@PathVariable Long installationId, 
                                                  @Valid @RequestBody Household household) {
        return new ResponseEntity<>(solarService.addHousehold(installationId, household), HttpStatus.CREATED);
    }

    @PostMapping("/installations/{installationId}/generation")
    public ResponseEntity<GenerationLog> logGeneration(@PathVariable Long installationId, 
                                                       @Valid @RequestBody GenerationLog log) {
        return new ResponseEntity<>(solarService.logGeneration(installationId, log), HttpStatus.CREATED);
    }

    @PostMapping("/households/{householdId}/consumption")
    public ResponseEntity<ConsumptionLog> logConsumption(@PathVariable Long householdId, 
                                                         @Valid @RequestBody ConsumptionLog log) {
        return new ResponseEntity<>(solarService.logConsumption(householdId, log), HttpStatus.CREATED);
    }

    @GetMapping("/households/{householdId}/summary")
    public ResponseEntity<MonthlySummary> getMonthlySummary(@PathVariable Long householdId, 
                                                            @RequestParam int year, 
                                                            @RequestParam int month) {
        return new ResponseEntity<>(solarService.getMonthlySummary(householdId, year, month), HttpStatus.OK);
    }
}
