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

import example.app.chat.bot.config.EnableChatBot;
import example.app.chat.config.ApacheGeodeConfiguration;
import example.app.chat.model.Chat;
import example.app.chat.service.ChatService;
import example.app.chat.util.ChatRenderer;

/**
 * The ChatClientApplication class...
 *
 * @author John Blum
 * @since 1.0.0
 */
@SpringBootApplication
@EnableChatBot
@Import(ApacheGeodeConfiguration.class)
@UseMemberName("CliChatClientApplication")
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
			.map(chat -> (Chat) chat)
			.map(chatRenderer()::render)
			.ifPresent(System.out::println));
	}

	@Bean
	Renderer<Chat> chatRenderer() {
		return new ChatRenderer();
	}
}
