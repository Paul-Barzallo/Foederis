package es.uned.foederis.eventos.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Usuario_Evento;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.Date;
import java.util.List;

@Repository
public interface IUsuarioEventoRepository extends CrudRepository<Usuario_Evento, Integer>{
	

}

