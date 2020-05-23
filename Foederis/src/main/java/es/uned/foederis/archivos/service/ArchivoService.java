package es.uned.foederis.archivos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.archivos.repository.IArchivoRepository;
import es.uned.foederis.eventos.model.Evento;

@Service
public class ArchivoService {

	@Autowired
	private IArchivoRepository FileRepository;
	
	/*
	 * public List<Chat>getChats(String userName, long eventId){ List<Chat> }
	 */	
	
	public Archivo createFile(Archivo file){
        file = FileRepository.save(file);
         
        return file;
    }

	public List<Archivo> findAll() {
		List<Archivo> result = (List<Archivo>) FileRepository.findAll();
		return result;
	}

	public List<Archivo> findByIdEvento(Evento event) {
		List<Archivo> result = (List<Archivo>)FileRepository.findByidEvento(event);
		return result;
	} 

}
