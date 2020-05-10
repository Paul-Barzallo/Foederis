package es.uned.foederis.eventos.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import es.uned.foederis.eventos.model.Evento;


@Repository
public interface IEventoRepository extends CrudRepository<Evento, Integer> {
	
	List<Evento> findByfechaInicioAfter(Date fechaInicio);

}