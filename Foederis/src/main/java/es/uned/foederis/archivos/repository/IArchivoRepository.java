package es.uned.foederis.archivos.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.eventos.model.Evento;

public interface IArchivoRepository extends CrudRepository<Archivo, Integer> {
	List<Archivo> findByEvento(Evento evento);
}
