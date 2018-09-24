package io.springoneplatform8.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@SpringBootApplication
public class ChatWebappDbApplication implements CommandLineRunner {
	
	@Autowired
	private SimpMessageSendingOperations sender;

	public static void main(String[] args) {
		SpringApplication.run(ChatWebappDbApplication.class, args);
	}
	
	   @Override
	    public void run(String... args) throws Exception {
		   for (int i = 0; i < 100; i++) {
			   //send 100 messages
			   sender.convertAndSend("/topic/messages", new Chat("name " + i, "message " + i));
		   }
	    }
	
	class Chat {
		private String name;
		private String message;
		public Chat(String name, String message) {
			this.name = name;
			this.message = message;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	}
}
