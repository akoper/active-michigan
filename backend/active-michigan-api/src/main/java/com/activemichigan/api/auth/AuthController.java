package com.activemichigan.api.auth;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.activemichigan.api.users.AppUser;
import com.activemichigan.api.users.AppUserRepository;
import com.activemichigan.api.users.UserPrincipal;
import com.activemichigan.api.users.UserRole;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AppUserRepository users;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthController(
			AppUserRepository users,
			PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager,
			JwtService jwtService
	) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthDtos.AuthResponse register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
		if (users.existsByEmailIgnoreCase(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

		if (request.role() == UserRole.ADMIN) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot self-register as admin");
		}

		var user = new AppUser();
		user.setEmail(request.email().trim().toLowerCase());
		user.setDisplayName(request.displayName().trim());
		user.setRole(request.role());
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		var saved = users.save(user);

		String token = jwtService.generateToken(saved.getEmail(), saved.getRole().name());
		return new AuthDtos.AuthResponse(token, saved.getEmail(), saved.getDisplayName(), saved.getRole(), saved.getId());
	}

	@PostMapping("/login")
	public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest request) {
		try {
			var auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())
			);

			var principal = (UserPrincipal) auth.getPrincipal();
			var user = users.findByEmailIgnoreCase(principal.getUsername())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

			String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
			return new AuthDtos.AuthResponse(token, user.getEmail(), user.getDisplayName(), user.getRole(), user.getId());
		} catch (AuthenticationException ex) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
		}
	}

	@GetMapping("/me")
	public AuthDtos.AuthResponse getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
		var user = users.findById(principal.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return new AuthDtos.AuthResponse(null, user.getEmail(), user.getDisplayName(), user.getRole(), user.getId());
	}
}

