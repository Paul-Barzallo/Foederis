package es.uned.foederis.eventos.repository;

import java.sql.Date;

import org.springframework.data.repository.CrudRepository;

import es.uned.foederis.eventos.model.Horarios;

public interface IHorarioRepository extends CrudRepository<Horarios, Integer>{
	Horarios findByIdHorario(Integer idHorario); 
}
