package io.springoneplatform8.webapp;

import java.util.List;

import org.springframework.data.gemfire.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Long> {
	
	@Query("select * from /Messages m where m.creationDateTime.getTime() > $1")
	List<Message> findByMessageAfter(Long timeStamp);
 
}
