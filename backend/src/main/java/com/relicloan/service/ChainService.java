package com.relicloan.service;

import com.relicloan.dto.*;
import com.relicloan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChainService {
    private final LoanApplicationService loanApplicationService;
    private final ArtifactService artifactService;
    private final InsurancePolicyService insurancePolicyService;
    private final TransportRecordService transportRecordService;
    private final HandoverRecordService handoverRecordService;
    private final ExhibitionMonitoringService exhibitionMonitoringService;
    private final ReturnRecordService returnRecordService;

    public ChainResponse getChain(Long loanId) {
        LoanApplicationResponse loan = loanApplicationService.findById(loanId);
        ArtifactResponse artifact = artifactService.findById(loan.getArtifactId());

        return new ChainResponse(
                loan,
                artifact,
                insurancePolicyService.findByLoanApplicationId(loanId),
                transportRecordService.findAll().stream()
                        .filter(t -> t.getLoanApplicationId().equals(loanId))
                        .collect(Collectors.toList()),
                handoverRecordService.findAll().stream()
                        .filter(h -> h.getLoanApplicationId().equals(loanId))
                        .collect(Collectors.toList()),
                exhibitionMonitoringService.findAll().stream()
                        .filter(m -> m.getLoanApplicationId().equals(loanId))
                        .collect(Collectors.toList()),
                returnRecordService.findAll().stream()
                        .filter(r -> r.getLoanApplicationId().equals(loanId))
                        .collect(Collectors.toList())
        );
    }
}
