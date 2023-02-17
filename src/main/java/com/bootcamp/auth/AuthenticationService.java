package com.bootcamp.auth;

import com.bootcamp.config.JwtService;
import com.bootcamp.model.Role;
import com.bootcamp.model.User;
import com.bootcamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws Exception {
        var user = User.builder().name(request.getName()).email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
        //repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        user.setToken(jwtToken);

        if (repository.findByEmail(user.getEmail()).isPresent()) {
            return AuthenticationResponse.builder().status(409).message("User with this email already exists.")
                    .token(null).build();
        } else {
            repository.save(user);
        }
        return AuthenticationResponse.builder().status(200).message("Success").token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            return AuthenticationResponse.builder().status(401).message("Invalid Credentials").token("").build();
        }
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().status(200).message("Success").token(jwtToken).build();
    }
}
