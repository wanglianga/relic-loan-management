package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VibrationReportRequest {
    private String vibrationData;
    private boolean exceeded;
}
