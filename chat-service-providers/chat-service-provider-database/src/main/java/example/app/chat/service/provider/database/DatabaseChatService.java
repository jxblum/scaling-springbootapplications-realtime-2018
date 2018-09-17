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

package example.app.chat.service.provider.database;

import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import example.app.chat.event.ChatEvent;
import example.app.chat.model.Chat;
import example.app.chat.repo.ChatRepository;
import example.app.chat.service.AbstractChatService;

/**
 * The DatabaseChatService class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@Service("DatabaseChatService")
@SuppressWarnings("unused")
public class DatabaseChatService extends AbstractChatService {

	public DatabaseChatService(ChatRepository chatRepository) {
		super(chatRepository);
	}

	public long chatCount() {
		return getChatRepository().count();
	}

	public Chat findOne() {

		return StreamSupport.stream(getChatRepository().findAll().spliterator(), false)
			.sorted()
			.findFirst()
			.orElse(null);
	}

	@SuppressWarnings("unchecked")
	public void receive(Chat chat) {

		Optional.ofNullable(chat)
			.<ChatEvent>map(ChatEvent.newChatEvent(this)::with)
			.ifPresent(this::fire);
	}
}
