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

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;
import org.springframework.jdbc.core.JdbcTemplate;

import example.app.chat.jdbc.ChatRowMapper;
import example.app.chat.jdbc.ChatTimestampSqlParameterSource;
import example.app.chat.model.Chat;
import example.app.chat.repo.PersonRepository;
import example.app.chat.service.provider.database.DatabaseChatService;

/**
 * The ChatIntegrationFlowConfiguration class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Configuration
@Profile("chat-integration-flow")
@SuppressWarnings("unused")
class ChatIntegrationFlowConfiguration {

	private static final String CHAT_POLLING_SQL =
		"SELECT * FROM chat_service.chats c WHERE c.received IS false";

	private static final String CHAT_UPDATE_SQL =
		"UPDATE chat_service.chats c SET c.received = true WHERE c.received IS false";

	@Bean
	MessageSource<Object> jdbcMessageSource(JdbcTemplate jdbcTemplate, PersonRepository personRepository) {

		JdbcPollingChannelAdapter channelAdapter =
			new JdbcPollingChannelAdapter(jdbcTemplate, CHAT_POLLING_SQL);

		//channelAdapter.setMaxRowsPerPoll(1);
		channelAdapter.setRowMapper(new ChatRowMapper(personRepository));
		channelAdapter.setSelectSqlParameterSource(new ChatTimestampSqlParameterSource());
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
