package com.bootcamp.service;

import com.bootcamp.model.OTP;
import com.bootcamp.model.User;
import com.bootcamp.repository.OTPRepository;
import com.bootcamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class OTPService {
    @Autowired
    OTPRepository otpRepository;
    @Autowired
    UserRepository userRepository;
    public void generateOTP(String email){
        if (userRepository.findByEmail(email).isPresent()) {
            Optional<OTP> otpTmp = otpRepository.findByEmail(email);
            OTP otp;
            otp = otpTmp.orElseGet(() -> new OTP(email));
            otp.generateOTP();
            otpRepository.save(otp);
            String host = "smtp.gmail.com";
            final String user = "playground.kata@gmail.com";
            final String password = "<playground>";


            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            props.put("mail.debug", "true");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(user, password);
                        }
                    });

            System.out.println("#########################################");
            System.out.println("Auth Successful");

            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(otp.getEmail()));
                message.setSubject("Email verification for ToDos");
                message.setText(String.format("Your otp is %d", otp.getOtp()));
                System.out.println("All good here -------------------");

                Transport.send(message);

                System.out.println("message sent successfully...");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public void saveOTP(OTP otp){
        Optional<User> userTmp=userRepository.findByEmail(otp.getEmail());
        if(userTmp.isPresent()){
            otpRepository.save(otp);
        }
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public boolean validateOTP(int otp, String email){
        Optional<OTP> otpTmp=otpRepository.findByEmail(email);
        if(otpTmp.isPresent()){
            if(otpTmp.get().getOtp()==otp){
                return true;
            }
        }
        return false;
    }

}
