package io.springoneplatform8.webapp;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class NewClientConnectionDetector implements ApplicationListener<SessionSubscribeEvent> {

	private MessageService messageService;
	
	public NewClientConnectionDetector(MessageService messageService) {
		this.messageService = messageService;
	}

	@Override
	public void onApplicationEvent(SessionSubscribeEvent arg0) {
		messageService.checkForExistingMessagesBeforeListening();		
	}

}
