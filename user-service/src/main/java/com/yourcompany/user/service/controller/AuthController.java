package com.yourcompany.user.service.controller;

import com.yourcompany.user.service.dto.AuthRequest;
import com.yourcompany.user.service.dto.AuthResponse;
import com.yourcompany.user.service.dto.RefreshTokenRequest;
import com.yourcompany.user.service.dto.SignupRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    // @PostMapping("/login")
    // public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
    //     // --- MOCK AUTHENTICATION ---
    //     if ("user".equals(authRequest.getUsername()) && "password".equals(authRequest.getPassword())) {
    //         log.info("Authentication successful for user: {}", authRequest.getUsername());
    //         String token = generateJwtToken(authRequest.getUsername());
    //         return ResponseEntity.ok(new AuthResponse(token));
    //     }

    //     log.warn("Authentication failed for user: {}", authRequest.getUsername());
    //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    // }

    @PostMapping("/refresh/token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            Claims claims = getUsernameFromJwt(request.getToken().split(" ")[1]);
            String newToken = generateJwtToken(claims);
            return ResponseEntity.ok(new AuthResponse(newToken));
        } catch (Exception e) {
            log.error("Invalid token for refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // @PostMapping("/signup")
    // public ResponseEntity<AuthResponse> signupUser(@RequestBody SignupRequest signupRequest) {
    //     // --- MOCK USER CREATION ---
    //     log.info("Creating new user: {}", signupRequest.getUsername());
    //     String token = generateJwtToken(Claims.builder()
    //             .setSubject(signupRequest.getUsername())
    //             .claim("userId", "mockUserId123") // Mock user ID
    //             .claim("roles", List.of("USER"))   // Mock roles
    //             .build());
    //     return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse(token));
    // }

    private Claims getUsernameFromJwt(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        
        
        return claims;
    }

    private String generateJwtToken(Claims userClaims) {
        System.out.println(jwtSecret);
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(userClaims.getSubject())
                .claim("userId", userClaims.get("userId")) // Custom claim for userId
                .claim("roles", userClaims.get("roles"))   // Custom claim for roles
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}