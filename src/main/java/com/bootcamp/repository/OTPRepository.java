package com.bootcamp.repository;

import com.bootcamp.model.OTP;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP, Long> {
    public Optional<OTP> findByEmail(String email);

    @Transactional
    public Long deleteByEmail(String email);
}

