package com.relicloan.dto;

import com.relicloan.entity.TransportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportRecordResponse {
    private Long id;
    private Long loanApplicationId;
    private String humidityBox;
    private String escortPersonnel;
    private String vibrationRecords;
    private String route;
    private String handoverPhotos;
    private TransportStatus status;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Boolean vibrationExceeded;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
