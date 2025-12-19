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
public class DimensionDifferenceId implements Serializable {
    @Column(name = "comparison_id")
    private String comparisonId;

    @Column(name = "key_code")
    private String keyCode;
}
