package es.uned.foederis.chats.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uned.foederis.chats.service.ChatService;
import es.uned.foederis.chats.model.Chat;

@Controller
@RequestMapping("/chatentity")
public class ChatEntityController {
	
    @Autowired
    ChatService chatService_;

    /**
     * 
     * @param chat
     */
    @PostMapping("/create")
    public void createChat(Chat chat) 
    {
        chatService_.createChat(chat);
    }
}
