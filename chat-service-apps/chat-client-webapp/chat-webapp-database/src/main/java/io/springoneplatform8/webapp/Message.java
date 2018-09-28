package io.springoneplatform8.webapp;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
class Message {
	
	@Id
	private Long id;
	private String userName;
	private String messageText;
    private Date creationDateTime;
}