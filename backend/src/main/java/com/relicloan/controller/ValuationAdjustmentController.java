package com.relicloan.controller;

import com.relicloan.dto.ValuationAdjustmentRequest;
import com.relicloan.dto.ValuationAdjustmentResponse;
import com.relicloan.service.ValuationAdjustmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/valuation-adjustments")
@CrossOrigin
@RequiredArgsConstructor
public class ValuationAdjustmentController {
    private final ValuationAdjustmentService valuationAdjustmentService;

    @GetMapping
    public List<ValuationAdjustmentResponse> list() {
        return valuationAdjustmentService.findAll();
    }

    @GetMapping("/{id}")
    public ValuationAdjustmentResponse detail(@PathVariable Long id) {
        return valuationAdjustmentService.findById(id);
    }

    @GetMapping("/artifact/{artifactId}")
    public List<ValuationAdjustmentResponse> findByArtifactId(@PathVariable Long artifactId) {
        return valuationAdjustmentService.findByArtifactId(artifactId);
    }

    @GetMapping("/loan/{loanApplicationId}")
    public List<ValuationAdjustmentResponse> findByLoanApplicationId(@PathVariable Long loanApplicationId) {
        return valuationAdjustmentService.findByLoanApplicationId(loanApplicationId);
    }

    @GetMapping("/policy/{insurancePolicyId}")
    public List<ValuationAdjustmentResponse> findByInsurancePolicyId(@PathVariable Long insurancePolicyId) {
        return valuationAdjustmentService.findByInsurancePolicyId(insurancePolicyId);
    }

    @PostMapping
    public ValuationAdjustmentResponse create(@RequestBody ValuationAdjustmentRequest request) {
        return valuationAdjustmentService.create(request);
    }

    @GetMapping("/check-sufficient/{loanApplicationId}")
    public Map<String, Boolean> checkInsuredAmountSufficient(@PathVariable Long loanApplicationId) {
        boolean sufficient = valuationAdjustmentService.isInsuredAmountSufficient(loanApplicationId);
        return Map.of("sufficient", sufficient);
    }
}
