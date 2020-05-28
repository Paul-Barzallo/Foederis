package es.uned.foederis.eventos.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.eventos.model.Evento;


@Repository
public interface IEventoRepository extends CrudRepository<Evento, Integer> {
	
	Evento findByIdEvento(int idEvento);

}