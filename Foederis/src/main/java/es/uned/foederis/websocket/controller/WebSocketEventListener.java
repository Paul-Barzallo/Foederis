package es.uned.foederis.websocket.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuarioRepository;
import es.uned.foederis.websocket.model.ChatMessage;

@Component
public class WebSocketEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    private IUsuarioRepository userRepository_;

	@Autowired
	private Usuario user_ = new Usuario();

	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    	LOGGER.info("Received a new web socket connection");
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    	
    	Principal principal= (Principal)headerAccessor.getUser();
    	String username = (String)principal.getName();
    	
    	Optional<Usuario> usuarios = userRepository_.findByUsername(username);
    	if (!usuarios.isEmpty()) {
    		user_ = usuarios.get();
    	}
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        int idEvento = -1;
    	
    	StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
        	LOGGER.info("User Disconnected : " + username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);

		 	//Obtenemos el usuario_evento
        	Optional<Usuario> usuarios = userRepository_.findByUsername(username);
        	if (!usuarios.isEmpty()) {
        		user_ = usuarios.get();

			 	for(Usuario_Evento aux: user_. getEventosDelUsuario()) {   
			 		if (aux.isAsistente()) {
			 			idEvento = aux.getEvento().getIdEvento();
						break;
			 		}
				}   		 	
        	}
        	
		 	if (idEvento != -1)
		 		messagingTemplate.convertAndSend("/topic/public/" + idEvento, chatMessage);
        }
    }
}
