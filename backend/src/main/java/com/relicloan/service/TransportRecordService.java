package com.relicloan.service;

import com.relicloan.dto.TransportRecordRequest;
import com.relicloan.dto.TransportRecordResponse;
import com.relicloan.dto.VibrationReportRequest;
import com.relicloan.entity.TransportRecord;
import com.relicloan.entity.TransportStatus;
import com.relicloan.repository.TransportRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportRecordService {
    private final TransportRecordRepository transportRecordRepository;
    private final LoanApplicationService loanApplicationService;
    private final InsurancePolicyService insurancePolicyService;

    public List<TransportRecordResponse> findAll() {
        return transportRecordRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransportRecordResponse findById(Long id) {
        TransportRecord record = transportRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TransportRecord not found: " + id));
        return toResponse(record);
    }

    public TransportRecordResponse create(TransportRecordRequest request) {
        TransportRecord record = new TransportRecord();
        applyRequest(record, request);
        return toResponse(transportRecordRepository.save(record));
    }

    public TransportRecordResponse update(Long id, TransportRecordRequest request) {
        TransportRecord record = transportRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TransportRecord not found: " + id));
        applyRequest(record, request);
        return toResponse(transportRecordRepository.save(record));
    }

    @Transactional
    public TransportRecordResponse reportVibration(Long id, VibrationReportRequest request) {
        TransportRecord record = transportRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TransportRecord not found: " + id));

        String existing = record.getVibrationRecords() != null ? record.getVibrationRecords() : "";
        record.setVibrationRecords(existing + (existing.isEmpty() ? "" : "\n") + request.getVibrationData());

        if (request.isExceeded()) {
            record.setVibrationExceeded(true);
            record.setStatus(TransportStatus.ABNORMAL);
            transportRecordRepository.save(record);

            loanApplicationService.suspendLoan(record.getLoanApplicationId());
            insurancePolicyService.triggerAdjusting(record.getLoanApplicationId(),
                    "Vibration exceeded during transport, policy adjusting");
        } else {
            transportRecordRepository.save(record);
        }

        return toResponse(record);
    }

    private void applyRequest(TransportRecord record, TransportRecordRequest request) {
        record.setLoanApplicationId(request.getLoanApplicationId());
        record.setHumidityBox(request.getHumidityBox());
        record.setEscortPersonnel(request.getEscortPersonnel());
        record.setVibrationRecords(request.getVibrationRecords());
        record.setRoute(request.getRoute());
        record.setHandoverPhotos(request.getHandoverPhotos());
        record.setDepartureTime(request.getDepartureTime());
        record.setArrivalTime(request.getArrivalTime());
    }

    private TransportRecordResponse toResponse(TransportRecord record) {
        return new TransportRecordResponse(
                record.getId(),
                record.getLoanApplicationId(),
                record.getHumidityBox(),
                record.getEscortPersonnel(),
                record.getVibrationRecords(),
                record.getRoute(),
                record.getHandoverPhotos(),
                record.getStatus(),
                record.getDepartureTime(),
                record.getArrivalTime(),
                record.getVibrationExceeded(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
