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

import org.springframework.data.geo.Point;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The GeocodingResult class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@RequiredArgsConstructor(staticName = "of")
public class GeocodingResult {

	@NonNull
	private final Address address;

	@NonNull
	private final Point location;

	private Long time;

	private Boolean cacheMiss;

	public GeocodingResult withCacheMiss(boolean cacheMiss) {
		this.cacheMiss = cacheMiss;
		return this;
	}

	public GeocodingResult withTime(long time) {
		this.time = time;
		return this;
	}
}
