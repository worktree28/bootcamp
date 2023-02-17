package com.bootcamp.service;

import com.bootcamp.model.OTP;
import com.bootcamp.model.User;
import com.bootcamp.repository.OTPRepository;
import com.bootcamp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OTPService {
    @Autowired
    OTPRepository otpRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailService emailService;

    public List<OTP> getAll() {
        List<OTP> otpList = otpRepository.findAll();
        if (otpList == null)
            return new ArrayList<>();
        return otpList;
    }

//    public void deleteByEmail(String email) {
//        otpRepository.deleteByEmail("akashsudanf5@gmail.com");
//    }

    public void generateOTP(String email) {
        OTP otp = new OTP(email);
        otp.generateOTP();
        Optional<OTP> otpTemp = otpRepository.findByEmail(email);
        if (otpTemp.isPresent())
            otpRepository.deleteByEmail(email);
        otpRepository.save(otp);
        emailService.sendSimpleEmail(email, "OTP", Integer.toString(otp.getOtp()));

    }

    public void saveOTP(OTP otp) {
        Optional<User> userTmp = userRepository.findByEmail(otp.getEmail());
        if (userTmp.isPresent()) {
            otpRepository.save(otp);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<String> validateOTP(int otp, String email) {
        Optional<OTP> otpTmp = otpRepository.findByEmail(email);
        if (otpTmp.isPresent()) {
            if (otpTmp.get().getOtp() == otp) {
                otpRepository.deleteByEmail(email);
                System.out.println("Valid");
                return new ResponseEntity<>(
                        "Otp verification successful ",
                        HttpStatus.OK);
            }
        }
        System.out.println("Not valid");
        return new ResponseEntity<>(
                "Invalid Otp",
                HttpStatus.BAD_REQUEST);

    }

}
