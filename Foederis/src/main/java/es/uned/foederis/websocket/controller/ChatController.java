package es.uned.foederis.websocket.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uned.foederis.chats.service.ChatService;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.FoederisApplication;
import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.websocket.model.ChatMessage;
import es.uned.foederis.sesion.constante.UsuarioConstantes;
import es.uned.foederis.sesion.model.Usuario;


//import es.uned.foederis.sesion.service.UserService;

@Controller
public class ChatController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(FoederisApplication.class);


    @Autowired
    ChatService MyChatService;
    @Autowired
    AdministracionService MyUserService;
    
    private Model MyModel;
    
    // Id del evento al que pertenece el chat
    private Evento MyEvent;

	@GetMapping("/chat")
    public String getChat(@RequestParam(value="id") Evento evento, Model model, Authentication authentication) {
		MyModel = model;
		MyEvent = evento;
		
		MyModel.addAttribute("user", authentication.getName());
		MyModel.addAttribute("message", MyModel.getAttribute("message"));
		MyModel.addAttribute("eventname", MyEvent.getNombre());
		MyModel.addAttribute("eventid", MyEvent.getIdEvento());
		return "/chat";
	}

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {

    	MyUserService.cargarUsuarios(MyModel,UsuarioConstantes.USERNAME,chatMessage.getSender());

    	@SuppressWarnings("unchecked")
    	List<Usuario> usuarios = (List<Usuario>)MyModel.getAttribute(Atributos.USUARIOS);

    	for (Usuario usr: usuarios) {
    		if (!usr.getNombre().equals("null") && usr.getUsername().equals(chatMessage.getSender())) { 
    			// Construir entidad Chat 
    			Chat c = new Chat();
    			c.setTimestamp(new Timestamp((new Date()).getTime())); 
    			c.setTexto(chatMessage.getContent());
    			c.setIdEvento(MyEvent);

    			MyChatService.createChat(c);
    		}
    	}

    	logChats();

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
    
    private void logChats() {
    	// Debug alta de chat
		  List<Chat> result = (List<Chat>) MyChatService.findAll();
		  for (Chat c: result) {
			  LOGGER.info("Chat message: {}", c.toString());
		  }
    }
}
