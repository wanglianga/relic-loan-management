package com.relicloan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChainResponse {
    private LoanApplicationResponse loanApplication;
    private ArtifactResponse artifact;
    private List<InsurancePolicyResponse> insurances;
    private List<TransportRecordResponse> transports;
    private List<HandoverRecordResponse> handovers;
    private List<ExhibitionMonitoringResponse> monitorings;
    private List<ReturnRecordResponse> returns;
}
