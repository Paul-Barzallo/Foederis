package es.uned.foederis.sesion.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.sesion.model.Rol;

@Repository
public interface IRolRepository extends CrudRepository<Rol, Long> {

}
