package es.uned.foederis.chats.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.foederis.chats.repository.IChatRepository;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.chats.model.Chat;

@Service
public class ChatService {

	@Autowired
	private IChatRepository ChatRepository;
	
	/**
	 * Crea un registro chat en base de datos
	 * @param chat
	 * @return
	 */
	public Chat createChat(Chat chat){
        chat = ChatRepository.save(chat);
         
        return chat;
    }

	/**
	 * Busca todos los registros de chat
	 * @return Lista con todos los registros de chat
	 */
	public List<Chat> findAll() {
		List<Chat> result = (List<Chat>) ChatRepository.findAll();
		return result;
	}

	/**
	 * Busca todos los registros de chat que pertenezcan al evento event
	 * @param event
	 * @return
	 */
	public List<Chat> findByIdEvento(Evento event) {
		List<Chat> result = (List<Chat>)ChatRepository.findByEvento(event);
		return result;
	}

	/**
	 * Elimina un chat de la tabla
	 * @param c
	 */
	public void remove(Chat c) {
		ChatRepository.delete(c);
	} 

}
