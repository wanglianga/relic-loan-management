package com.relicloan.repository;

import com.relicloan.entity.ReturnRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReturnRecordRepository extends JpaRepository<ReturnRecord, Long> {
    List<ReturnRecord> findByLoanApplicationId(Long loanApplicationId);
}
