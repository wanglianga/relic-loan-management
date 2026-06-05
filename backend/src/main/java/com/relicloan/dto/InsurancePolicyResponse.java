package com.relicloan.dto;

import com.relicloan.entity.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsurancePolicyResponse {
    private Long id;
    private Long loanApplicationId;
    private String policyNumber;
    private BigDecimal insuredAmount;
    private BigDecimal deductible;
    private String terms;
    private String valuationAdjustments;
    private PolicyStatus status;
    private LocalDate validFrom;
    private LocalDate validTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
