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
package example.app.chat.jdbc;

import java.sql.Timestamp;

import org.cp.elements.jdbc.SqlType;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.lang.Nullable;

/**
 * The ChatTimestampSqlParameterSource class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public class ChatTimestampSqlParameterSource extends AbstractSqlParameterSource {

	private static final String TIMESTAMP_PARAMETER_NAME = "timestamp";

	private volatile Timestamp lastTimestamp;

	public ChatTimestampSqlParameterSource() {

		this.lastTimestamp = new Timestamp(System.currentTimeMillis());

		registerSqlType(TIMESTAMP_PARAMETER_NAME, SqlType.TIMESTAMP.getType());
	}

	@Override @SuppressWarnings("all")
	public boolean hasValue(String parameterName) {
		return TIMESTAMP_PARAMETER_NAME.equalsIgnoreCase(parameterName);
	}

	private Timestamp getAndUpdateTimestamp() {

		Timestamp timestamp = this.lastTimestamp;

		this.lastTimestamp = new Timestamp(System.currentTimeMillis());

		return timestamp;
	}

	@Nullable @Override @SuppressWarnings("all")
	public Object getValue(String parameterName) throws IllegalArgumentException {
		return hasValue(parameterName) ? getAndUpdateTimestamp() : null;
	}
}
