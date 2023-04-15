package com.nikola.blog.controller;

import com.nikola.blog.payload.JWTAuthResponse;
import com.nikola.blog.payload.LoginDto;
import com.nikola.blog.payload.RegisterDto;
import com.nikola.blog.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "CRUD REST APIs for Authentication Resource")
public class AuthController {
    private AuthService authService;
    @Autowired
    public AuthController(AuthService theAuthService) {
        authService = theAuthService;
    }

    // Build Login REST API
    @PostMapping(value = {"/login","/signin"})
    public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto){
        String token = authService.login(loginDto);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setAccessToken(token);

        return ResponseEntity.ok(jwtAuthResponse);
    }
    // Build Register REST API
    @PostMapping(value = {"/register","/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = authService.register(registerDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
