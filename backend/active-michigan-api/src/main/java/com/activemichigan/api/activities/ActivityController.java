package com.activemichigan.api.activities;

import java.time.Instant;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import com.activemichigan.api.users.UserPrincipal;
import com.activemichigan.api.users.AppUser;
import com.activemichigan.api.users.AppUserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/activities")
@Validated
public class ActivityController {
	private final ActivityRepository repo;
	private final AppUserRepository userRepo;

	public ActivityController(ActivityRepository repo, AppUserRepository userRepo) {
		this.repo = repo;
		this.userRepo = userRepo;
	}

	@GetMapping
	public Page<Activity> search(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) ActivityType type,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String region,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
			@RequestParam(required = false) Long userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size
	) {
		var pageable = PageRequest.of(
				Math.max(page, 0),
				Math.min(Math.max(size, 1), 100),
				Sort.by(Sort.Direction.ASC, "startsAt")
		);

		return repo.findAll(
				ActivitySpecs.search(
						q,
						type,
						city,
						region,
						from,
						to,
						userId
				),
				pageable
		);
	}

	@GetMapping("/{id}")
	public Activity get(@PathVariable long id) {
		return repo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("isAuthenticated()")
	public Activity create(@Valid @RequestBody Activity body, Authentication auth) {
		body.setId(null);
		if (auth != null && auth.getPrincipal() instanceof UserPrincipal principal) {
			AppUser user = userRepo.findById(principal.getId()).orElseThrow();
			body.setUser(user);
		} else {
			throw new IllegalStateException("Authentication required to create activity");
		}
		return repo.save(body);
	}

	@PutMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public Activity update(@PathVariable long id, @Valid @RequestBody Activity body, Authentication auth) {
		var existing = repo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));

		if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
			throw new IllegalStateException("Authentication required");
		}

		if (existing.getUser() == null || !existing.getUser().getId().equals(principal.getId())) {
			throw new AccessDeniedException("Not authorized to update this activity");
		}

		existing.setTitle(body.getTitle());
		existing.setDescription(body.getDescription());
		existing.setType(body.getType());
		existing.setCity(body.getCity());
		existing.setRegion(body.getRegion());
		existing.setStartsAt(body.getStartsAt());
		existing.setEndsAt(body.getEndsAt());
		existing.setWebsiteUrl(body.getWebsiteUrl());

		return repo.save(existing);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("isAuthenticated()")
	public void delete(@PathVariable long id, Authentication auth) {
		var existing = repo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));

		if (auth == null || !(auth.getPrincipal() instanceof UserPrincipal principal)) {
			throw new IllegalStateException("Authentication required");
		}

		if (existing.getUser() == null || !existing.getUser().getId().equals(principal.getId())) {
			throw new AccessDeniedException("Not authorized to delete this activity");
		}

		repo.delete(existing);
	}
}

