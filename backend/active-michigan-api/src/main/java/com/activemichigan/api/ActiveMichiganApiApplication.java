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

			Activity sleepingBearHike = new Activity();
			sleepingBearHike.setTitle("Sleeping Bear Dunes Hike");
			sleepingBearHike.setDescription("Guided hike through the beautiful Sleeping Bear Dunes.");
			sleepingBearHike.setType(ActivityType.HIKE);
			sleepingBearHike.setCity("Empire");
			sleepingBearHike.setRegion("Northwest");
			sleepingBearHike.setStartsAt(Instant.now().plus(5, ChronoUnit.DAYS));
			sleepingBearHike.setWebsiteUrl("https://example.org/sleeping-bear");
			repository.save(sleepingBearHike);

			Activity mackinacIslandBike = new Activity();
			mackinacIslandBike.setTitle("Mackinac Island Perimeter Bike Ride");
			mackinacIslandBike.setDescription("An 8-mile scenic bike ride around Mackinac Island.");
			mackinacIslandBike.setType(ActivityType.BIKE);
			mackinacIslandBike.setCity("Mackinac Island");
			mackinacIslandBike.setRegion("Upper Peninsula");
			mackinacIslandBike.setStartsAt(Instant.now().plus(12, ChronoUnit.DAYS));
			mackinacIslandBike.setWebsiteUrl("https://example.org/mackinac-bike");
			repository.save(mackinacIslandBike);

			Activity isleRoyalePaddle = new Activity();
			isleRoyalePaddle.setTitle("Isle Royale Kayak Expedition");
			isleRoyalePaddle.setDescription("A multi-day kayaking trip around Isle Royale National Park.");
			isleRoyalePaddle.setType(ActivityType.PADDLE);
			isleRoyalePaddle.setCity("Houghton");
			isleRoyalePaddle.setRegion("Upper Peninsula");
			isleRoyalePaddle.setStartsAt(Instant.now().plus(60, ChronoUnit.DAYS));
			isleRoyalePaddle.setWebsiteUrl("https://example.org/isle-royale");
			repository.save(isleRoyalePaddle);

			Activity boyneMountainSki = new Activity();
			boyneMountainSki.setTitle("Boyne Mountain Ski Weekend");
			boyneMountainSki.setDescription("Weekend ski trip at Boyne Mountain Resort.");
			boyneMountainSki.setType(ActivityType.SKI);
			boyneMountainSki.setCity("Boyne Falls");
			boyneMountainSki.setRegion("North");
			boyneMountainSki.setStartsAt(Instant.now().plus(200, ChronoUnit.DAYS));
			boyneMountainSki.setWebsiteUrl("https://example.org/boyne-ski");
			repository.save(boyneMountainSki);

			Activity kzooRiverPaddle = new Activity();
			kzooRiverPaddle.setTitle("Kalamazoo River Canoe Trip");
			kzooRiverPaddle.setDescription("Relaxing canoe trip down the Kalamazoo River.");
			kzooRiverPaddle.setType(ActivityType.PADDLE);
			kzooRiverPaddle.setCity("Kalamazoo");
			kzooRiverPaddle.setRegion("Southwest");
			kzooRiverPaddle.setStartsAt(Instant.now().plus(15, ChronoUnit.DAYS));
			kzooRiverPaddle.setWebsiteUrl("https://example.org/kzoo-paddle");
			repository.save(kzooRiverPaddle);

			Activity lansingCapitalRun = new Activity();
			lansingCapitalRun.setTitle("Lansing Capital City 10K");
			lansingCapitalRun.setDescription("10K run starting and ending at the State Capitol.");
			lansingCapitalRun.setType(ActivityType.RUN);
			lansingCapitalRun.setCity("Lansing");
			lansingCapitalRun.setRegion("Central");
			lansingCapitalRun.setStartsAt(Instant.now().plus(40, ChronoUnit.DAYS));
			lansingCapitalRun.setWebsiteUrl("https://example.org/lansing-10k");
			repository.save(lansingCapitalRun);

			Activity muskegonLakeBike = new Activity();
			muskegonLakeBike.setTitle("Muskegon Lake Loop Bike Ride");
			muskegonLakeBike.setDescription("Scenic bike ride around Muskegon Lake.");
			muskegonLakeBike.setType(ActivityType.BIKE);
			muskegonLakeBike.setCity("Muskegon");
			muskegonLakeBike.setRegion("West");
			muskegonLakeBike.setStartsAt(Instant.now().plus(22, ChronoUnit.DAYS));
			muskegonLakeBike.setWebsiteUrl("https://example.org/muskegon-bike");
			repository.save(muskegonLakeBike);

			Activity tahquamenonFallsHike = new Activity();
			tahquamenonFallsHike.setTitle("Tahquamenon Falls Trail Hike");
			tahquamenonFallsHike.setDescription("Hike between the Upper and Lower Tahquamenon Falls.");
			tahquamenonFallsHike.setType(ActivityType.HIKE);
			tahquamenonFallsHike.setCity("Paradise");
			tahquamenonFallsHike.setRegion("Upper Peninsula");
			tahquamenonFallsHike.setStartsAt(Instant.now().plus(35, ChronoUnit.DAYS));
			tahquamenonFallsHike.setWebsiteUrl("https://example.org/tahquamenon-hike");
			repository.save(tahquamenonFallsHike);

			Activity crystalMountainSki = new Activity();
			crystalMountainSki.setTitle("Crystal Mountain Downhill Skiing");
			crystalMountainSki.setDescription("Day trip for downhill skiing at Crystal Mountain.");
			crystalMountainSki.setType(ActivityType.SKI);
			crystalMountainSki.setCity("Thompsonville");
			crystalMountainSki.setRegion("Northwest");
			crystalMountainSki.setStartsAt(Instant.now().plus(180, ChronoUnit.DAYS));
			crystalMountainSki.setWebsiteUrl("https://example.org/crystal-ski");
			repository.save(crystalMountainSki);

			Activity auSableRiverPaddle = new Activity();
			auSableRiverPaddle.setTitle("Au Sable River Canoe Marathon");
			auSableRiverPaddle.setDescription("Watching or participating in the famous canoe marathon.");
			auSableRiverPaddle.setType(ActivityType.PADDLE);
			auSableRiverPaddle.setCity("Grayling");
			auSableRiverPaddle.setRegion("North");
			auSableRiverPaddle.setStartsAt(Instant.now().plus(70, ChronoUnit.DAYS));
			auSableRiverPaddle.setWebsiteUrl("https://example.org/ausable-paddle");
			repository.save(auSableRiverPaddle);

			Activity hollandTulipRun = new Activity();
			hollandTulipRun.setTitle("Holland Tulip Time 5K");
			hollandTulipRun.setDescription("Run through the tulips during the Tulip Time Festival.");
			hollandTulipRun.setType(ActivityType.RUN);
			hollandTulipRun.setCity("Holland");
			hollandTulipRun.setRegion("West");
			hollandTulipRun.setStartsAt(Instant.now().plus(365, ChronoUnit.DAYS));
			hollandTulipRun.setWebsiteUrl("https://example.org/holland-run");
			repository.save(hollandTulipRun);

			Activity copperHarborBike = new Activity();
			copperHarborBike.setTitle("Copper Harbor Mountain Bike Trails");
			copperHarborBike.setDescription("World-class mountain biking in the Keweenaw Peninsula.");
			copperHarborBike.setType(ActivityType.BIKE);
			copperHarborBike.setCity("Copper Harbor");
			copperHarborBike.setRegion("Upper Peninsula");
			copperHarborBike.setStartsAt(Instant.now().plus(50, ChronoUnit.DAYS));
			copperHarborBike.setWebsiteUrl("https://example.org/copper-harbor-bike");
			repository.save(copperHarborBike);

			Activity picturedRocksHike = new Activity();
			picturedRocksHike.setTitle("Pictured Rocks Backpacking Trip");
			picturedRocksHike.setDescription("Backpacking along the Lakeshore Trail at Pictured Rocks.");
			picturedRocksHike.setType(ActivityType.HIKE);
			picturedRocksHike.setCity("Munising");
			picturedRocksHike.setRegion("Upper Peninsula");
			picturedRocksHike.setStartsAt(Instant.now().plus(55, ChronoUnit.DAYS));
			picturedRocksHike.setWebsiteUrl("https://example.org/pictured-rocks-hike");
			repository.save(picturedRocksHike);

			Activity grandLedgeClimb = new Activity();
			grandLedgeClimb.setTitle("Grand Ledge Rock Climbing");
			grandLedgeClimb.setDescription("Outdoor rock climbing at Fitzgerald Park.");
			grandLedgeClimb.setType(ActivityType.OTHER);
			grandLedgeClimb.setCity("Grand Ledge");
			grandLedgeClimb.setRegion("Central");
			grandLedgeClimb.setStartsAt(Instant.now().plus(18, ChronoUnit.DAYS));
			grandLedgeClimb.setWebsiteUrl("https://example.org/grand-ledge-climb");
			repository.save(grandLedgeClimb);

			Activity frankenmuth10k = new Activity();
			frankenmuth10k.setTitle("Frankenmuth Bruckelaufe Half Marathon & 5K");
			frankenmuth10k.setDescription("Traditional German-themed race in Little Bavaria.");
			frankenmuth10k.setType(ActivityType.RUN);
			frankenmuth10k.setCity("Frankenmuth");
			frankenmuth10k.setRegion("East Central");
			frankenmuth10k.setStartsAt(Instant.now().plus(120, ChronoUnit.DAYS));
			frankenmuth10k.setWebsiteUrl("https://example.org/frankenmuth-run");
			repository.save(frankenmuth10k);

			Activity suttonsBayPaddle = new Activity();
			suttonsBayPaddle.setTitle("Suttons Bay Paddleboard Rental");
			suttonsBayPaddle.setDescription("Afternoon paddleboarding in the calm waters of Suttons Bay.");
			suttonsBayPaddle.setType(ActivityType.PADDLE);
			suttonsBayPaddle.setCity("Suttons Bay");
			suttonsBayPaddle.setRegion("Northwest");
			suttonsBayPaddle.setStartsAt(Instant.now().plus(8, ChronoUnit.DAYS));
			suttonsBayPaddle.setWebsiteUrl("https://example.org/suttons-bay-paddle");
			repository.save(suttonsBayPaddle);

			Activity nubsNobSki = new Activity();
			nubsNobSki.setTitle("Nub's Nob Cross-Country Skiing");
			nubsNobSki.setDescription("Groomed cross-country ski trails for all levels.");
			nubsNobSki.setType(ActivityType.SKI);
			nubsNobSki.setCity("Harbor Springs");
			nubsNobSki.setRegion("North");
			nubsNobSki.setStartsAt(Instant.now().plus(210, ChronoUnit.DAYS));
			nubsNobSki.setWebsiteUrl("https://example.org/nubs-nob-ski");
			repository.save(nubsNobSki);

			Activity grandHavenVolleyball = new Activity();
			grandHavenVolleyball.setTitle("Grand Haven Beach Volleyball Tournament");
			grandHavenVolleyball.setDescription("Summer beach volleyball tournament at City Beach.");
			grandHavenVolleyball.setType(ActivityType.OTHER);
			grandHavenVolleyball.setCity("Grand Haven");
			grandHavenVolleyball.setRegion("West");
			grandHavenVolleyball.setStartsAt(Instant.now().plus(65, ChronoUnit.DAYS));
			grandHavenVolleyball.setWebsiteUrl("https://example.org/grand-haven-vb");
			repository.save(grandHavenVolleyball);

			Activity stJosephRun = new Activity();
			stJosephRun.setTitle("St. Joseph Sunset 5K");
			stJosephRun.setDescription("Evening 5K run along the bluff overlooking Lake Michigan.");
			stJosephRun.setType(ActivityType.RUN);
			stJosephRun.setCity("St. Joseph");
			stJosephRun.setRegion("Southwest");
			stJosephRun.setStartsAt(Instant.now().plus(28, ChronoUnit.DAYS));
			stJosephRun.setWebsiteUrl("https://example.org/st-joseph-run");
			repository.save(stJosephRun);

			Activity isleRoyaleHike = new Activity();
			isleRoyaleHike.setTitle("Isle Royale Greenstone Ridge Hike");
			isleRoyaleHike.setDescription("A challenging 40-mile trek across the island.");
			isleRoyaleHike.setType(ActivityType.HIKE);
			isleRoyaleHike.setCity("Rock Harbor");
			isleRoyaleHike.setRegion("Upper Peninsula");
			isleRoyaleHike.setStartsAt(Instant.now().plus(90, ChronoUnit.DAYS));
			isleRoyaleHike.setWebsiteUrl("https://example.org/isle-royale-hike");
			repository.save(isleRoyaleHike);

			Activity houghtonLakeSnowmobile = new Activity();
			houghtonLakeSnowmobile.setTitle("Houghton Lake Snowmobile Trails");
			houghtonLakeSnowmobile.setDescription("Explore hundreds of miles of groomed snowmobile trails.");
			houghtonLakeSnowmobile.setType(ActivityType.OTHER);
			houghtonLakeSnowmobile.setCity("Houghton Lake");
			houghtonLakeSnowmobile.setRegion("Central");
			houghtonLakeSnowmobile.setStartsAt(Instant.now().plus(240, ChronoUnit.DAYS));
			houghtonLakeSnowmobile.setWebsiteUrl("https://example.org/houghton-lake-snow");
			repository.save(houghtonLakeSnowmobile);

			Activity traverseCityTri = new Activity();
			traverseCityTri.setTitle("IRONMAN 70.3 Traverse City");
			traverseCityTri.setDescription("Half IRONMAN triathlon in the cherry capital.");
			traverseCityTri.setType(ActivityType.TRIATHLON);
			traverseCityTri.setCity("Traverse City");
			traverseCityTri.setRegion("Northwest");
			traverseCityTri.setStartsAt(Instant.now().plus(150, ChronoUnit.DAYS));
			traverseCityTri.setWebsiteUrl("https://example.org/tc-tri");
			repository.save(traverseCityTri);

			Activity belleIsleBike = new Activity();
			belleIsleBike.setTitle("Belle Isle Loop Bike Ride");
			belleIsleBike.setDescription("Cycling around the perimeter of Detroit's island park.");
			belleIsleBike.setType(ActivityType.BIKE);
			belleIsleBike.setCity("Detroit");
			belleIsleBike.setRegion("Southeast");
			belleIsleBike.setStartsAt(Instant.now().plus(12, ChronoUnit.DAYS));
			belleIsleBike.setWebsiteUrl("https://example.org/belle-isle-bike");
			repository.save(belleIsleBike);

			Activity porcupineMountainsHike = new Activity();
			porcupineMountainsHike.setTitle("Porcupine Mountains Wilderness Hike");
			porcupineMountainsHike.setDescription("Hiking the Escarpment Trail for breathtaking views.");
			porcupineMountainsHike.setType(ActivityType.HIKE);
			porcupineMountainsHike.setCity("Ontonagon");
			porcupineMountainsHike.setRegion("Upper Peninsula");
			porcupineMountainsHike.setStartsAt(Instant.now().plus(42, ChronoUnit.DAYS));
			porcupineMountainsHike.setWebsiteUrl("https://example.org/porkies-hike");
			repository.save(porcupineMountainsHike);

			Activity ludingtonPaddle = new Activity();
			ludingtonPaddle.setTitle("Ludington State Park River Paddle");
			ludingtonPaddle.setDescription("Paddling down the Hamlin Lake to Lake Michigan river.");
			ludingtonPaddle.setType(ActivityType.PADDLE);
			ludingtonPaddle.setCity("Ludington");
			ludingtonPaddle.setRegion("West");
			ludingtonPaddle.setStartsAt(Instant.now().plus(32, ChronoUnit.DAYS));
			ludingtonPaddle.setWebsiteUrl("https://example.org/ludington-paddle");
			repository.save(ludingtonPaddle);
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
