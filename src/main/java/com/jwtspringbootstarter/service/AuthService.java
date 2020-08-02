package com.jwtspringbootstarter.service;

import com.jwtspringbootstarter.config.JWTConfig;
import com.jwtspringbootstarter.dto.AuthenticationResponse;
import com.jwtspringbootstarter.dto.LoginRequest;
import com.jwtspringbootstarter.dto.RegisterRequest;
import com.jwtspringbootstarter.model.User;
import com.jwtspringbootstarter.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;

import static java.util.Date.from;

@Service
@Transactional
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTConfig jwtConfig;


    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, JWTConfig jwtConfig) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
    }

    public void signup(RegisterRequest registerRequest){
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstname());
        user.setLastName(registerRequest.getLastname());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = (User) authentication.getPrincipal();
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(from(Instant.now()))
                .claim("authorities", user.getAuthorities())
                .signWith(jwtConfig.secretKey())
                .setExpiration(Date.from(Instant.now().plusMillis(jwtConfig.getTokenExpirationAfterDays())))
                .compact();
        AuthenticationResponse response = new AuthenticationResponse();
        response.setAuthenticationToken(token);
        response.setExpiresAt(Instant.now().plusMillis(jwtConfig.getTokenExpirationAfterDays()));
        response.setUsername(loginRequest.getUsername());
        return response;
    }
}
