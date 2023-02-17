package com.bootcamp.controller;

import com.bootcamp.model.OTP;
import com.bootcamp.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class OTPController {
    @Autowired
    OTPService otpService;

    @GetMapping("/hello")
    public String sm() {
        return "hello";
    }

    @GetMapping("/getAll")
    public List<OTP> getAll() {
        return otpService.getAll();
    }

//    @GetMapping("/otp-delete/{email}")
//    public void deleteByEmail(@PathVariable String email) {
//        otpService.deleteByEmail(email);
//    }

    @PostMapping("/otp-generate")
    public void sendOTP(@RequestBody UserEmail userEmail) {
        otpService.generateOTP(userEmail.email());
    }

    @PostMapping("/otp-verify")
    public ResponseEntity<String> verifyOTP(@RequestBody UserOTP userOTP) {
        return otpService.validateOTP(userOTP.otp(), userOTP.email());
    }

    private record UserOTP(String email, int otp) {
    }

    private record UserEmail(String email) {
    }
}
