package com.relicloan.controller;

import com.relicloan.dto.InsurancePolicyRequest;
import com.relicloan.dto.InsurancePolicyResponse;
import com.relicloan.service.InsurancePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/insurances")
@CrossOrigin
@RequiredArgsConstructor
public class InsurancePolicyController {
    private final InsurancePolicyService insurancePolicyService;

    @GetMapping
    public List<InsurancePolicyResponse> list() {
        return insurancePolicyService.findAll();
    }

    @GetMapping("/{id}")
    public InsurancePolicyResponse detail(@PathVariable Long id) {
        return insurancePolicyService.findById(id);
    }

    @PostMapping
    public InsurancePolicyResponse create(@RequestBody InsurancePolicyRequest request) {
        return insurancePolicyService.create(request);
    }

    @PutMapping("/{id}")
    public InsurancePolicyResponse update(@PathVariable Long id, @RequestBody InsurancePolicyRequest request) {
        return insurancePolicyService.update(id, request);
    }
}
