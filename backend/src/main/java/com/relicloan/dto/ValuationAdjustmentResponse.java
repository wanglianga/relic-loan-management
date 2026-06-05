package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValuationAdjustmentResponse {
    private Long id;
    private Long artifactId;
    private Long loanApplicationId;
    private Long insurancePolicyId;
    private BigDecimal originalValue;
    private BigDecimal newValue;
    private String adjustmentReason;
    private String appraisalInstitution;
    private String insuranceTermsChange;
    private BigDecimal additionalPremium;
    private String contractImpact;
    private String originalValuationBasis;
    private Boolean isValueIncrease;
    private String adjustedBy;
    private LocalDateTime createdAt;
}
