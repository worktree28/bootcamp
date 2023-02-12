package com.bootcamp.controller;

import com.bootcamp.service.OTPService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OTPController {
    private final OTPService otpService;
    @GetMapping("/hello")
    public String sm(){return "hello";}

    @PostMapping("/otp-generate")
    public void sendOTP(@RequestBody UserEmail userEmail){
        otpService.generateOTP(userEmail.getEmail());
    }
    @PostMapping("/otp-verify")
    public void verifyOTP(@RequestBody UserOTP userOTP){
        otpService.validateOTP(userOTP.otp(), userOTP.email());
    }

    private record UserOTP(String email, int otp){}
    @Data
    private static class UserEmail{
        String email;
    }
}
