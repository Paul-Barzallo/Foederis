package es.uned.foederis.archivos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.archivos.repository.IArchivoRepository;
import es.uned.foederis.eventos.model.Evento;

@Service
public class ArchivoService {

	@Autowired
	private IArchivoRepository FileRepository;
	
	/**
	 * Crea un registro de archivo en la base de datos
	 * @param file
	 * @return Archivo añadido a la base de datos
	 */
	public Archivo createFile(Archivo file){
        file = FileRepository.save(file);
         
        return file;
    }

	/**
	 * Busca todos los archivos en la base de datos
	 * @return Lista con los archivos de la base de datos
	 */
	public List<Archivo> findAll() {
		List<Archivo> result = (List<Archivo>) FileRepository.findAll();
		return result;
	}

	/**
	 * Busca los archivos pertenecientes a evento event
	 * @param event
	 * @return
	 */
	public List<Archivo> findByIdEvento(Evento event) {
		List<Archivo> result = (List<Archivo>)FileRepository.findByEvento(event);
		return result;
	}

	/**
	 * Elimina un fichero con el id fileId
	 * @param fileId
	 */
	public void removeByIdFile(int fileId) {
		FileRepository.deleteById(fileId);
	} 

}
