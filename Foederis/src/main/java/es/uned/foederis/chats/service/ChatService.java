package es.uned.foederis.chats.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.foederis.chats.repository.IChatRepository;
import es.uned.foederis.chats.model.Chat;

@Service
public class ChatService {

	@Autowired
	private IChatRepository ChatRepository;
	
	 public Chat createChat(Chat chat){
        chat = ChatRepository.save(chat);
         
        return chat;
    } 

}
