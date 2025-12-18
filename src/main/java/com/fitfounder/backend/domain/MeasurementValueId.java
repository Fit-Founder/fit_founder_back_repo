package com.fitfounder.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementValueId implements Serializable {
    @Column(name = "row_id")
    private String rowId;

    @Column(name = "key_code")
    private String keyCode;
}
