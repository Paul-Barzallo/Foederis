package es.uned.foederis.sesion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;

@Repository
public interface IUsuarioRepository extends CrudRepository<Usuario, Long> {

	Optional<Usuario> findByUsername(String username);
	List<Usuario> findByNombreContainingOrApellidosContaining(String nombre, String apellidos);
	List<Usuario> findByRolContaining(String rol);
	List<Usuario> findByUsernameContaining(String rol);
	List<Usuario> findByRolLessThanAndActivoTrue(Rol rol);
}
