package com.parking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

/**
 * Service for JWT token generation, parsing, and validation.
 * <p>
 * Reads the signing secret from application configuration with a
 * fallback default. Ensures the key meets the minimum 256-bit
 * requirement for HMAC-SHA256. Provides methods for generating
 * tokens with username and role claims, extracting claims from
 * tokens, and validating token expiry.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Service
public class JwtService {

    /**
     * Secret key for JWT signing, configurable via {@code jwt.secret} property.
     */
    @Value("${jwt.secret:my-secret-key-my-secret-key-my-secret-key-123456}")
    private String secret;

    /**
     * The HMAC signing key derived from the secret.
     */
    private Key key;

    /**
     * Initializes the signing key, padding to 256 bits if necessary.
     */
    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes();
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            System.arraycopy(keyBytes, 0, padded, 0, Math.min(keyBytes.length, 32));
            keyBytes = padded;
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT token with the username as subject and role as a claim.
     * Token expires after 1 hour.
     *
     * @param username the authenticated user's username
     * @param role     the user's role
     * @return a signed JWT token string
     */
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(
                        System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token
     * @return the username embedded in the token
     */
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Extracts the role claim from a JWT token.
     *
     * @param token the JWT token
     * @return the role (e.g., ROLE_ADMIN, ROLE_CUSTOMER)
     */
    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    /**
     * Validates whether a JWT token is still valid (not expired).
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid and not expired, {@code false} otherwise
     */
    public boolean isTokenValid(String token) {
        try {
            return getClaims(token)
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Parses and returns the claims body from a JWT token.
     *
     * @param token the JWT token
     * @return the claims contained in the token
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}