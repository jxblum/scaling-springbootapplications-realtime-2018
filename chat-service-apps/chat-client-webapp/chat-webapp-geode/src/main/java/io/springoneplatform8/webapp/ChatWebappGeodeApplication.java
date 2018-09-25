package io.springoneplatform8.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;

@SpringBootApplication
@EnableEntityDefinedRegions(basePackageClasses = Message.class)
public class ChatWebappGeodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatWebappGeodeApplication.class, args);
	}
}
