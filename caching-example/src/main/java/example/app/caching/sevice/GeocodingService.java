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
package example.app.caching.sevice;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import example.app.caching.model.Address;
import example.app.caching.repo.GeocodingRepository;

/**
 * The GeocodingService class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Service
@SuppressWarnings("unused")
public class GeocodingService {

	private final AtomicBoolean cacheMiss = new AtomicBoolean(false);

	private final GeocodingRepository geocodingRepository;

	public GeocodingService(GeocodingRepository geocodingRepository) {

		Assert.notNull(geocodingRepository, "GeocodingRepository is required");

		this.geocodingRepository = geocodingRepository;
	}

	public boolean wasCacheMiss() {
		return this.cacheMiss.compareAndSet(true, false);
	}

	@Cacheable("AddressToLocation")
	public Point geocode(Address address) {
		this.cacheMiss.set(true);
		return this.geocodingRepository.geocode(address);
	}

	public Address reverseGeocode(Point location) {
		this.cacheMiss.set(true);
		return this.geocodingRepository.reverseGeocode(location);
	}
}
