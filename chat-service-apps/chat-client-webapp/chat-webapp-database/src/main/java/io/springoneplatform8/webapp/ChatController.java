package io.springoneplatform8.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import example.app.chat.model.Chat;

@Controller
public class ChatController {
	
	@PostMapping
	public void postChat(Chat chat) {
		
	}

}
