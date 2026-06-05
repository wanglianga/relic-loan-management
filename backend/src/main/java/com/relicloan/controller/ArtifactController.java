package com.relicloan.controller;

import com.relicloan.dto.ArtifactRequest;
import com.relicloan.dto.ArtifactResponse;
import com.relicloan.service.ArtifactService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artifacts")
@CrossOrigin
@RequiredArgsConstructor
public class ArtifactController {
    private final ArtifactService artifactService;

    @GetMapping
    public List<ArtifactResponse> list() {
        return artifactService.findAll();
    }

    @GetMapping("/{id}")
    public ArtifactResponse detail(@PathVariable Long id) {
        return artifactService.findById(id);
    }

    @PostMapping
    public ArtifactResponse create(@RequestBody ArtifactRequest request) {
        return artifactService.create(request);
    }

    @PutMapping("/{id}")
    public ArtifactResponse update(@PathVariable Long id, @RequestBody ArtifactRequest request) {
        return artifactService.update(id, request);
    }
}
