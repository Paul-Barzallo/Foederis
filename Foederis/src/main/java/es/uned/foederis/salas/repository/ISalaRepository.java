package es.uned.foederis.salas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.sesion.model.Usuario;

@Repository
public interface ISalaRepository extends CrudRepository<Sala, Long> {

	Optional<Sala> findByNombre(String nombre);
	List<Sala> findByNombreContaining(String nombre);
}
