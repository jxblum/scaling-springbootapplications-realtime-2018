package io.springoneplatform8.webapp;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
	
	private MessageService messageService;
	
	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	@PostMapping("/message")
	public void postMessage(@ModelAttribute Message message) {
		messageService.writeMessage(message);
	}





}
