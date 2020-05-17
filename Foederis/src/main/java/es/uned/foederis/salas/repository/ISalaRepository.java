package es.uned.foederis.salas.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.salas.model.Sala;

@Repository
public interface ISalasRepository extends CrudRepository<Sala, Long> {

	Sala findByNombre(String nombre);
}
