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

package example.app.caching.repo.provider;

import org.cp.elements.lang.concurrent.ThreadUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;

import example.app.caching.model.Address;
import example.app.caching.model.State;
import example.app.caching.repo.GeocodingRepository;

/**
 * The MockGeocodingRepository class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Profile("mock-geocoding")
@Repository("MockGeocodingRepository")
@SuppressWarnings("unused")
public class MockGeocodingRepository implements GeocodingRepository {

	@Override
	public Point geocode(Address address) {
		ThreadUtils.sleep(2000, 0);
		return new Point(37.781842d, -122.404291);
	}

	@Override
	public Address reverseGeocode(Point location) {
		ThreadUtils.sleep(5000, 0);
		return new Address().on("875 Howard Street").in("San Francisco").in(State.CALIFORNIA).with("94103");
	}
}
