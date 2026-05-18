package com.activemichigan.api.auth;

import com.activemichigan.api.users.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class AuthDtos {
	private AuthDtos() {
	}

	public record RegisterRequest(
			@NotBlank @Email String email,
			@NotBlank @Size(min = 8, max = 120) String password,
			@NotBlank @Size(max = 100) String displayName,
			@NotNull UserRole role
	) {
	}

	public record LoginRequest(
			@NotBlank @Email String email,
			@NotBlank String password
	) {
	}

	public record AuthResponse(
			String token,
			String email,
			String displayName,
			UserRole role,
			Long id
	) {
	}
}

