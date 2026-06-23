package com.parking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility component for JWT token generation and validation.
 * <p>
 * Provides low-level JWT operations using the JJWT library.
 * Tokens include the username as subject and role as a claim,
 * with a configurable expiration period. Note: This is a
 * simpler implementation; {@link JwtService} is the primary
 * JWT service used by the application.
 * </p>
 *
 * @author Team Smart Parking
 * @version 1.0
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing JWT tokens. Must be at least 256 bits for HMAC-SHA256.
     */
    private final String SECRET = "mySecretKeyMySecretKeyMySecretKey123456";

    /**
     * Token expiration time in milliseconds (1 hour).
     */
    private final long EXPIRATION = 1000 * 60 * 60;

    /**
     * Derives the HMAC signing key from the secret string.
     *
     * @return the signing key
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates a JWT token for the authenticated user.
     *
     * @param username the user's username
     * @param role     the user's role (e.g., ROLE_ADMIN, ROLE_CUSTOMER)
     * @return a signed JWT token string
     */
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username (subject) from a JWT token.
     *
     * @param token the JWT token
     * @return the username embedded in the token
     */
    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates whether a JWT token is correctly signed and not expired.
     *
     * @param token the JWT token to validate
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean validateToken(String token) {

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}