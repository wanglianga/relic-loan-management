package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationRequest {
    private Long artifactId;
    private String applicantMuseum;
    private String borrowingVenue;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contractNumber;
    private String specialRequirements;
}
