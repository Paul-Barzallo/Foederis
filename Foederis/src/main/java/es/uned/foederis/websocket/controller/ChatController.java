package es.uned.foederis.websocket.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.uned.foederis.chats.service.ChatService;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.websocket.model.ChatMessage;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.service.UserService;

@Controller
public class ChatController {

    @Autowired
    ChatService MyChatService;
    @Autowired
    UserService MyUserService;

	@GetMapping("/chat")
    public String getChat(Model model, Authentication authentication) {
		model.addAttribute("user", authentication.getName());
		model.addAttribute("message", model.getAttribute("message"));	// No funciona, buscar alternativas
		return "/chat";
	}

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
    	
		/*
		 * Usuario usr = MyUserService.loadUserByUsername(chatMessage.getSender());
		 * 
		 * if (usr.getNombre() != "null") { // Building chat entity Chat c = new Chat();
		 * c.setTimestamp(new Date()); 
		 * c.setTexto(chatMessage.getContent());
		 * c.setIdUsuarioPropietario(usr.getId()); }
		 */
    	
    	return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
