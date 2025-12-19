package com.fitfounder.backend.repository;

import com.fitfounder.backend.domain.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, String> {
    Optional<UserSession> findBySessionIdAndRevokedAtIsNull(String sessionId);
}
