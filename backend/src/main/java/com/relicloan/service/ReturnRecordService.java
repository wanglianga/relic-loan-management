package com.relicloan.service;

import com.relicloan.dto.ReturnRecordRequest;
import com.relicloan.dto.ReturnRecordResponse;
import com.relicloan.entity.ReturnRecord;
import com.relicloan.repository.ReturnRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReturnRecordService {
    private final ReturnRecordRepository returnRecordRepository;
    private final LoanApplicationService loanApplicationService;
    private final InsurancePolicyService insurancePolicyService;

    public List<ReturnRecordResponse> findAll() {
        return returnRecordRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ReturnRecordResponse findById(Long id) {
        ReturnRecord record = returnRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ReturnRecord not found: " + id));
        return toResponse(record);
    }

    @Transactional
    public ReturnRecordResponse create(ReturnRecordRequest request) {
        ReturnRecord record = new ReturnRecord();
        applyRequest(record, request);
        ReturnRecord saved = returnRecordRepository.save(record);

        if (Boolean.TRUE.equals(request.getIsExtension())) {
            loanApplicationService.markOverdue(request.getLoanApplicationId());
            LocalDate newValidTo = request.getReturnDate() != null
                    ? request.getReturnDate().plusMonths(3)
                    : LocalDate.now().plusMonths(3);
            insurancePolicyService.renewPolicy(request.getLoanApplicationId(), newValidTo);
        }

        return toResponse(saved);
    }

    private void applyRequest(ReturnRecord record, ReturnRecordRequest request) {
        record.setLoanApplicationId(request.getLoanApplicationId());
        record.setReturnCondition(request.getReturnCondition());
        record.setReturnedTo(request.getReturnedTo());
        record.setReturnDate(request.getReturnDate());
        record.setIsExtension(request.getIsExtension());
        record.setExtensionReason(request.getExtensionReason());
    }

    private ReturnRecordResponse toResponse(ReturnRecord record) {
        return new ReturnRecordResponse(
                record.getId(),
                record.getLoanApplicationId(),
                record.getReturnCondition(),
                record.getReturnedTo(),
                record.getReturnDate(),
                record.getIsExtension(),
                record.getExtensionReason(),
                record.getCreatedAt()
        );
    }
}
