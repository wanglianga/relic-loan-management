package com.relicloan.controller;

import com.relicloan.dto.ReturnRecordRequest;
import com.relicloan.dto.ReturnRecordResponse;
import com.relicloan.service.ReturnRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/returns")
@CrossOrigin
@RequiredArgsConstructor
public class ReturnRecordController {
    private final ReturnRecordService returnRecordService;

    @GetMapping
    public List<ReturnRecordResponse> list() {
        return returnRecordService.findAll();
    }

    @GetMapping("/{id}")
    public ReturnRecordResponse detail(@PathVariable Long id) {
        return returnRecordService.findById(id);
    }

    @PostMapping
    public ReturnRecordResponse create(@RequestBody ReturnRecordRequest request) {
        return returnRecordService.create(request);
    }
}
