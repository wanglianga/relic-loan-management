package com.relicloan.service;

import com.relicloan.dto.ExhibitionMonitoringRequest;
import com.relicloan.dto.ExhibitionMonitoringResponse;
import com.relicloan.entity.ExhibitionMonitoring;
import com.relicloan.repository.ExhibitionMonitoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitionMonitoringService {
    private final ExhibitionMonitoringRepository exhibitionMonitoringRepository;
    private final LoanApplicationService loanApplicationService;

    public List<ExhibitionMonitoringResponse> findAll() {
        return exhibitionMonitoringRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ExhibitionMonitoringResponse findById(Long id) {
        ExhibitionMonitoring monitoring = exhibitionMonitoringRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExhibitionMonitoring not found: " + id));
        return toResponse(monitoring);
    }

    @Transactional
    public ExhibitionMonitoringResponse create(ExhibitionMonitoringRequest request) {
        ExhibitionMonitoring monitoring = new ExhibitionMonitoring();
        applyRequest(monitoring, request);
        ExhibitionMonitoring saved = exhibitionMonitoringRepository.save(monitoring);

        if (Boolean.FALSE.equals(request.getConditionCompliant())) {
            loanApplicationService.suspendLoan(request.getLoanApplicationId());
        }

        return toResponse(saved);
    }

    private void applyRequest(ExhibitionMonitoring monitoring, ExhibitionMonitoringRequest request) {
        monitoring.setLoanApplicationId(request.getLoanApplicationId());
        monitoring.setShowcaseCondition(request.getShowcaseCondition());
        monitoring.setSecurityPlan(request.getSecurityPlan());
        monitoring.setTemperature(request.getTemperature());
        monitoring.setHumidity(request.getHumidity());
        monitoring.setConditionCompliant(request.getConditionCompliant());
        monitoring.setMonitoringDate(request.getMonitoringDate());
        monitoring.setNotes(request.getNotes());
    }

    private ExhibitionMonitoringResponse toResponse(ExhibitionMonitoring monitoring) {
        return new ExhibitionMonitoringResponse(
                monitoring.getId(),
                monitoring.getLoanApplicationId(),
                monitoring.getShowcaseCondition(),
                monitoring.getSecurityPlan(),
                monitoring.getTemperature(),
                monitoring.getHumidity(),
                monitoring.getConditionCompliant(),
                monitoring.getMonitoringDate(),
                monitoring.getNotes(),
                monitoring.getCreatedAt()
        );
    }
}
