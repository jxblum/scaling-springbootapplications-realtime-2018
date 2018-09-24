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

import example.app.chat.event.ChatEventPublisher;
import example.app.chat.model.Chat;
import example.app.chat.repo.ChatRepository;

/**
 * The {@link AbstractChatService} class is an abstract base class implementing the {@link ChatService} interface
 * in order to provide operations common to all {@link ChatService} implementations used to send and optionally,
 * receive {@link Chat Chats}.
 *
 * @author John Blum
 * @see example.app.chat.event.ChatEventPublisher
 * @see example.app.chat.model.Chat
 * @see example.app.chat.repo.ChatRepository
 * @see example.app.chat.service.ChatService
 * @since 1.0.0
 */
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
	 * Sends the {@link Chat} using the Chat Service application client.
	 *
	 * @param chat {@link Chat} to send.
	 * @see example.app.chat.model.Chat
	 */
	public void send(Chat chat) {
		getChatRepository().save(chat);
	}
}
