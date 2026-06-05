package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRecordResponse {
    private Long id;
    private Long loanApplicationId;
    private String returnCondition;
    private String returnedTo;
    private LocalDate returnDate;
    private Boolean isExtension;
    private String extensionReason;
    private LocalDateTime createdAt;
}
