package com.java.emergency_system_java.controller;

import com.java.emergency_system_java.entity.User;
import com.java.emergency_system_java.repository.UserRepository;
import com.java.emergency_system_java.services.auth.TokenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService,
                          UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto data) {
        if (userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        
        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(null, data.name(), data.email(), encryptedPassword);
        userRepository.save(newUser);
        
        return ResponseEntity.ok().build();
    }
}

record LoginRequestDto(
        @NotBlank(message = "email é obrigatório") String email,
        @NotBlank(message = "password é obrigatório") String password
) {}

record LoginResponseDto(String token) {}

record RegisterRequestDto(
        @NotBlank(message = "nome é obrigatório") String name,
        @NotBlank(message = "email é obrigatório") @Email(message = "email inválido") String email,
        @NotBlank(message = "password é obrigatório") String password
) {}
