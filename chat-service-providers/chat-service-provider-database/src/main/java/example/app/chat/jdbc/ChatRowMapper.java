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

import static org.springframework.data.gemfire.util.RuntimeExceptionFactory.newIllegalArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import example.app.chat.model.Chat;
import example.app.chat.model.Person;
import example.app.chat.repo.PersonRepository;

/**
 * The ChatRowMapper class...
 *
 * @author John Blum
 * @since 1.0.0
 */
public class ChatRowMapper implements RowMapper<Chat> {

	private final PersonRepository personRepository;

	public ChatRowMapper(PersonRepository personRepository) {

		Assert.notNull(personRepository, "PersonRepository is required");

		this.personRepository = personRepository;
	}

	protected PersonRepository getPersonRepository() {
		return this.personRepository;
	}

	@Nullable @Override @SuppressWarnings("all")
	public Chat mapRow(ResultSet resultSet, int index) throws SQLException {

		long personId = resultSet.getLong("person_id");
		long timestamp = resultSet.getTimestamp("timestamp").getTime();

		Person person = getPersonRepository().findById(personId)
			.orElseThrow(() -> newIllegalArgumentException("Person with ID [%s] not found"));

		String message = resultSet.getString("message");

		String chatId = resultSet.getString("id");

		return Chat.newChat(person, message)
			.at(toLocalDateTime(timestamp))
			.identifiedBy(chatId);
	}

	private LocalDateTime toLocalDateTime(long epochMilliseconds) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilliseconds), ZoneId.systemDefault());
	}
}
