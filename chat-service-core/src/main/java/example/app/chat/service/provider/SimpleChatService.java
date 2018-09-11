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

package example.app.chat.service.provider;

import example.app.chat.model.Chat;
import example.app.chat.repo.ChatRepository;
import example.app.chat.service.AbstractChatService;
import example.app.chat.service.ChatService;

/**
 * The {@link SimpleChatService} class is an implementation of {@link ChatService}, which is only capable of
 * sending {@link Chat Chats}.
 *
 * @author John Blum
 * @see example.app.chat.model.Chat
 * @see example.app.chat.repo.ChatRepository
 * @see example.app.chat.service.AbstractChatService
 * @see example.app.chat.service.ChatService
 * @since 1.0.0
 */
public class SimpleChatService extends AbstractChatService {

	public SimpleChatService(ChatRepository chatRepository) {
		super(chatRepository);
	}
}
