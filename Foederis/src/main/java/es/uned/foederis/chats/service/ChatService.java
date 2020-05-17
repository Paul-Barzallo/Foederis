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
	
	 public Chat createChat(Chat chat) 
	    {
	        if(chat.getIdChat()  == 0) 
	        {
	            chat = ChatRepository.save(chat);
	             
	            return chat;
	        } 
	        else
	        {
	            Optional<Chat> itChat = ChatRepository.findById(chat.getIdChat());
	             
	            if(itChat.isPresent()) 
	            {
	                Chat aChat = itChat.get();
	               
	                aChat.setIdUsuarioPropietario(chat.getIdUsuarioPropietario());
	                aChat.setTexto(chat.getTexto());
	                aChat.setTimestamp(chat.getTimestamp());
	 
	                aChat = ChatRepository.save(aChat);
	                 
	                return aChat;
	            } else {
	                chat = ChatRepository.save(chat);
	                 
	                return chat;
	            }
	        }
	    } 

}
