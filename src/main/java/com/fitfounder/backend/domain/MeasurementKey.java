package com.fitfounder.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "measurement_keys")
@Getter
@Setter
public class MeasurementKey {
    @Id
    @Column(name = "key_code")
    private String keyCode;

    @Column(name = "garment_type", nullable = false)
    private String garmentType;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
