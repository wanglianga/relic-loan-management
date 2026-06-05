package com.relicloan.service;

import com.relicloan.dto.HandoverRecordRequest;
import com.relicloan.dto.HandoverRecordResponse;
import com.relicloan.entity.HandoverRecord;
import com.relicloan.repository.HandoverRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandoverRecordService {
    private final HandoverRecordRepository handoverRecordRepository;

    public List<HandoverRecordResponse> findAll() {
        return handoverRecordRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public HandoverRecordResponse findById(Long id) {
        HandoverRecord record = handoverRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("HandoverRecord not found: " + id));
        return toResponse(record);
    }

    public HandoverRecordResponse create(HandoverRecordRequest request) {
        HandoverRecord record = new HandoverRecord();
        applyRequest(record, request);
        return toResponse(handoverRecordRepository.save(record));
    }

    private void applyRequest(HandoverRecord record, HandoverRecordRequest request) {
        record.setLoanApplicationId(request.getLoanApplicationId());
        record.setHandoverType(request.getHandoverType());
        record.setHandlerFrom(request.getHandlerFrom());
        record.setHandlerTo(request.getHandlerTo());
        record.setConditionDescription(request.getConditionDescription());
        record.setPhotos(request.getPhotos());
        record.setNotes(request.getNotes());
        record.setHandoverTime(request.getHandoverTime());
    }

    private HandoverRecordResponse toResponse(HandoverRecord record) {
        return new HandoverRecordResponse(
                record.getId(),
                record.getLoanApplicationId(),
                record.getHandoverType(),
                record.getHandlerFrom(),
                record.getHandlerTo(),
                record.getConditionDescription(),
                record.getPhotos(),
                record.getNotes(),
                record.getHandoverTime(),
                record.getCreatedAt()
        );
    }
}
