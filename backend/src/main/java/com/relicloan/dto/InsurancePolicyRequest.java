package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicyRequest {
    private Long loanApplicationId;
    private String policyNumber;
    private BigDecimal insuredAmount;
    private BigDecimal deductible;
    private String terms;
    private String valuationAdjustments;
    private LocalDate validFrom;
    private LocalDate validTo;
}
