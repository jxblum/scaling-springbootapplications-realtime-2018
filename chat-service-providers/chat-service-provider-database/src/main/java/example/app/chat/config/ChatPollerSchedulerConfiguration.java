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

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import example.app.chat.model.Chat;
import example.app.chat.repo.DatabaseChatRepository;
import example.app.chat.repo.PersonRepository;
import example.app.chat.service.provider.database.DatabaseChatService;

/**
 * The ChatPollerSchedulerConfiguration class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Configuration
@EnableScheduling
@Profile("chat-poller-scheduler")
@SuppressWarnings("unused")
class ChatPollerSchedulerConfiguration {

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
