package es.uned.foederis.eventos.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.eventos.model.Usuario_Evento;

@Repository
public interface IUsuarioEventoRepository extends CrudRepository<Usuario_Evento, Integer>{
	
	List<Usuario_Evento> findByEvento(Integer idEvento);
	Usuario_Evento findByEventoAndUsuario(Integer idEvento, Long idUsuario); 

}

