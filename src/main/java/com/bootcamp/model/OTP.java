package com.bootcamp.model;

import lombok.Data;

import jakarta.persistence.*;
@Data
@Entity
@Table(name = "otps")
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "otp")
    private Integer otp;
    @Column(name = "user_id")
    private Long userId;

    public OTP(String email) {
        this.email = email;
    }
    public void generateOTP() {
        this.otp = ((int) (Math.random() * 1000000)) % 10000;
    }
}
