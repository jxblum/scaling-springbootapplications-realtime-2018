package io.springoneplatform8.webapp;

class Message {
	private String userName;
	private String messageText;
	public Message(String userName) {
		this.userName = userName;
	}
	public Message() {
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	@Override
	public String toString() {
		return "Message [userName=" + userName + ", messageText=" + messageText + "]";
	}
}