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

package example.app.chat.config;

import static org.springframework.data.gemfire.util.RuntimeExceptionFactory.newIllegalArgumentException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.cp.elements.jdbc.SqlType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.AbstractSqlParameterSource;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.Assert;

import example.app.chat.model.Chat;
import example.app.chat.model.Person;
import example.app.chat.repo.DatabaseChatRepository;
import example.app.chat.repo.PersonRepository;
import example.app.chat.service.ChatService;
import example.app.chat.service.provider.database.DatabaseChatService;

/**
 * The DatabaseConfiguration class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Configuration
@Import(EmbeddedDatabaseConfiguration.class)
@EntityScan(basePackageClasses = Chat.class)
@EnableJpaRepositories(basePackageClasses = { DatabaseChatRepository.class, PersonRepository.class })
@ComponentScan(basePackageClasses = { ChatService.class })
@SuppressWarnings("unused")
public class DatabaseConfiguration {

	private static final String CHAT_POLLING_SQL =
		"SELECT * FROM chat_service.chats c WHERE c.received IS false";

	private static final String CHAT_UPDATE_SQL =
		"UPDATE chat_service.chats c SET c.received = true WHERE c.received IS false";

	@Configuration
	@Profile("chat-integration-flow")
	static class ChatIntegrationFlowConfiguration {

		@Bean
		MessageSource<Object> jdbcMessageSource(JdbcTemplate jdbcTemplate, PersonRepository personRepository) {

			JdbcPollingChannelAdapter channelAdapter =
				new JdbcPollingChannelAdapter(jdbcTemplate, CHAT_POLLING_SQL);

			//channelAdapter.setMaxRowsPerPoll(1);
			channelAdapter.setRowMapper(new ChatRowMapper(personRepository));
			//channelAdapter.setSelectSqlParameterSource(new ChatTimestampSqlParameterSource());
			channelAdapter.setUpdateSql(CHAT_UPDATE_SQL);

			return channelAdapter;
		}

		@Bean
		IntegrationFlow jdbcPollingIntegrationFlow(DatabaseChatService chatService, JdbcTemplate jdbcTemplate,
			PersonRepository personRepository) {

			return IntegrationFlows
				.from(jdbcMessageSource(jdbcTemplate, personRepository), channelAdapterSpec ->
					channelAdapterSpec.poller(Pollers.fixedRate(1, TimeUnit.SECONDS)))
				//.log(LoggingHandler.Level.DEBUG)
				//.handle("DatabaseChatService", "receive")
				.handle(message -> chatService.receive((Chat) message.getPayload()))
				.get();
		}
	}

	@Configuration
	@EnableScheduling
	@Profile("scheduled-chat-poller")
	static class ScheduledChatPollerConfiguration {

		@Autowired
		private DatabaseChatRepository databaseChatRepository;

		@Autowired
		private DatabaseChatService databaseChatService;

		private volatile LocalDateTime timestamp = LocalDateTime.now();
		private volatile LocalDateTime updatedTimestamp = this.timestamp;

		@Autowired
		private JdbcTemplate chatsTemplate;

		@Autowired
		private PersonRepository personRepository;

		protected LocalDateTime currentTimestamp() {

			LocalDateTime currentTimestamp = this.timestamp;

			this.updatedTimestamp = LocalDateTime.now();

			return currentTimestamp;
		}

		@Scheduled(fixedRateString = "${example.app.chat.receiver.schedule.rate:1000}")
		public void receiveChat() {

			//List<Chat> chats = this.chatsTemplate.query(CHAT_POLLING_SQL, new ChatRowMapper(this.personRepository));
			List<Chat> chats = this.databaseChatRepository.findChatByTimestampAfter(currentTimestamp());

			if (!chats.isEmpty()) {
				this.timestamp = this.updatedTimestamp;
				//this.chatsTemplate.update(CHAT_UPDATE_SQL);
			}

			chats.forEach(this.databaseChatService::receive);
		}
	}

	static class ChatRowMapper implements RowMapper<Chat> {

		private final PersonRepository personRepository;

		ChatRowMapper(PersonRepository personRepository) {

			Assert.notNull(personRepository, "PersonRepository is required");

			this.personRepository = personRepository;
		}

		PersonRepository getPersonRepository() {
			return personRepository;
		}

		@Nullable @Override @SuppressWarnings("all")
		public Chat mapRow(ResultSet resultSet, int index) throws SQLException {

			long personId = resultSet.getLong("person_id");

			Person person = getPersonRepository().findById(personId)
				.orElseThrow(() -> newIllegalArgumentException("Person with ID [%s] not found"));

			long epochMilliseconds = resultSet.getTimestamp("timestamp").getTime();

			return Chat.newChat(person, resultSet.getString("message"))
				.at(LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilliseconds), ZoneId.systemDefault()))
				.identifiedBy(resultSet.getString("id"));
		}
	}

	static class ChatTimestampSqlParameterSource extends AbstractSqlParameterSource {

		private volatile Timestamp lastTimestamp;

		ChatTimestampSqlParameterSource() {
			this.lastTimestamp = new Timestamp(System.currentTimeMillis());
			registerSqlType("timestamp", SqlType.TIMESTAMP.getType());
		}

		@Override @SuppressWarnings("all")
		public boolean hasValue(String parameterName) {
			return "timestamp".equalsIgnoreCase(parameterName);
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
}
