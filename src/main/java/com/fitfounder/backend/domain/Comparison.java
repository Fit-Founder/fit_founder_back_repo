package com.fitfounder.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comparisons")
@Getter
@Setter
public class Comparison {
    @Id
    @Column(name = "comparison_id")
    private String comparisonId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "bestfit_id", nullable = false)
    private String bestfitId;

    @Column(name = "size_chart_id", nullable = false)
    private String sizeChartId;

    @Column(name = "newfit_selected_row_id")
    private String newfitSelectedRowId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "decision")
    private String decision;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "comparison", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DimensionDifference> differences = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
