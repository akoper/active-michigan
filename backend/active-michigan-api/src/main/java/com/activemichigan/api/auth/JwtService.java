package com.activemichigan.api.auth;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	private final SecretKey key;
	private final long expirationMinutes;

	public JwtService(
			@Value("${app.jwt.secret}") String jwtSecret,
			@Value("${app.jwt.expiration-minutes}") long expirationMinutes
	) {
		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
		this.expirationMinutes = expirationMinutes;
	}

	public String generateToken(String subject, String role) {
		Instant now = Instant.now();
		return Jwts.builder()
				.subject(subject)
				.claim("role", role)
				.issuedAt(Date.from(now))
				.expiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
				.signWith(key)
				.compact();
	}

	public String extractSubject(String token) {
		return parseClaims(token).getSubject();
	}

	public boolean isValid(String token, UserDetails userDetails) {
		var claims = parseClaims(token);
		String subject = claims.getSubject();
		Date expiration = claims.getExpiration();
		return subject != null
				&& subject.equalsIgnoreCase(userDetails.getUsername())
				&& expiration != null
				&& expiration.toInstant().isAfter(Instant.now());
	}

	private Claims parseClaims(String token) {
		return Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}
}

