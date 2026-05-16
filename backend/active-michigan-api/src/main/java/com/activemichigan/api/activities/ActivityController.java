package com.activemichigan.api.activities;

import java.time.Instant;

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

	public ActivityController(ActivityRepository repo) {
		this.repo = repo;
	}

	@GetMapping
	public Page<Activity> search(
			@RequestParam(required = false) String q,
			@RequestParam(required = false) ActivityType type,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String region,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
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
						to
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
	public Activity create(@Valid @RequestBody Activity body) {
		body.setId(null);
		return repo.save(body);
	}

	@PutMapping("/{id}")
	public Activity update(@PathVariable long id, @Valid @RequestBody Activity body) {
		var existing = repo.findById(id).orElseThrow(() -> new ActivityNotFoundException(id));

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
	public void delete(@PathVariable long id) {
		if (!repo.existsById(id)) {
			throw new ActivityNotFoundException(id);
		}
		repo.deleteById(id);
	}
}

