package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionMonitoringRequest {
    private Long loanApplicationId;
    private String showcaseCondition;
    private String securityPlan;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Boolean conditionCompliant;
    private LocalDate monitoringDate;
    private String notes;
}
