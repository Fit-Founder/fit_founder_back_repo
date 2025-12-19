package com.fitfounder.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dimension_differences")
@Getter
@Setter
public class DimensionDifference {
    @EmbeddedId
    private DimensionDifferenceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("comparisonId")
    @JoinColumn(name = "comparison_id")
    private Comparison comparison;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "bestfit_value", nullable = false)
    private Double bestfitValue;

    @Column(name = "newfit_value", nullable = false)
    private Double newfitValue;

    @Column(name = "delta", nullable = false)
    private Double delta;
}
