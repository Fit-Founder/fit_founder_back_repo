package com.fitfounder.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "social_accounts")
@Getter
@Setter
public class SocialAccount {
    @Id
    @Column(name = "social_account_id", length = 64)
    private String socialAccountId;

    @Column(name = "user_id", nullable = false, length = 40)
    private String userId;

    @Column(name = "link_flow_id")
    private String linkFlowId;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "email_updated_at")
    private OffsetDateTime emailUpdatedAt;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "provider_scope")
    private String providerScope;

    @Column(name = "connected_at", nullable = false)
    private OffsetDateTime connectedAt;

    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;

    @PrePersist
    public void onCreate() {
        connectedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
