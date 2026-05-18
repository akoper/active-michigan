package com.activemichigan.api.activities;

import java.time.Instant;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public final class ActivitySpecs {
	private ActivitySpecs() {
	}

	public static Specification<Activity> search(
			String q,
			ActivityType type,
			String city,
			String region,
			Instant from,
			Instant to,
			Long userId
	) {
		return (root, query, cb) -> {
			var predicates = new java.util.ArrayList<Predicate>();

			if (q != null && !q.isBlank()) {
				String like = "%" + q.trim().toLowerCase() + "%";
				predicates.add(cb.or(
						cb.like(cb.lower(root.get("title")), like),
						cb.like(cb.lower(root.get("description")), like)
				));
			}

			if (type != null) {
				predicates.add(cb.equal(root.get("type"), type));
			}

			if (city != null && !city.isBlank()) {
				predicates.add(cb.equal(cb.lower(root.get("city")), city.trim().toLowerCase()));
			}

			if (region != null && !region.isBlank()) {
				predicates.add(cb.equal(cb.lower(root.get("region")), region.trim().toLowerCase()));
			}

			if (from != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("startsAt"), from));
			}
			if (to != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("startsAt"), to));
			}

			if (userId != null) {
				predicates.add(cb.equal(root.get("user").get("id"), userId));
			}

			return cb.and(predicates.toArray(Predicate[]::new));
		};
	}
}

