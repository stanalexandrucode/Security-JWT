package com.example.codecool.controller;


import com.example.codecool.dto.AuthenticationResponse;
import com.example.codecool.dto.LoginRequest;
import com.example.codecool.dto.RegistrationRequest;
import com.example.codecool.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegistrationRequest registrationRequest){
        authService.signup(registrationRequest);
        return new ResponseEntity<>("User registration successful",HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }
    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest){
      return authService.login(loginRequest);
    }

}
