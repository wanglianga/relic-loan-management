package com.relicloan.dto;

import com.relicloan.entity.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplicationResponse {
    private Long id;
    private Long artifactId;
    private String applicantMuseum;
    private String borrowingVenue;
    private LocalDate startDate;
    private LocalDate endDate;
    private LoanStatus status;
    private String contractNumber;
    private String specialRequirements;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
