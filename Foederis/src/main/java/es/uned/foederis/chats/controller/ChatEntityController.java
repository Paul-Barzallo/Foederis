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
    ChatService service;
 
    /*
    @RequestMapping
    public String getAllEmployees(Model model) 
    {
        List<Chat> list = service.getAllEmployees();
 
        model.addAttribute("employees", list);
        return "list-employees";
    }
 
    @RequestMapping(path = {"/edit", "/edit/{id}"})
    public String editEmployeeById(Model model, @PathVariable("id") Optional<Long> id) 
                            throws RecordNotFoundException 
    {
        if (id.isPresent()) {
            Chat entity = service.getEmployeeById(id.get());
            model.addAttribute("employee", entity);
        } else {
            model.addAttribute("employee", new Chat());
        }
        return "add-edit-employee";
    }
     
    @RequestMapping(path = "/delete/{id}")
    public String deleteEmployeeById(Model model, @PathVariable("id") Long id) 
                            throws RecordNotFoundException 
    {
        service.deleteEmployeeById(id);
        return "redirect:/";
    }*/
    
 
    @PostMapping("/create")
    public void createChat(Chat chat) 
    {
        service.createChat(chat);
        //return "redirect:/";
    }
}
