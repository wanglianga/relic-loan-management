package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnRecordRequest {
    private Long loanApplicationId;
    private String returnCondition;
    private String returnedTo;
    private LocalDate returnDate;
    private Boolean isExtension;
    private String extensionReason;
}
