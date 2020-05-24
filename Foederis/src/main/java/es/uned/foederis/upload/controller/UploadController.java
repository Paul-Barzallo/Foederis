package es.uned.foederis.upload.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.FoederisApplication;
import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.archivos.service.ArchivoService;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.chats.service.ChatService;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.service.EventoServiceImpl;
import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.websocket.controller.ChatController;
import es.uned.foederis.constantes.Archivos;
import es.uned.foederis.constantes.Atributos;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/chat")
public class UploadController {
	
	private static final Logger LOGGER=LoggerFactory.getLogger(FoederisApplication.class);
	

    String Message_;
    
	@Autowired
	IEventoService 			eventService_;

	@Autowired
    ArchivoService 			myFileService_;
	@Autowired
    ChatService 			myChatService_;
	@Autowired
	AdministracionService 	myUserService_;

    
	@GetMapping("/download")
	public ModelAndView listEventFiles(@RequestParam("eventId") int eventId, Model model) throws IOException {
		Evento ev = eventService_.getEventById(eventId);
		
		model.addAttribute(Atributos.CHATS, myChatService_.findByIdEvento(ev));
		model.addAttribute(Atributos.FILES, myFileService_.findByIdEvento(ev));

		return new ModelAndView("chat/resultDownload");
	}

	
	@GetMapping("/download/file")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@RequestParam("filename") String filename) {
		Path path = Paths.get(filename);
		Resource resource = null;

		try {
			resource = new UrlResource(path.toUri());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/octet-stream"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

    @PostMapping("/upload")
    public ModelAndView uploadMultipartFile(@RequestParam("file") MultipartFile file, 
    										@RequestParam("eventid") int eventId, 
    										@RequestParam("userid") String userId , Model model){
        if (file.isEmpty()) {
        	Message_ = "Please select a file to upload";
            model.addAttribute("message", Message_);

            return new ModelAndView("fragmentos :: resultUpload");
        }

        try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            File directory = new File(Archivos.UPLOADED_FOLDER);
            if (! directory.exists()){
                directory.mkdir();
            }
            directory = new File(Archivos.UPLOADED_FOLDER + eventId);
            if (! directory.exists()){
                directory.mkdir();
            }

            Path path = Paths.get(Archivos.UPLOADED_FOLDER + eventId + "//" + file.getOriginalFilename());
            Files.write(path, bytes);

            Message_ = "Fichero '" + file.getOriginalFilename() + "' subido correctamente.";
            
            CreateFileEntity(path, eventId, userId, model);
            
        } catch (IOException e) {
        	Message_= "Error -> message = " + e.getMessage();
        }

        model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }


	@GetMapping("/uploadStatus")
    @ResponseBody
    public ModelAndView uploadStatus(Model model) {
    	model.addAttribute("message", Message_);

        return new ModelAndView("fragmentos :: resultUpload");
    }

    private void CreateFileEntity(Path pathFile, int eventId, String userId, Model model) {
    	
    	// Generar timestap del momento de la subida
    	Timestamp timestamp = new Timestamp((new Date()).getTime());
    	
    	// Localizar el evento en la BD
    	Evento ev = eventService_.getEventById(eventId);

		if (myUserService_.cargarUsuario(model, Long.parseLong(userId))) {
			Usuario usr = (Usuario) model.getAttribute(Atributos.USUARIO);

			// Genera entidad Archivo y la inserta en bd
			Archivo file = new Archivo();
			file.setIdEvento(ev);
			file.setNombreArchivo(pathFile.toString());
			file.setIdUsuario(usr);
			file.setTimestamp(timestamp);

			myFileService_.createFile(file);

			logFiles();
		}
	}

	private void logFiles() {
    	// Debug alta de chat
		  List<Archivo> result = (List<Archivo>) myFileService_.findAll();
		  for (Archivo f: result) {
			  LOGGER.info("File uploades: {}", f.toString());
		  }
    }
}