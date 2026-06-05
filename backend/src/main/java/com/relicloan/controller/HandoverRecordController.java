package com.relicloan.controller;

import com.relicloan.dto.HandoverRecordRequest;
import com.relicloan.dto.HandoverRecordResponse;
import com.relicloan.service.HandoverRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/handovers")
@CrossOrigin
@RequiredArgsConstructor
public class HandoverRecordController {
    private final HandoverRecordService handoverRecordService;

    @GetMapping
    public List<HandoverRecordResponse> list() {
        return handoverRecordService.findAll();
    }

    @GetMapping("/{id}")
    public HandoverRecordResponse detail(@PathVariable Long id) {
        return handoverRecordService.findById(id);
    }

    @PostMapping
    public HandoverRecordResponse create(@RequestBody HandoverRecordRequest request) {
        return handoverRecordService.create(request);
    }
}
