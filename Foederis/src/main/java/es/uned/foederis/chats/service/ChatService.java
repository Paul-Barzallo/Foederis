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
	
	
	public Chat createChat(Chat chat){
        chat = ChatRepository.save(chat);
         
        return chat;
    }

	public List<Chat> findAll() {
		List<Chat> result = (List<Chat>) ChatRepository.findAll();
		return result;
	}

	public List<Chat> findByIdEvento(Evento event) {
		List<Chat> result = (List<Chat>)ChatRepository.findByEvento(event);
		return result;
	}

	public void remove(Chat c) {
		ChatRepository.delete(c);
	} 

}
