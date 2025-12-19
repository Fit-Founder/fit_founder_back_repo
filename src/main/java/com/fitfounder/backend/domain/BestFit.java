package com.fitfounder.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "bestfits")
@Getter
@Setter
public class BestFit {
    @Id
    @Column(name = "bestfit_id")
    private String bestfitId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "size_chart_id", nullable = false)
    private String sizeChartId;

    @Column(name = "row_id", nullable = false)
    private String rowId;

    @Column(name = "product_name", nullable = false, length = 20)
    private String productName;

    @Column(name = "memo", length = 100)
    private String memo;

    @Column(name = "favorite", nullable = false)
    private boolean favorite = false;

    @Column(name = "pant_fit_type")
    private String pantFitType;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

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
