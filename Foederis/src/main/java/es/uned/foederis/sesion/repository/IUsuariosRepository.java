package es.uned.foederis.sesion.repository;

import org.springframework.stereotype.Repository;

import es.uned.foederis.sesion.model.Usuario;

import org.springframework.data.repository.CrudRepository;

@Repository
public interface IUsuariosRepository extends CrudRepository<Usuario, Long> {

	Usuario findByUser(String user);
}
