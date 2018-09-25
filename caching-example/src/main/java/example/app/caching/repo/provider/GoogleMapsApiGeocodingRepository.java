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

import java.util.Arrays;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import example.app.caching.model.Address;
import example.app.caching.model.State;
import example.app.caching.repo.GeocodingRepository;

/**
 * The GoogleMapsApiGeocodingRepository class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Profile("google-api-maps-geocoding")
@Repository("GoogleGeocodingRepository")
@SuppressWarnings("unused")
public class GoogleMapsApiGeocodingRepository implements GeocodingRepository {

	@Value("${google.apis.maps.geocoding.key}")
	private String apiKey;

	private GeoApiContext newGeoApiContext() {
		return new GeoApiContext.Builder()
			.apiKey(this.apiKey)
			.disableRetries()
			//.maxRetries(2)
			.build();
	}

	@Override
	public Point geocode(Address address) {

		String stringAddress = toString(address);

		try {

			GeoApiContext context = newGeoApiContext();

			GeocodingResult[] results = GeocodingApi.geocode(context, stringAddress).await();

			if (!ObjectUtils.isEmpty(results)) {

				GeocodingResult result = results[0];

				return new Point(result.geometry.location.lat, result.geometry.location.lng);
			}

			throw new IllegalArgumentException(String.format(
				"Geographic coordinates (latitude/longitude) for address [%s] not found", stringAddress));
		}
		catch (Exception cause) {
			throw new IllegalArgumentException(
				String.format("Failed to convert address [%s] into latitude/longitude", stringAddress), cause);
		}
	}

	private String toString(Address address) {

		return String.format("%1$s, %2$s, %3$s %4$s",
			address.getStreet(), address.getCity(), address.getState(), address.getZip());
	}

	@Override
	public Address reverseGeocode(Point location) {

		try {

			GeoApiContext context = newGeoApiContext();

			GeocodingResult[] results = GeocodingApi.reverseGeocode(context, toLatitudeLongitude(location)).await();

			if (!ObjectUtils.isEmpty(results)) {

				GeocodingResult result = results[0];

				return toAddress(result.addressComponents);
			}

			throw new IllegalArgumentException(String.format("Address for latitude/longitude [%s] not found", location));
		}
		catch (Exception cause) {
			throw new IllegalArgumentException(
				String.format("Failed to reverse geocode latitude/longitude [%s] into a physical address",
					location), cause);
		}
	}

	private LatLng toLatitudeLongitude(Point point) {
		return new LatLng(point.getX(), point.getY());
	}

	private Address toAddress(AddressComponent[] addressComponents) {

		String street = findAddressComponent(addressComponents, AddressComponentType.STREET_NUMBER)
			.concat(" ").concat(findAddressComponent(addressComponents, AddressComponentType.ROUTE));

		String city = findAddressComponent(addressComponents, AddressComponentType.LOCALITY);

		State state = State.valueOfName(findAddressComponent(addressComponents,
			AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_1));

		String zipCode = findAddressComponent(addressComponents, AddressComponentType.POSTAL_CODE);

		return new Address().on(street).in(city).in(state).with(zipCode);
	}

	private String findAddressComponent(AddressComponent[] addressComponents,
		AddressComponentType... componentTypes) {

		for (AddressComponent addressComponent : addressComponents) {
			if (Arrays.stream(addressComponent.types)
				.anyMatch(element -> Arrays.asList(componentTypes).contains(element))) {

				return addressComponent.longName;
			}
		}

		throw new IllegalStateException(String.format("no address component of types [%s] found",
			Arrays.toString(componentTypes)));
	}
}
