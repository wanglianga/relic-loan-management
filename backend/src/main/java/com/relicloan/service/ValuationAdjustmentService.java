package com.relicloan.service;

import com.relicloan.dto.ValuationAdjustmentRequest;
import com.relicloan.dto.ValuationAdjustmentResponse;
import com.relicloan.entity.*;
import com.relicloan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValuationAdjustmentService {
    private final ValuationAdjustmentRepository valuationAdjustmentRepository;
    private final ArtifactRepository artifactRepository;
    private final InsurancePolicyRepository insurancePolicyRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public List<ValuationAdjustmentResponse> findAll() {
        return valuationAdjustmentRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ValuationAdjustmentResponse findById(Long id) {
        ValuationAdjustment adjustment = valuationAdjustmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Valuation adjustment not found: " + id));
        return toResponse(adjustment);
    }

    public List<ValuationAdjustmentResponse> findByArtifactId(Long artifactId) {
        return valuationAdjustmentRepository.findByArtifactIdOrderByCreatedAtDesc(artifactId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ValuationAdjustmentResponse> findByLoanApplicationId(Long loanApplicationId) {
        return valuationAdjustmentRepository.findByLoanApplicationIdOrderByCreatedAtDesc(loanApplicationId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ValuationAdjustmentResponse> findByInsurancePolicyId(Long insurancePolicyId) {
        return valuationAdjustmentRepository.findByInsurancePolicyIdOrderByCreatedAtDesc(insurancePolicyId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ValuationAdjustmentResponse create(ValuationAdjustmentRequest request) {
        Artifact artifact = artifactRepository.findById(request.getArtifactId())
                .orElseThrow(() -> new RuntimeException("Artifact not found: " + request.getArtifactId()));

        BigDecimal originalValue = request.getOriginalValue() != null ? request.getOriginalValue() : artifact.getAppraisedValue();
        BigDecimal newValue = request.getNewValue();
        boolean isValueIncrease = newValue.compareTo(originalValue) > 0;

        ValuationAdjustment adjustment = new ValuationAdjustment();
        adjustment.setArtifactId(request.getArtifactId());
        adjustment.setLoanApplicationId(request.getLoanApplicationId());
        adjustment.setInsurancePolicyId(request.getInsurancePolicyId());
        adjustment.setOriginalValue(originalValue);
        adjustment.setNewValue(newValue);
        adjustment.setAdjustmentReason(request.getAdjustmentReason());
        adjustment.setAppraisalInstitution(request.getAppraisalInstitution());
        adjustment.setInsuranceTermsChange(request.getInsuranceTermsChange());
        adjustment.setAdditionalPremium(request.getAdditionalPremium());
        adjustment.setContractImpact(request.getContractImpact());
        adjustment.setOriginalValuationBasis(request.getOriginalValuationBasis());
        adjustment.setIsValueIncrease(isValueIncrease);
        adjustment.setAdjustedBy(request.getAdjustedBy());

        ValuationAdjustment saved = valuationAdjustmentRepository.save(adjustment);

        artifact.setAppraisedValue(newValue);
        artifactRepository.save(artifact);

        if (request.getInsurancePolicyId() != null) {
            InsurancePolicy policy = insurancePolicyRepository.findById(request.getInsurancePolicyId())
                    .orElseThrow(() -> new RuntimeException("Insurance policy not found: " + request.getInsurancePolicyId()));
            
            String existingAdjustments = policy.getValuationAdjustments() != null ? policy.getValuationAdjustments() : "";
            String adjustmentLog = String.format("[%s] 估值调整: %s -> %s万元, 原因: %s",
                    saved.getCreatedAt(), originalValue, newValue, request.getAdjustmentReason());
            policy.setValuationAdjustments(existingAdjustments + (existingAdjustments.isEmpty() ? "" : "\n") + adjustmentLog);
            
            if (isValueIncrease && policy.getInsuredAmount().compareTo(newValue) < 0) {
                policy.setStatus(PolicyStatus.ADJUSTING);
            }
            
            insurancePolicyRepository.save(policy);
        }

        return toResponse(saved);
    }

    public boolean isInsuredAmountSufficient(Long loanApplicationId) {
        LoanApplication loan = loanApplicationRepository.findById(loanApplicationId)
                .orElseThrow(() -> new RuntimeException("Loan application not found: " + loanApplicationId));
        
        Artifact artifact = artifactRepository.findById(loan.getArtifactId())
                .orElseThrow(() -> new RuntimeException("Artifact not found: " + loan.getArtifactId()));

        List<InsurancePolicy> policies = insurancePolicyRepository.findByLoanApplicationId(loanApplicationId);
        for (InsurancePolicy policy : policies) {
            if (policy.getStatus() == PolicyStatus.ACTIVE || policy.getStatus() == PolicyStatus.ADJUSTING) {
                if (policy.getInsuredAmount().compareTo(artifact.getAppraisedValue()) >= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private ValuationAdjustmentResponse toResponse(ValuationAdjustment adjustment) {
        return new ValuationAdjustmentResponse(
                adjustment.getId(),
                adjustment.getArtifactId(),
                adjustment.getLoanApplicationId(),
                adjustment.getInsurancePolicyId(),
                adjustment.getOriginalValue(),
                adjustment.getNewValue(),
                adjustment.getAdjustmentReason(),
                adjustment.getAppraisalInstitution(),
                adjustment.getInsuranceTermsChange(),
                adjustment.getAdditionalPremium(),
                adjustment.getContractImpact(),
                adjustment.getOriginalValuationBasis(),
                adjustment.getIsValueIncrease(),
                adjustment.getAdjustedBy(),
                adjustment.getCreatedAt()
        );
    }
}
