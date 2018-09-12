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

package example.app.chat.service.provider.geode;

import java.util.Optional;

import org.apache.geode.cache.query.CqEvent;
import org.springframework.context.annotation.Primary;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.stereotype.Service;

import example.app.chat.event.ChatEvent;
import example.app.chat.repo.ChatRepository;
import example.app.chat.service.AbstractChatService;
import example.app.chat.service.ChatService;

/**
 * The {@link ApacheGeodeChatService} class is an implementation of the {@link ChatService} backed by Apache Geode.
 *
 * @author John Blum
 * @see example.app.chat.repo.ChatRepository
 * @see example.app.chat.service.AbstractChatService
 * @see example.app.chat.service.ChatService
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Primary
@Service("ApacheGeodeChatService")
public class ApacheGeodeChatService extends AbstractChatService {

	public ApacheGeodeChatService(ChatRepository chatRepository) {
		super(chatRepository);
	}

	@ContinuousQuery(name = "ChatReceiver", query = "SELECT * FROM /Chats")
	@SuppressWarnings("unchecked")
	public void receive(CqEvent cqEvent) {

		Optional.ofNullable(cqEvent)
			.map(CqEvent::getNewValue)
			.map(chat -> ChatEvent.newChatEvent(this).with(chat))
			.ifPresent(this::fire);
	}
}
