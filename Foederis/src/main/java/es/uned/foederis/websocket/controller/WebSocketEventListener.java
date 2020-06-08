package es.uned.foederis.websocket.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.repository.IUsuarioEventoRepository;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuarioRepository;
import es.uned.foederis.websocket.model.ChatMessage;

@Component
public class WebSocketEventListener {
	
	final boolean CONNECTED		= true;
	final boolean NOT_CONNECTED = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;
    
    @Autowired
    private IUsuarioRepository userRepository_;

	@Autowired
	private IUsuarioEventoRepository userEventRepository_;

	/**
	 * Listener de solicitudes de conexión de clientes al websocket
	 * @param event
	 */
	@EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    	LOGGER.info("Received a new web socket connection");
    }

	/**
	 * Listener de desconexiones de clientes al websocket
	 * @param event
	 */
	@EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        int idEvento = -1;

        MessageHeaders msgHeaders = event.getMessage().getHeaders();
        Principal princ = (Principal) msgHeaders.get("simpUser");

        String userName = princ.getName();

        if(userName != null) {
        	LOGGER.info("User Disconnected : " + userName);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(userName);

            idEvento = setUserConnected(userName, NOT_CONNECTED);

            if (idEvento != -1) {
		 		messagingTemplate.convertAndSend("/topic/public/" + idEvento, chatMessage);
            }
        }
    }

    private int setUserConnected(String userName, boolean connected) {
        int idEvento = -1;

        Optional<Usuario> usuarios = userRepository_.findByUsername(userName);
    	if (!usuarios.isEmpty()) {
    		Usuario user = usuarios.get();

		 	for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
		 		if (aux.isAsistente() && aux.isConectado() != connected) {
		 			idEvento = aux.getEvento().getIdEvento();
		 			aux.setConectado(connected);
		 			userEventRepository_.save(aux);
		 			
		 			log(user);
					break;
		 		}
			}   		 	
    	}
    	
    	return idEvento;
	}

	private void log(Usuario user) {
		// Debug Usuario_evento
		List<Usuario_Evento> result = user.getEventosDelUsuario();
		for (Usuario_Evento c: result) {
			LOGGER.info("User_Event: {}", c.toString());
		}
	}

}
