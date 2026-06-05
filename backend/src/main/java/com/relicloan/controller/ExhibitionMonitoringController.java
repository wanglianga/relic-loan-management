package com.relicloan.controller;

import com.relicloan.dto.ExhibitionMonitoringRequest;
import com.relicloan.dto.ExhibitionMonitoringResponse;
import com.relicloan.service.ExhibitionMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitorings")
@CrossOrigin
@RequiredArgsConstructor
public class ExhibitionMonitoringController {
    private final ExhibitionMonitoringService exhibitionMonitoringService;

    @GetMapping
    public List<ExhibitionMonitoringResponse> list() {
        return exhibitionMonitoringService.findAll();
    }

    @GetMapping("/{id}")
    public ExhibitionMonitoringResponse detail(@PathVariable Long id) {
        return exhibitionMonitoringService.findById(id);
    }

    @PostMapping
    public ExhibitionMonitoringResponse create(@RequestBody ExhibitionMonitoringRequest request) {
        return exhibitionMonitoringService.create(request);
    }
}
