package io.springoneplatform8.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 
 * @author luke
 *
 */
@Controller
public class ChatLoginController {
	
	/**
	 * Takes a name and add them to the chatroom as a person reference
	 * @param person
	 * @return
	 */
	@PostMapping("/join")
	public String directToRoom(@ModelAttribute("joinform") String username) {
		return "main";
	}

	
	/**
	 * Direct the user to the join room
	 */
	@GetMapping("/")
	public String joinChat() {
		return "login";
	}

}
