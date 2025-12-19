package com.fitfounder.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Column(name = "user_id", length = 40)
    private String userId;

    @Column(name = "nickname", nullable = false, length = 100)
    private String nickname;

    @Column(name = "height_cm", nullable = false)
    private Integer heightCm;

    @Column(name = "weight_kg", nullable = false)
    private Integer weightKg;

    @Column(name = "profile_image_asset_id")
    private String profileImageAssetId;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "last_link_at")
    private OffsetDateTime lastLinkAt;

    @Column(name = "seen_tutorial_flag", nullable = false)
    private boolean seenTutorialFlag = false;

    @Column(name = "primary_email_id")
    private String primaryEmailId;

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
