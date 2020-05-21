package es.uned.foederis.upload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.FoederisApplication;
import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.archivos.service.ArchivoService;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.chats.service.ChatService;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.service.EventoServiceImpl;
import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.websocket.controller.ChatController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class UploadController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(FoederisApplication.class);
	
   //Save the uploaded file to this folder
    private static final String UPLOADED_FOLDER = ".//uploadFiles//";

    String Message_;
    
	@Autowired
	IEventoService eventService;

	@Autowired
    ArchivoService MyFileService;

//    @GetMapping("/upload")
//    public String index() {
//        return "index";
//    }
    
    
    @PostMapping("/upload")
    public ModelAndView uploadMultipartFile(@RequestParam("file") MultipartFile file, @RequestParam("eventid") int eventId , Model model){
        if (file.isEmpty()) {
        	Message_ = "Please select a file to upload";
            //redirectAttributes.addFlashAttribute("message", Message_);
            model.addAttribute("message", Message_);

            return new ModelAndView("fragmentos :: resultUpload");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            Message_ = "You successfully uploaded '" + file.getOriginalFilename() + "'";
            
            CreateFileEntity(path,eventId);
            
        } catch (IOException e) {
        	Message_= "Error -> message = " + e.getMessage();
        }

        model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }
/*
    @PostMapping("/upload") 
    @ResponseBody
//    public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file,
//            RedirectAttributes redirectAttributes) {
        public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file,
                Model model) {

        if (file.isEmpty()) {
        	Message_ = "Please select a file to upload";
            //redirectAttributes.addFlashAttribute("message", Message_);
            model.addAttribute("message", Message_);

            return new ModelAndView("fragmentos :: resultUpload");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            Message_ = "You successfully uploaded '" + file.getOriginalFilename() + "'";
            
            //redirectAttributes.addFlashAttribute("message",Message_);
            model.addAttribute("message", Message_);

            return new ModelAndView("fragmentos :: resultUpload");
        } catch (IOException e) {
            e.printStackTrace();
        }

        model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }
*/
    
	@GetMapping("/uploadStatus")
    @ResponseBody
    public ModelAndView uploadStatus(Model model) {
    	model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }

    private void CreateFileEntity(Path pathFile, int eventId) {
		// Localizar el evento en la BD
    	Evento ev = eventService.getEventById(eventId);
    	// Genera entidad Archivo y la inserta en bd

		
		  Archivo file = new Archivo(); 
		  file.setIdEvento(ev);
		  file.setNombreArchivo(pathFile.toString());
		  
		  MyFileService.createChat(file);
		 
		  LogFiles();
		
	}

	private void LogFiles() {
    	// Debug alta de chat
		  List<Archivo> result = (List<Archivo>) MyFileService.findAll();
		  for (Archivo f: result) {
			  LOGGER.info("File uploades: {}", f.toString());
		  }
    }
}