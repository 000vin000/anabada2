package kr.co.anabada.auth.repository;

import kr.co.anabada.auth.entity.EmailAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailAuthTokenRepository extends JpaRepository<EmailAuthToken, Long> {
    Optional<EmailAuthToken> findTopByEmailAndIsUsedTrueOrderByExpiresAtDesc(String email);
    Optional<EmailAuthToken> findTopByEmailAndCodeOrderByIdDesc(String email, String code);
}
