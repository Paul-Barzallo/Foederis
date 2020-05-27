package es.uned.foederis.eventos.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import es.uned.foederis.eventos.model.Evento;


@Repository
public interface IEventoRepository extends CrudRepository<Evento, Integer> {
	
	
	Evento findByidEvento(int idEvento);


}