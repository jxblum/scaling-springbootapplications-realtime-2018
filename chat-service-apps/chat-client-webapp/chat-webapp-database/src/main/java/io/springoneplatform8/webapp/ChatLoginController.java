package io.springoneplatform8.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * @author luke
 *
 */
@Controller
public class ChatLoginController {
	
	private MessageService messageService;
	
	public ChatLoginController(MessageService messageService) {
		this.messageService = messageService;
	}


	/**
	 * Takes a name and add them to the chatroom as a person reference
	 * @param person
	 * @return
	 */
	@PostMapping("/chat")
	public ModelAndView directToRoom(@ModelAttribute User user, Model model) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("username", user.getUsername());
		mv.addObject("usercount", messageService.getChatCount());
		mv.addObject("message", new Message(user.getUsername()));
		mv.setViewName("main");
		return mv;
	}

	
	/**
	 * Direct the user to the join room
	 */
	@GetMapping("/")
	public ModelAndView joinChat(Model model) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("user", new User());
		mv.setViewName("login");
		return mv;
	}

}
