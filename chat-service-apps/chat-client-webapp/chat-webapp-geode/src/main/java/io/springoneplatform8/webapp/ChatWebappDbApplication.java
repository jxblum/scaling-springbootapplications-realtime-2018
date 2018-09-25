package io.springoneplatform8.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChatWebappDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatWebappDbApplication.class, args);
	}

}
