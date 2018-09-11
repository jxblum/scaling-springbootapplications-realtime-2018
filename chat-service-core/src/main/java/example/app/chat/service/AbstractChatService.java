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
 * @see org.springframework.stereotype.Service
 * @see example.app.chat.event.ChatEventPublisher
 * @see example.app.chat.model.Chat
 * @see example.app.chat.model.Person
 * @see example.app.chat.repo.ChatRepository
 * @see example.app.chat.service.ChatService
 * @since 1.0.0
 */
@Service
@SuppressWarnings("unused")
public abstract class AbstractChatService extends ChatEventPublisher implements ChatService {

	private final ChatRepository chatRepository;

	/**
	 * Constructs a new instance of {@link AbstractChatService} initialized with the {@link ChatRepository},
	 * which is used to persist and access {@link Chat Chats}.
	 *
	 * @param chatRepository {@link ChatRepository} used to persist and access {@link Chat Chats}.
	 * @throws IllegalArgumentException if {@link ChatRepository} is {@literal null}.
	 * @see example.app.chat.repo.ChatRepository
	 */
	public AbstractChatService(ChatRepository chatRepository) {

		Assert.notNull(chatRepository, "ChatRepository is required");

		this.chatRepository = chatRepository;
	}

	/**
	 * Return a reference to the configured {@link ChatRepository} for accessing {@link Chat Chats}.
	 *
	 * @return a reference to the configured {@link ChatRepository} for accessing {@link Chat Chats}.
	 * @see example.app.chat.repo.ChatRepository
	 */
	protected ChatRepository getChatRepository() {
		return this.chatRepository;
	}

	/**
	 * Returns all {@link Chat chats} from all {@link Person people} ever recorded in the Chat Service application.
	 *
	 * @return an {@link Iterable} over the available {@link Chat chats} ever recorded by all {@link Person users}
	 * of the Chat Service application.
	 * @see example.app.chat.model.Chat
	 * @see java.lang.Iterable
	 */
	@Override
	public Iterable<Chat> findAll() {
		return getChatRepository().findAll();
	}

	/**
	 * Returns all {@link Chat chats} recorded by the given {@link Person}.
	 *
	 * @param person {@link Person} who is the subject of the search.
	 * @return all {@link Chat chats} recorded by the given {@link Person}.
	 * @see example.app.chat.model.Chat
	 * @see example.app.chat.model.Person
	 * @see java.lang.Iterable
	 */
	@Override
	public Iterable<Chat> findBy(Person person) {
		return getChatRepository().findByPerson(person);
	}

	/**
	 * Sends the {@link Chat} using the Chat Service application client.
	 *
	 * @param chat {@link Chat} to send.
	 * @see example.app.chat.model.Chat
	 */
	public void send(Chat chat) {
		getChatRepository().save(chat);
	}
}
