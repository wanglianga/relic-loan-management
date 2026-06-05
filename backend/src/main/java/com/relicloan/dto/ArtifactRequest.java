package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtifactRequest {
    private String name;
    private String category;
    private String grade;
    private BigDecimal appraisedValue;
    private String dimensions;
    private String humidityRequirement;
    private LocalDate loanDeadline;
    private String museumName;
    private String description;
}
