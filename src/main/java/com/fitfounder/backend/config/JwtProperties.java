package com.fitfounder.backend.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @NotBlank
    private String secret;

    @NotBlank
    private String issuer;

    @NotBlank
    private String audience;

    @NotNull
    private Long accessTokenTtlSeconds;

    @NotNull
    private Long refreshTokenTtlSeconds;
}
