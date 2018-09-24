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
package example.app.chat.client.cli.database;

import org.cp.elements.lang.Renderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import example.app.chat.bot.ChatBot;
import example.app.chat.bot.config.EnableChatBot;
import example.app.chat.config.DatabaseConfiguration;
import example.app.chat.model.Chat;
import example.app.chat.service.ChatService;
import example.app.chat.service.provider.database.DatabaseChatService;
import example.app.chat.util.ChatRenderer;

/**
 * The {@link DatabaseChatClientApplication} class is a command-line, Chat Client application that sends {@link Chat Chats}
 * using {@link ChatBot ChatBots} and persisting {@link Chat Chats} to a backend database.
 *
 * @author John Blum
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Import
 * @see org.springframework.web.bind.annotation.RestController
 * @see example.app.chat.bot.ChatBot
 * @see example.app.chat.bot.config.EnableChatBot
 * @see example.app.chat.config.DatabaseConfiguration
 * @see example.app.chat.model.Chat
 * @see example.app.chat.service.ChatService
 * @since 1.0.0
 */
@SpringBootApplication
@Import(DatabaseConfiguration.class)
@EnableChatBot
@SuppressWarnings("unused")
public class DatabaseChatClientApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(DatabaseChatClientApplication.class)
			.build()
			.run(args);
	}

	@Bean
	ApplicationRunner runner(ChatService chatService) {

		return args -> chatService.register(chatEvent -> chatEvent.getChat()
			.filter(Chat.class::isInstance)
			.map(Chat.class::cast)
			.map(chatRenderer()::render)
			.ifPresent(System.out::println));
	}

	@Bean
	Renderer<Chat> chatRenderer() {
		return new ChatRenderer();
	}

	@RestController
	static class WebService {

		@Autowired
		private DatabaseChatService chatService;

		@GetMapping("/chats/count")
		Long chatCount() {
			return this.chatService.chatCount();
		}

		@GetMapping("/chats/one")
		Chat chatFirst() {
			return this.chatService.findOne();
		}

		@GetMapping("/ping")
		String ping() {
			return "PONG";
		}
	}
}
