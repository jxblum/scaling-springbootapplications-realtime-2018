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

package example.app.function.server.config;

import org.apache.geode.cache.GemFireCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.geo.Point;

import example.app.caching.model.Address;
import example.app.caching.repo.AddressRepository;
import example.app.caching.repo.GeocodingRepository;

/**
 * The GeodeConfiguration class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Configuration
@CacheServerApplication(name = "GeocodingFunctionServer")
@EnableManager(start = true)
@EnableLocator
@EnableEntityDefinedRegions(basePackageClasses = Address.class)
@EnableGemfireRepositories(basePackageClasses = AddressRepository.class)
@ComponentScan(basePackageClasses = GeocodingRepository.class)
@SuppressWarnings("unused")
public class GeodeConfiguration {

	@Bean("AddressToLocation")
	PartitionedRegionFactoryBean<Address, Point> addressToLocationRegion(GemFireCache gemfireCache) {

		PartitionedRegionFactoryBean<Address, Point> addressToLocationRegion =
			new PartitionedRegionFactoryBean<>();

		addressToLocationRegion.setCache(gemfireCache);
		addressToLocationRegion.setClose(false);
		addressToLocationRegion.setPersistent(false);

		return addressToLocationRegion;
	}
}
