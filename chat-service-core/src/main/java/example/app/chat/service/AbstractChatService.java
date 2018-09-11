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

package example.app.chat.service;

import org.apache.geode.cache.query.CqEvent;
import org.cp.elements.lang.Assert;
import org.springframework.stereotype.Service;

import example.app.chat.event.ChatEventPublisher;
import example.app.chat.model.Chat;
import example.app.chat.model.Person;
import example.app.chat.repo.ChatRepository;

/**
 * The {@link AbstractChatService} class is a Spring {@link Service} class implementing a chat service to send chats.
 *
 * @author John Blum
 * @see CqEvent
 * @see org.cp.elements.lang.IdentifierSequence
 * @see org.springframework.data.gemfire.listener.annotation.ContinuousQuery
 * @see org.springframework.stereotype.Service
 * @see example.chat.client.model.Chat
 * @see example.chat.client.repo.ChatRepository
 * @see example.chat.event.ChatEvent
 * @see example.chat.event.ChatEventPublisher
 * @see example.chat.service.ChatService
 * @since 1.0.0
 */
@Service
@SuppressWarnings("unused")
public abstract class AbstractChatService extends ChatEventPublisher implements ChatService {

	private final ChatRepository chatRepository;

	public AbstractChatService(ChatRepository chatRepository) {

		Assert.notNull(chatRepository, "ChatRepository is required");

		this.chatRepository = chatRepository;
	}

	protected ChatRepository getChatRepository() {
		return this.chatRepository;
	}

	@Override
	public Iterable<Chat> findAll() {
		return getChatRepository().findAll();
	}

	@Override
	public Iterable<Chat> findBy(Person person) {
		return getChatRepository().findByPerson(person);
	}

	public void send(Chat chat) {
		getChatRepository().save(chat);
	}
}
