package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportRecordRequest {
    private Long loanApplicationId;
    private String humidityBox;
    private String escortPersonnel;
    private String vibrationRecords;
    private String route;
    private String handoverPhotos;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
}
