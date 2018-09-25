package io.springoneplatform8.webapp;

import java.util.ArrayList;
import java.util.List;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	private SimpMessageSendingOperations sender;
	
	public MessageService(SimpMessageSendingOperations sender) {
		this.sender = sender;
	}

	public void send(Message message) {
		sender.convertAndSend("/topic/message", message);
		System.out.println("Sent message: " + message);
	}
	
	public String isUniqueUserName(String username) {
		return username;
	}
	
	public int getChatCount() {
		return 1;
	}
	
	public boolean isUserStillValid(String username) {
		return true;
	}
	
	public List<String> getAllUsers() {
		return new ArrayList<String>();
	}

}
