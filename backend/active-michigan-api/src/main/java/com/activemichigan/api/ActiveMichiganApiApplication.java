package com.activemichigan.api;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.activemichigan.api.activities.Activity;
import com.activemichigan.api.activities.ActivityRepository;
import com.activemichigan.api.activities.ActivityType;
import com.activemichigan.api.users.AppUser;
import com.activemichigan.api.users.AppUserRepository;
import com.activemichigan.api.users.UserRole;

@SpringBootApplication
public class ActiveMichiganApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActiveMichiganApiApplication.class, args);
	}

	@Bean
	public org.springframework.boot.CommandLineRunner seedActivities(ActivityRepository repository) {
		return args -> {
			if (repository.count() > 0) {
				return;
			}

			Activity annArborHalf = new Activity();
			annArborHalf.setTitle("Ann Arbor River Half Marathon");
			annArborHalf.setDescription("Scenic half marathon route along river trails.");
			annArborHalf.setType(ActivityType.RUN);
			annArborHalf.setCity("Ann Arbor");
			annArborHalf.setRegion("Southeast");
			annArborHalf.setStartsAt(Instant.now().plus(14, ChronoUnit.DAYS));
			annArborHalf.setWebsiteUrl("https://example.org/annarbor-half");

			Activity traverseBike = new Activity();
			traverseBike.setTitle("Traverse City Lakeshore Bike Race");
			traverseBike.setDescription("Open-road cycling event with beginner and advanced groups.");
			traverseBike.setType(ActivityType.BIKE);
			traverseBike.setCity("Traverse City");
			traverseBike.setRegion("Northwest");
			traverseBike.setStartsAt(Instant.now().plus(24, ChronoUnit.DAYS));
			traverseBike.setWebsiteUrl("https://example.org/traverse-bike");

			Activity marquetteTrail = new Activity();
			marquetteTrail.setTitle("Marquette Forest Trail Challenge");
			marquetteTrail.setDescription("Trail run and hike challenge across mixed terrain.");
			marquetteTrail.setType(ActivityType.HIKE);
			marquetteTrail.setCity("Marquette");
			marquetteTrail.setRegion("Upper Peninsula");
			marquetteTrail.setStartsAt(Instant.now().plus(30, ChronoUnit.DAYS));
			marquetteTrail.setWebsiteUrl("https://example.org/marquette-trail");

			Activity detroitTri = new Activity();
			detroitTri.setTitle("Detroit River Sprint Triathlon");
			detroitTri.setDescription("Sprint triathlon for first-timers and seasoned athletes.");
			detroitTri.setType(ActivityType.TRIATHLON);
			detroitTri.setCity("Detroit");
			detroitTri.setRegion("Southeast");
			detroitTri.setStartsAt(Instant.now().plus(45, ChronoUnit.DAYS));
			detroitTri.setWebsiteUrl("https://example.org/detroit-tri");

			Activity grandRapids5k = new Activity();
			grandRapids5k.setTitle("Grand Rapids Downtown 5K");
			grandRapids5k.setDescription("Fast and flat downtown 5K with community teams.");
			grandRapids5k.setType(ActivityType.RUN);
			grandRapids5k.setCity("Grand Rapids");
			grandRapids5k.setRegion("West");
			grandRapids5k.setStartsAt(Instant.now().plus(10, ChronoUnit.DAYS));
			grandRapids5k.setWebsiteUrl("https://example.org/gr5k");

			repository.save(annArborHalf);
			repository.save(traverseBike);
			repository.save(marquetteTrail);
			repository.save(detroitTri);
			repository.save(grandRapids5k);
		};
	}

	@Bean
	public org.springframework.boot.CommandLineRunner seedAdminUser(
			AppUserRepository users,
			PasswordEncoder passwordEncoder
	) {
		return args -> {
			if (users.existsByEmailIgnoreCase("admin@activemichigan.local")) {
				return;
			}

			var admin = new AppUser();
			admin.setEmail("admin@activemichigan.local");
			admin.setDisplayName("Active Michigan Admin");
			admin.setRole(UserRole.ADMIN);
			admin.setPasswordHash(passwordEncoder.encode("admin12345"));
			users.save(admin);
		};
	}
}
