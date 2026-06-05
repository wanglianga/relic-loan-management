package com.relicloan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "valuation_adjustments")
public class ValuationAdjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long artifactId;

    private Long loanApplicationId;

    private Long insurancePolicyId;

    @Column(nullable = false)
    private BigDecimal originalValue;

    @Column(nullable = false)
    private BigDecimal newValue;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String adjustmentReason;

    private String appraisalInstitution;

    @Column(columnDefinition = "TEXT")
    private String insuranceTermsChange;

    private BigDecimal additionalPremium;

    @Column(columnDefinition = "TEXT")
    private String contractImpact;

    @Column(columnDefinition = "TEXT")
    private String originalValuationBasis;

    @Column(nullable = false)
    private Boolean isValueIncrease;

    private String adjustedBy;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isValueIncrease == null) {
            isValueIncrease = newValue.compareTo(originalValue) > 0;
        }
    }
}
