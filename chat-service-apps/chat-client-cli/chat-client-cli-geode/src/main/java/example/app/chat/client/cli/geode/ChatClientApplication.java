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
package example.app.chat.client.cli.geode;

import org.cp.elements.lang.Renderer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.geode.config.annotation.UseMemberName;

import example.app.chat.bot.ChatBot;
import example.app.chat.bot.config.EnableChatBot;
import example.app.chat.config.ApacheGeodeConfiguration;
import example.app.chat.model.Chat;
import example.app.chat.service.ChatService;
import example.app.chat.util.ChatRenderer;

/**
 * The {@link ChatClientApplication} class is a command-line, Chat Client application that sends {@link Chat Chats}
 * using {@link ChatBot ChatBots}.
 *
 * @author John Blum
 * @see org.springframework.boot.ApplicationRunner
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Import
 * @see example.app.chat.bot.ChatBot
 * @see example.app.chat.bot.config.EnableChatBot
 * @see example.app.chat.model.Chat
 * @see example.app.chat.service.ChatService
 * @see example.app.chat.config.ApacheGeodeConfiguration
 * @since 1.0.0
 */
@SpringBootApplication
@EnableChatBot
@Import(ApacheGeodeConfiguration.class)
@UseMemberName("ChatClientCliApplication")
@SuppressWarnings("unused")
public class ChatClientApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(ChatClientApplication.class)
			.web(WebApplicationType.NONE)
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
}
