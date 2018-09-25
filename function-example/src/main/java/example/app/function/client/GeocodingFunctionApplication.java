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

package example.app.function.client;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

import example.app.caching.model.Address;
import example.app.function.client.execution.GeocodingFunctionExecution;

/**
 * The GeocodingFunctionApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = Address.class)
@SuppressWarnings("unused")
public class GeocodingFunctionApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(GeocodingFunctionApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(GeocodingFunctionExecution geocodingFunction) {

		return args -> {

			System.err.println("Geocoding addressess...");

			geocodingFunction.geocodeAddresses();

			System.err.println("Done");
		};
	}
}
