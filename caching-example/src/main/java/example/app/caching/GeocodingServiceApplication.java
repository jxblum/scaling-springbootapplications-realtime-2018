/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package example.app.caching;

import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.geo.Point;
import org.springframework.geode.boot.autoconfigure.ContinuousQueryAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.app.caching.model.Address;
import example.app.caching.sevice.GeocodingService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The GeocodingServiceApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication(exclude = ContinuousQueryAutoConfiguration.class)
@EnableCachingDefinedRegions(clientRegionShortcut = ClientRegionShortcut.LOCAL)
@SuppressWarnings("unused")
public class GeocodingServiceApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(GeocodingServiceApplication.class)
			.web(WebApplicationType.SERVLET)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(GeocodingService geocodingService) {

		return args -> {

			runGeocodeOn(geocodingService, Address.parse("875 Howard Street, San Francisco, CA, 94103"));
			runGeocodeOn(geocodingService, Address.parse("15220 NW Greenbrier Parkway, Beaverton, OR, 97006"));
			runGeocodeOn(geocodingService, Address.parse("875 Howard Street, San Francisco, CA, 94103"));

		};
	}

	private void runGeocodeOn(GeocodingService geocodingService, Address address) {

		long t0 = System.currentTimeMillis();

		Point location = geocodingService.geocode(address);

		long t1 = System.currentTimeMillis();

		System.err.printf("%nAddress [%1$s], Location [%2$s], Cache Miss [%3$s], Time [%4$d]%n",
			address, location, geocodingService.wasCacheMiss(), t1 - t0);
	}

	@RestController
	@SuppressWarnings("unused")
	static class WebService {

		@Autowired
		private GeocodingService geocodingService;

		@GetMapping("/geode")
		public GeocodingResult geode(@RequestParam("address") String stringAddress) {

			Address address = Address.parse(stringAddress);

			long t0 = System.currentTimeMillis();

			Point location = this.geocodingService.geocode(address);

			long t1 = System.currentTimeMillis();

			return GeocodingResult.of(address, location)
				.withCacheMiss(this.geocodingService.wasCacheMiss())
				.withTime(t1 - t0);
		}

		@GetMapping("/ping")
		public String ping() {
			return "PONG";
		}
	}

	@RequiredArgsConstructor(staticName = "of")
	static class GeocodingResult {

		@NonNull
		Address address;

		@NonNull
		Point location;

		Long time;

		Boolean cacheMiss;

		GeocodingResult withCacheMiss(boolean cacheMiss) {
			this.cacheMiss = cacheMiss;
			return this;
		}

		GeocodingResult withTime(long time) {
			this.time = time;
			return this;
		}
	}
}
