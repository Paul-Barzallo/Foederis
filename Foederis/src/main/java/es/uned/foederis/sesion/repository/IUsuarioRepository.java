package es.uned.foederis.sesion.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.sesion.model.Usuario;

@Repository
public interface IUsuarioRepository extends CrudRepository<Usuario, Long> {

	Usuario findByUsername(String username);
}
