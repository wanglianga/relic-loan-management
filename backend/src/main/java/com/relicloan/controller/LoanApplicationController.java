package com.relicloan.controller;

import com.relicloan.dto.LoanApplicationRequest;
import com.relicloan.dto.LoanApplicationResponse;
import com.relicloan.dto.LoanExtensionRequest;
import com.relicloan.dto.LoanStatusUpdateRequest;
import com.relicloan.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin
@RequiredArgsConstructor
public class LoanApplicationController {
    private final LoanApplicationService loanApplicationService;

    @GetMapping
    public List<LoanApplicationResponse> list() {
        return loanApplicationService.findAll();
    }

    @GetMapping("/{id}")
    public LoanApplicationResponse detail(@PathVariable Long id) {
        return loanApplicationService.findById(id);
    }

    @PostMapping
    public LoanApplicationResponse create(@RequestBody LoanApplicationRequest request) {
        return loanApplicationService.create(request);
    }

    @PutMapping("/{id}")
    public LoanApplicationResponse update(@PathVariable Long id, @RequestBody LoanApplicationRequest request) {
        return loanApplicationService.update(id, request);
    }

    @PutMapping("/{id}/status")
    public LoanApplicationResponse updateStatus(@PathVariable Long id, @RequestBody LoanStatusUpdateRequest request) {
        return loanApplicationService.updateStatus(id, request);
    }

    @PostMapping("/{id}/extend")
    public LoanApplicationResponse extendLoan(@PathVariable Long id, @RequestBody LoanExtensionRequest request) {
        return loanApplicationService.extendLoan(id, request);
    }

    @GetMapping("/{id}/can-extend")
    public Map<String, Boolean> canExtend(@PathVariable Long id) {
        boolean canExtend = loanApplicationService.canExtend(id);
        return Map.of("canExtend", canExtend);
    }
}
