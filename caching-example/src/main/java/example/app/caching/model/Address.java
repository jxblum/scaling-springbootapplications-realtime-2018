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
package example.app.caching.model;

import org.springframework.util.Assert;

import lombok.Data;

/**
 * The Address class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Data
@SuppressWarnings("unused")
public class Address {

	private String street;

	private String city;

	private State state;

	private String zip;


	public static Address parse(String address) {

		Assert.hasText(address, "Address is required");

		String[] addressElements = address.split(",");

		Assert.isTrue(addressElements.length == 4,
			String.format("Address [%s] consist of street, city, state, zip", address));

		return new Address()
			.on(addressElements[0])
			.in(addressElements[1])
			.in(State.valueOfAbbreviation(addressElements[2]))
			.with(addressElements[3]);
	}

	public Address on(String street) {
		setStreet(street);
		return this;
	}

	public Address in(String city) {
		setCity(city);
		return this;
	}

	public Address in(State state) {
		setState(state);
		return this;
	}

	public Address with(String zip) {
		setZip(zip);
		return this;
	}
}
