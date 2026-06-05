package com.relicloan.repository;

import com.relicloan.entity.ExhibitionMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionMonitoringRepository extends JpaRepository<ExhibitionMonitoring, Long> {
    List<ExhibitionMonitoring> findByLoanApplicationId(Long loanApplicationId);
}
