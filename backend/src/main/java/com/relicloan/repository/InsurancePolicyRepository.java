package com.relicloan.repository;

import com.relicloan.entity.InsurancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {
    List<InsurancePolicy> findByLoanApplicationId(Long loanApplicationId);
}
