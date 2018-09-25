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
package example.app.function.server;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

import example.app.caching.model.Address;
import example.app.caching.repo.AddressRepository;

/**
 * The GeocodingFunctionServer class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@SuppressWarnings("unused")
public class GeocodingFunctionServer {

	public static void main(String[] args) {

		new SpringApplicationBuilder(GeocodingFunctionServer.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(AddressRepository addressRepository) {

		return args -> {

			addressRepository.save(Address.parse("1035 Pearl Street, Boulder, CO, 80302").identifiedBy(1L));
			addressRepository.save(Address.parse("15220 NW Greenbrier Parkway, Beaverton, OR, 97006").identifiedBy(2L));

		};
	}
}
