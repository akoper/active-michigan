package com.activemichigan.api.users;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/users")
public class UserController {
	private final AppUserRepository users;
	private final PasswordEncoder passwordEncoder;

	public UserController(AppUserRepository users, PasswordEncoder passwordEncoder) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public List<UserResponse> listUsers() {
		return users.findAll().stream().map(UserResponse::fromEntity).toList();
	}

	@GetMapping("/{id}")
	public UserResponse getUser(@PathVariable Long id) {
		var user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return UserResponse.fromEntity(user);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
		if (users.existsByEmailIgnoreCase(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

		var user = new AppUser();
		user.setEmail(request.email().trim().toLowerCase());
		user.setDisplayName(request.displayName().trim());
		user.setRole(request.role());
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		return UserResponse.fromEntity(users.save(user));
	}

	@PutMapping("/{id}")
	public UserResponse updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
		var user = users.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (!user.getEmail().equalsIgnoreCase(request.email())
				&& users.existsByEmailIgnoreCase(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
		}

		user.setEmail(request.email().trim().toLowerCase());
		user.setDisplayName(request.displayName().trim());
		user.setRole(request.role());
		if (request.password() != null && !request.password().isBlank()) {
			user.setPasswordHash(passwordEncoder.encode(request.password()));
		}

		return UserResponse.fromEntity(users.save(user));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Long id) {
		if (!users.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		users.deleteById(id);
	}

	public record UserResponse(Long id, String email, String displayName, UserRole role) {
		static UserResponse fromEntity(AppUser user) {
			return new UserResponse(user.getId(), user.getEmail(), user.getDisplayName(), user.getRole());
		}
	}

	public record CreateUserRequest(
			@NotBlank @Email String email,
			@NotBlank @Size(min = 8, max = 120) String password,
			@NotBlank @Size(max = 100) String displayName,
			@NotNull UserRole role
	) {
	}

	public record UpdateUserRequest(
			@NotBlank @Email String email,
			@Size(min = 8, max = 120) String password,
			@NotBlank @Size(max = 100) String displayName,
			@NotNull UserRole role
	) {
	}
}

