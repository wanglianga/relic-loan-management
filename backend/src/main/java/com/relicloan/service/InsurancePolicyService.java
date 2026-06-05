package com.relicloan.service;

import com.relicloan.dto.InsurancePolicyRequest;
import com.relicloan.dto.InsurancePolicyResponse;
import com.relicloan.entity.InsurancePolicy;
import com.relicloan.entity.PolicyStatus;
import com.relicloan.repository.InsurancePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsurancePolicyService {
    private final InsurancePolicyRepository insurancePolicyRepository;

    public List<InsurancePolicyResponse> findAll() {
        return insurancePolicyRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InsurancePolicyResponse findById(Long id) {
        InsurancePolicy policy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InsurancePolicy not found: " + id));
        return toResponse(policy);
    }

    public List<InsurancePolicyResponse> findByLoanApplicationId(Long loanApplicationId) {
        return insurancePolicyRepository.findByLoanApplicationId(loanApplicationId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public InsurancePolicyResponse create(InsurancePolicyRequest request) {
        InsurancePolicy policy = new InsurancePolicy();
        applyRequest(policy, request);
        return toResponse(insurancePolicyRepository.save(policy));
    }

    public InsurancePolicyResponse update(Long id, InsurancePolicyRequest request) {
        InsurancePolicy policy = insurancePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("InsurancePolicy not found: " + id));
        applyRequest(policy, request);
        return toResponse(insurancePolicyRepository.save(policy));
    }

    public void triggerAdjusting(Long loanApplicationId, String reason) {
        List<InsurancePolicy> policies = insurancePolicyRepository.findByLoanApplicationId(loanApplicationId);
        for (InsurancePolicy policy : policies) {
            if (policy.getStatus() == PolicyStatus.ACTIVE || policy.getStatus() == PolicyStatus.DRAFT) {
                policy.setStatus(PolicyStatus.ADJUSTING);
                String existing = policy.getValuationAdjustments() != null ? policy.getValuationAdjustments() : "";
                policy.setValuationAdjustments(existing + (existing.isEmpty() ? "" : "\n") + reason);
                insurancePolicyRepository.save(policy);
            }
        }
    }

    public void renewPolicy(Long loanApplicationId, LocalDate newValidTo) {
        List<InsurancePolicy> policies = insurancePolicyRepository.findByLoanApplicationId(loanApplicationId);
        for (InsurancePolicy policy : policies) {
            if (policy.getStatus() == PolicyStatus.ACTIVE || policy.getStatus() == PolicyStatus.ADJUSTING) {
                policy.setValidTo(newValidTo);
                policy.setStatus(PolicyStatus.ACTIVE);
                String existing = policy.getValuationAdjustments() != null ? policy.getValuationAdjustments() : "";
                policy.setValuationAdjustments(existing + (existing.isEmpty() ? "" : "\n") + "Policy renewed, new validTo: " + newValidTo);
                insurancePolicyRepository.save(policy);
            }
        }
    }

    private void applyRequest(InsurancePolicy policy, InsurancePolicyRequest request) {
        policy.setLoanApplicationId(request.getLoanApplicationId());
        policy.setPolicyNumber(request.getPolicyNumber());
        policy.setInsuredAmount(request.getInsuredAmount());
        policy.setDeductible(request.getDeductible());
        policy.setTerms(request.getTerms());
        policy.setValuationAdjustments(request.getValuationAdjustments());
        policy.setValidFrom(request.getValidFrom());
        policy.setValidTo(request.getValidTo());
    }

    private InsurancePolicyResponse toResponse(InsurancePolicy policy) {
        return new InsurancePolicyResponse(
                policy.getId(),
                policy.getLoanApplicationId(),
                policy.getPolicyNumber(),
                policy.getInsuredAmount(),
                policy.getDeductible(),
                policy.getTerms(),
                policy.getValuationAdjustments(),
                policy.getStatus(),
                policy.getValidFrom(),
                policy.getValidTo(),
                policy.getCreatedAt(),
                policy.getUpdatedAt()
        );
    }
}
