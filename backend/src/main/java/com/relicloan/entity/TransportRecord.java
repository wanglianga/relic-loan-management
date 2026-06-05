package com.relicloan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transport_records")
public class TransportRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long loanApplicationId;

    private String humidityBox;

    private String escortPersonnel;

    @Column(columnDefinition = "TEXT")
    private String vibrationRecords;

    private String route;

    @Column(columnDefinition = "TEXT")
    private String handoverPhotos;

    @Enumerated(EnumType.STRING)
    private TransportStatus status;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;

    private Boolean vibrationExceeded;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = TransportStatus.PREPARING;
        }
        if (vibrationExceeded == null) {
            vibrationExceeded = false;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
