package com.relicloan.repository;

import com.relicloan.entity.HandoverRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HandoverRecordRepository extends JpaRepository<HandoverRecord, Long> {
    List<HandoverRecord> findByLoanApplicationId(Long loanApplicationId);
}
