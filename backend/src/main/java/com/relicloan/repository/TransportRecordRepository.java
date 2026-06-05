package com.relicloan.repository;

import com.relicloan.entity.TransportRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportRecordRepository extends JpaRepository<TransportRecord, Long> {
    List<TransportRecord> findByLoanApplicationId(Long loanApplicationId);
}
