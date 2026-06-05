package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValuationAdjustmentRequest {
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
    private String adjustedBy;
}
