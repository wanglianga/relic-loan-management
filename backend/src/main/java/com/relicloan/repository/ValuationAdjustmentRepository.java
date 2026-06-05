package com.relicloan.repository;

import com.relicloan.entity.ValuationAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValuationAdjustmentRepository extends JpaRepository<ValuationAdjustment, Long> {
    List<ValuationAdjustment> findByArtifactIdOrderByCreatedAtDesc(Long artifactId);
    List<ValuationAdjustment> findByLoanApplicationIdOrderByCreatedAtDesc(Long loanApplicationId);
    List<ValuationAdjustment> findByInsurancePolicyIdOrderByCreatedAtDesc(Long insurancePolicyId);
}
