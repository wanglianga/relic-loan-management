package com.relicloan.controller;

import com.relicloan.dto.TransportRecordRequest;
import com.relicloan.dto.TransportRecordResponse;
import com.relicloan.dto.VibrationReportRequest;
import com.relicloan.service.TransportRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transports")
@CrossOrigin
@RequiredArgsConstructor
public class TransportRecordController {
    private final TransportRecordService transportRecordService;

    @GetMapping
    public List<TransportRecordResponse> list() {
        return transportRecordService.findAll();
    }

    @GetMapping("/{id}")
    public TransportRecordResponse detail(@PathVariable Long id) {
        return transportRecordService.findById(id);
    }

    @PostMapping
    public TransportRecordResponse create(@RequestBody TransportRecordRequest request) {
        return transportRecordService.create(request);
    }

    @PutMapping("/{id}")
    public TransportRecordResponse update(@PathVariable Long id, @RequestBody TransportRecordRequest request) {
        return transportRecordService.update(id, request);
    }

    @PostMapping("/{id}/vibration")
    public TransportRecordResponse reportVibration(@PathVariable Long id, @RequestBody VibrationReportRequest request) {
        return transportRecordService.reportVibration(id, request);
    }
}
