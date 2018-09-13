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

import example.app.chat.event.ChatEvent;
import example.app.chat.event.ChatListener;
import example.app.chat.model.Chat;
import example.app.chat.model.Person;

/**
 * The {@link ChatService} interface defines a contract for implementors to send and receive {@link Chat Chats}
 * written by a {@link Person}.
 *
 * The {@link Chat} class models the contents of a chat (such as {@link Person sender} and {@link String message}.
 * Minimally, the {@link Person} sending a {@link Chat} and the {@link String message} of the {@link Chat}
 * are required.
 *
 * The {@link ChatListener} defines the contract for receiving {@link Chat Chats} from other chat clients
 * and/or {@link Person people}.
 *
 * @author John Blum
 * @see example.app.chat.event.ChatEvent
 * @see example.app.chat.event.ChatListener
 * @see example.app.chat.model.Chat
 * @see example.app.chat.model.Person
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ChatService {

	/**
	 * Registers the {@link ChatListener} to receive {@link ChatEvent chat events}.
	 *
	 * @param chatListener {@link ChatListener} to register.
	 * @return a boolean value indicating whether the {@link ChatListener} was successfully registered.
	 * @see example.app.chat.event.ChatListener
	 */
	boolean register(ChatListener<?> chatListener);

	/**
	 * Sends the specified {@link String message} from the given {@link Person} in a {@link Chat}.
	 *
	 * @param person {@link Person} who is sending the {@link Chat}.
	 * @param message {@link String} containing the contents of the {@link Chat}.
	 * @see example.app.chat.model.Person
	 * @see java.lang.String
	 * @see #send(Chat)
	 */
	default void send(Person person, String message) {
		send(Chat.newChat(person, message));
	}

	/**
	 * Sends the given {@link Chat}.
	 *
	 * @param chat {@link Chat} to send.
	 * @see example.app.chat.model.Chat
	 */
	void send(Chat chat);

	/**
	 * Unregisters the {@link ChatListener} to stop receiving {@link ChatEvent chat events}.
	 *
	 * @param chatListener {@link ChatListener} to unregister.
	 * @return a boolean value indicating whether the {@link ChatListener} was successfully unregistered.
	 * @see example.app.chat.event.ChatListener
	 */
	boolean unregister(ChatListener<?> chatListener);

}
