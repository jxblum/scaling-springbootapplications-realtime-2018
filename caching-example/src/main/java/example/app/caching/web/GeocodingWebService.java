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
package example.app.caching.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.app.caching.model.Address;
import example.app.caching.model.GeocodingResult;
import example.app.caching.sevice.GeocodingService;

/**
 * The GeocodingWebService class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RestController
@Profile("geocoding-webservice")
@SuppressWarnings("unused")
public class GeocodingWebService {

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
