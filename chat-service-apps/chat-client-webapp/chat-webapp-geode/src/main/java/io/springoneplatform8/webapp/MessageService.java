package io.springoneplatform8.webapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.geode.cache.query.CqEvent;
import org.springframework.data.gemfire.listener.annotation.ContinuousQuery;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private SimpMessageSendingOperations sender;

	private MessageRepo messageRepo;

	public MessageService(SimpMessageSendingOperations sender, MessageRepo messageRepo) {
		this.sender = sender;
		this.messageRepo = messageRepo;
	}
	

	public void checkForExistingMessagesBeforeListening() {
		this.messageRepo.findAllMessagesAfter(0L)
			.forEach(message -> this.sender.convertAndSend("/topic/message", message));
	}

	public void writeMessage(Message message) {

		Date creationDateTime = new Date();

		message.setId(creationDateTime.getTime());
		message.setCreationDateTime(creationDateTime);

		this.messageRepo.save(message);
	}

	@ContinuousQuery(name = "ChatReceiver", query = "SELECT * FROM /Messages")
	public void receive(CqEvent cqEvent) {

		Optional.ofNullable(cqEvent)
			.map(CqEvent::getNewValue)
			.filter(Message.class::isInstance)
			.map(Message.class::cast)
			.ifPresent(message -> this.sender.convertAndSend("/topic/message", message));
	}

	public long getChatCount() {
		return this.messageRepo.count();
	}

	public String isUniqueUserName(String username) {
		return username;
	}

	public boolean isUserStillValid(String username) {
		return true;
	}

	public List<String> getAllUsers() {
		return new ArrayList<String>();
	}
}
