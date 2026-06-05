package com.relicloan.service;

import com.relicloan.dto.ArtifactRequest;
import com.relicloan.dto.ArtifactResponse;
import com.relicloan.entity.Artifact;
import com.relicloan.repository.ArtifactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtifactService {
    private final ArtifactRepository artifactRepository;

    public List<ArtifactResponse> findAll() {
        return artifactRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ArtifactResponse findById(Long id) {
        Artifact artifact = artifactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artifact not found: " + id));
        return toResponse(artifact);
    }

    public ArtifactResponse create(ArtifactRequest request) {
        Artifact artifact = new Artifact();
        applyRequest(artifact, request);
        return toResponse(artifactRepository.save(artifact));
    }

    public ArtifactResponse update(Long id, ArtifactRequest request) {
        Artifact artifact = artifactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artifact not found: " + id));
        applyRequest(artifact, request);
        return toResponse(artifactRepository.save(artifact));
    }

    private void applyRequest(Artifact artifact, ArtifactRequest request) {
        artifact.setName(request.getName());
        artifact.setCategory(request.getCategory());
        artifact.setGrade(request.getGrade());
        artifact.setAppraisedValue(request.getAppraisedValue());
        artifact.setDimensions(request.getDimensions());
        artifact.setHumidityRequirement(request.getHumidityRequirement());
        artifact.setLoanDeadline(request.getLoanDeadline());
        artifact.setMuseumName(request.getMuseumName());
        artifact.setDescription(request.getDescription());
    }

    private ArtifactResponse toResponse(Artifact artifact) {
        return new ArtifactResponse(
                artifact.getId(),
                artifact.getName(),
                artifact.getCategory(),
                artifact.getGrade(),
                artifact.getAppraisedValue(),
                artifact.getDimensions(),
                artifact.getHumidityRequirement(),
                artifact.getLoanDeadline(),
                artifact.getMuseumName(),
                artifact.getDescription(),
                artifact.getCreatedAt(),
                artifact.getUpdatedAt()
        );
    }
}
