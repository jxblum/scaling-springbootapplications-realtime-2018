package io.springoneplatform8.webapp;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
	
	private MessageService messageService;
	
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@MessageMapping("/message")
	public void postMessage(@Payload Message message) {
		messageService.writeMessage(message);
	}
	
	@SubscribeMapping("/gethistory")
	public List<Message> getHistory() {
		return messageService.checkForExistingMessagesBeforeListening();
		
	}
}
