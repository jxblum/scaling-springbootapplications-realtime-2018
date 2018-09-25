package io.springoneplatform8.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
	
	private SimpMessageSendingOperations sender;
	
	private MessageRepo messageRepo;
	
	public MessageService(SimpMessageSendingOperations sender, MessageRepo messageRepo) {
		this.sender = sender;
		this.messageRepo = messageRepo;
	}

	public void writeMessage(Message message) {
		message.setId(new Date().getTime());
		message.setCreationDateTime(new Date());
		messageRepo.save(message);
	}
	
	@Scheduled(fixedRate = 5000)
	private void checkForMessages() {
		List<Message> messages = messageRepo.findAll();
		if (!messages.isEmpty()) {
			for (Message message: messages) {
				sender.convertAndSend("/topic/message", message);
			}
		}
		
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
