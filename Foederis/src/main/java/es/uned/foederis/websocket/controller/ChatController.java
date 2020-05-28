package es.uned.foederis.websocket.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uned.foederis.FoederisApplication;
import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.chats.service.ChatService;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.sesion.constantes.UsuarioConstantes;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.websocket.model.ChatMessage;


//import es.uned.foederis.sesion.service.UserService;

@Controller
public class ChatController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(FoederisApplication.class);


    @Autowired
    ChatService myChatService_;
    @Autowired
    AdministracionService myUserService_;
    
    private Model myModel_;
    
    // Lista del eventos
    private HashMap <Integer,Evento> myEventList_ = new HashMap<Integer,Evento>();

	@GetMapping("/chat")
    public String getChat(@RequestParam(value="id") Evento evento, Model model, Authentication authentication) {
		myModel_ = model;
		if (!myEventList_.containsKey(evento.getIdEvento())) {
			myEventList_.put(evento.getIdEvento(),evento);
		}
		
		myModel_.addAttribute("user", authentication.getName());
		myModel_.addAttribute("message", myModel_.getAttribute("message"));
		myModel_.addAttribute("eventname", evento.getNombre());
		myModel_.addAttribute("eventid", evento.getIdEvento());
    	
		myUserService_.cargarUsuarios(model,UsuarioConstantes.USERNAME,authentication.getName());
		@SuppressWarnings("unchecked")
    	List<Usuario> usuarios = (List<Usuario>)model.getAttribute(Atributos.USUARIOS);

    	for (Usuario usr: usuarios) {
    		if (!usr.getNombre().equals("null") && usr.getUsername().equals(authentication.getName())) { 
    			myModel_.addAttribute(Atributos.USUARIO, usr.getIdUsuario());
    			break;
    		}
    	}

    	return "/chat";
	}

    @MessageMapping("/chat.sendMessage/{eventId}")
    @SendTo("/topic/public/{eventId}")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage, @DestinationVariable String eventId) {

    	// Comprobar que el mensaje recibido es del evento
    	Timestamp timestamp = new Timestamp((new Date()).getTime());
    	
    	myUserService_.cargarUsuarios(myModel_,UsuarioConstantes.USERNAME,chatMessage.getSender());

    	@SuppressWarnings("unchecked")
    	List<Usuario> usuarios = (List<Usuario>)myModel_.getAttribute(Atributos.USUARIOS);

    	for (Usuario usr: usuarios) {
    		if (!usr.getNombre().equals("null") && usr.getUsername().equals(chatMessage.getSender())) { 
    			// Construir entidad Chat 
    			Chat c = new Chat();
    			c.setTimestamp(timestamp); 
    			c.setTexto(chatMessage.getContent());
    			c.setEvento(myEventList_.get(Integer.parseInt(eventId)));
    			c.setUsuario(usr);

    			myChatService_.createChat(c);
    			
    			break;
    		}
    	}

    	logChats();

    	// Poner el timestamp del servidor
    	SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
    	String dateString=sdf.format(new Date(timestamp.getTime()));
    	
    	chatMessage.setTimestamp(dateString);
    	return chatMessage;
    }

    @MessageMapping("/chat.addUser/{eventId}")
    @SendTo("/topic/public/{eventId}")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, @DestinationVariable String eventId,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    
    private void logChats() {
    	// Debug alta de chat
		  List<Chat> result = (List<Chat>) myChatService_.findAll();
		  for (Chat c: result) {
			  LOGGER.info("Chat message: {}", c.toString());
		  }
    }
}
