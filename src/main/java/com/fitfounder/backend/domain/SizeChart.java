package com.fitfounder.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "size_charts")
@Getter
@Setter
public class SizeChart {
    @Id
    @Column(name = "size_chart_id")
    private String sizeChartId;

    @Column(name = "garment_type", nullable = false)
    private String garmentType;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
