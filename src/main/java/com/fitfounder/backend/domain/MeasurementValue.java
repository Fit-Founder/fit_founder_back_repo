package com.fitfounder.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "measurement_values")
@Getter
@Setter
public class MeasurementValue {
    @EmbeddedId
    private MeasurementValueId id;

    @Column(name = "value", nullable = false)
    private Double value;
}
