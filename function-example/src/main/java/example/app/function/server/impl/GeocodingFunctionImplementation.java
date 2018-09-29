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
package example.app.function.server.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.geode.cache.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.data.gemfire.function.annotation.RegionData;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import example.app.caching.model.Address;
import example.app.caching.repo.GeocodingRepository;

/**
 * The GeocodingFunctionImplementation class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Component
@SuppressWarnings("unused")
public class GeocodingFunctionImplementation {

	@Autowired
	private GeocodingRepository geocodingRepository;

	@Resource(name = "AddressToLocation")
	private Region<Address, Point> addressToLocation;

	@GemfireFunction
	public boolean geocodeAddresses(@RegionData Map<Long, Address> addresses) {

		addresses.values().forEach(address ->
			this.addressToLocation.put(address, this.geocodingRepository.geocode(address)));

		return true;
	}
}
