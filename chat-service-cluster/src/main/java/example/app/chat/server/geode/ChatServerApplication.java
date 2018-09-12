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
package example.app.chat.server.geode;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.server.CacheServer;
import org.apache.geode.distributed.Locator;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;

/**
 * The {@link ChatServerApplication} class is a Spring Boot application that configures and bootstraps
 * an Apache Geode {@link CacheServer cache server}, {@link Cache peer cache} application with
 * an embedded {@link Locator} and {@literal Manager} service.
 *
 * @author John Blum
 * @see org.apache.geode.cache.Cache
 * @see org.apache.geode.cache.server.CacheServer
 * @see org.apache.geode.distributed.Locator
 * @see org.springframework.boot.SpringApplication
 * @see org.springframework.boot.autoconfigure.SpringBootApplication
 * @see org.springframework.boot.builder.SpringApplicationBuilder
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.context.annotation.Profile
 * @see org.springframework.data.gemfire.config.annotation.CacheServerApplication
 * @see org.springframework.data.gemfire.config.annotation.EnableLocator
 * @see org.springframework.data.gemfire.config.annotation.EnableManager
 * @since 1.0.0
 */
@SpringBootApplication
@CacheServerApplication(name = "ChatServerApplication", locators = "localhost[10334]")
@SuppressWarnings("unused")
public class ChatServerApplication {

	public static void main(String[] args) {

		new SpringApplicationBuilder(ChatServerApplication.class)
			.web(WebApplicationType.NONE)
			.build()
			.run(args);
	}

	@Configuration
	@EnableLocator
	@EnableManager(start = true)
	@Profile("locator-manager")
	static class LocatorManagerConfiguration { }

}
