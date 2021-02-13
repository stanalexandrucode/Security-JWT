package com.example.codecool.service;


import com.example.codecool.dto.AuthenticationResponse;
import com.example.codecool.dto.LoginRequest;
import com.example.codecool.dto.RegistrationRequest;
import com.example.codecool.exception.ToDoNotFoundException;
import com.example.codecool.model.NotificationEmail;
import com.example.codecool.model.Roles;
import com.example.codecool.model.User;
import com.example.codecool.model.VerificationToken;
import com.example.codecool.repository.UserRepository;
import com.example.codecool.repository.VerificationTokenRepository;
import com.example.codecool.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;


    @Transactional
    public void signup(RegistrationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(Instant.now());
        user.setRoles(Roles.USER);
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
                "Thank you for signing up to CodeCool please click on the bellow url to activate your account : " +
                        "http://localhost:8080/api/auth/accountVerification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new ToDoNotFoundException("Invalid token"));
        fetchUserAndEnable(verificationToken.get());

    }

//    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ToDoNotFoundException("User not found with name -" + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    /**
     * @SecurityContextHolder is the most fundamental object where we store details of the present security context of the application
     * (includes details of the principal). Spring Security uses an Authentication object to represent this information and we can
     * query this Authentication object from anywhere in our application.
     * getContext() returns an instance of SecurityContext interface that holds the Authentication and possibly request-specific security information.
     */

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwtProvider.generateToken(authenticate);
        return new AuthenticationResponse(token, loginRequest.getUsername());
    }
}
