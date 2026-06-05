package com.relicloan.dto;

import com.relicloan.entity.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanStatusUpdateRequest {
    private LoanStatus status;
}
