package com.relicloan.dto;

import com.relicloan.entity.HandoverType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandoverRecordRequest {
    private Long loanApplicationId;
    private HandoverType handoverType;
    private String handlerFrom;
    private String handlerTo;
    private String conditionDescription;
    private String photos;
    private String notes;
    private LocalDateTime handoverTime;
}
