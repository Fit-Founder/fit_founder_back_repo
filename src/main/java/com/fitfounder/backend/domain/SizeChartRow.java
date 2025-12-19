package com.fitfounder.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "size_chart_rows")
@Getter
@Setter
public class SizeChartRow {
    @Id
    @Column(name = "row_id")
    private String rowId;

    @Column(name = "size_chart_id", nullable = false)
    private String sizeChartId;

    @Column(name = "size_label", nullable = false)
    private String sizeLabel;

    @Column(name = "ordinal", nullable = false)
    private Short ordinal;
}
