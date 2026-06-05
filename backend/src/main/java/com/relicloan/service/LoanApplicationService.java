package com.relicloan.service;

import com.relicloan.dto.LoanApplicationRequest;
import com.relicloan.dto.LoanApplicationResponse;
import com.relicloan.dto.LoanStatusUpdateRequest;
import com.relicloan.entity.LoanApplication;
import com.relicloan.entity.LoanStatus;
import com.relicloan.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoanApplicationService {
    private final LoanApplicationRepository loanApplicationRepository;

    private static final Map<LoanStatus, List<LoanStatus>> VALID_TRANSITIONS = Map.of(
            LoanStatus.APPLIED, List.of(LoanStatus.INSURANCE_CONFIRMED, LoanStatus.SUSPENDED),
            LoanStatus.INSURANCE_CONFIRMED, List.of(LoanStatus.PACKED, LoanStatus.SUSPENDED),
            LoanStatus.PACKED, List.of(LoanStatus.HANDED_OVER, LoanStatus.SUSPENDED),
            LoanStatus.HANDED_OVER, List.of(LoanStatus.EXHIBITING, LoanStatus.SUSPENDED),
            LoanStatus.EXHIBITING, List.of(LoanStatus.RETURNED, LoanStatus.SUSPENDED, LoanStatus.OVERDUE),
            LoanStatus.SUSPENDED, List.of(LoanStatus.INSURANCE_CONFIRMED, LoanStatus.EXHIBITING, LoanStatus.RETURNED),
            LoanStatus.OVERDUE, List.of(LoanStatus.RETURNED, LoanStatus.SUSPENDED),
            LoanStatus.RETURNED, List.of()
    );

    public List<LoanApplicationResponse> findAll() {
        return loanApplicationRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public LoanApplicationResponse findById(Long id) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanApplication not found: " + id));
        return toResponse(loan);
    }

    public LoanApplicationResponse create(LoanApplicationRequest request) {
        LoanApplication loan = new LoanApplication();
        applyRequest(loan, request);
        return toResponse(loanApplicationRepository.save(loan));
    }

    public LoanApplicationResponse update(Long id, LoanApplicationRequest request) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanApplication not found: " + id));
        applyRequest(loan, request);
        return toResponse(loanApplicationRepository.save(loan));
    }

    public LoanApplicationResponse updateStatus(Long id, LoanStatusUpdateRequest request) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanApplication not found: " + id));
        LoanStatus current = loan.getStatus();
        LoanStatus target = request.getStatus();
        List<LoanStatus> allowed = VALID_TRANSITIONS.getOrDefault(current, List.of());
        if (!allowed.contains(target)) {
            throw new RuntimeException(
                    "Invalid status transition from " + current + " to " + target);
        }
        loan.setStatus(target);
        return toResponse(loanApplicationRepository.save(loan));
    }

    public void suspendLoan(Long id) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanApplication not found: " + id));
        loan.setStatus(LoanStatus.SUSPENDED);
        loanApplicationRepository.save(loan);
    }

    public void markOverdue(Long id) {
        LoanApplication loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LoanApplication not found: " + id));
        loan.setStatus(LoanStatus.OVERDUE);
        loanApplicationRepository.save(loan);
    }

    private void applyRequest(LoanApplication loan, LoanApplicationRequest request) {
        loan.setArtifactId(request.getArtifactId());
        loan.setApplicantMuseum(request.getApplicantMuseum());
        loan.setBorrowingVenue(request.getBorrowingVenue());
        loan.setStartDate(request.getStartDate());
        loan.setEndDate(request.getEndDate());
        loan.setContractNumber(request.getContractNumber());
        loan.setSpecialRequirements(request.getSpecialRequirements());
    }

    private LoanApplicationResponse toResponse(LoanApplication loan) {
        return new LoanApplicationResponse(
                loan.getId(),
                loan.getArtifactId(),
                loan.getApplicantMuseum(),
                loan.getBorrowingVenue(),
                loan.getStartDate(),
                loan.getEndDate(),
                loan.getStatus(),
                loan.getContractNumber(),
                loan.getSpecialRequirements(),
                loan.getCreatedAt(),
                loan.getUpdatedAt()
        );
    }
}
