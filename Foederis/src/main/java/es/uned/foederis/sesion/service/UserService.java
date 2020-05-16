package es.uned.foederis.sesion.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private IUsuarioRepository repo;

	/**
	 * Implementación de busqueda de usuario del login
	 * Se busca por username y se devuelve el usuario con los datos
	 * Si no 
	 * @param username
	 * @return Usuario Spring-security
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user;
		Optional<Usuario> opUser = repo.findByUsername(username);
		if (opUser.isEmpty()) {
			user = new Usuario();
			user.setActivo(false);
		} else {
			user = opUser.get();
		}
		return user;
	}

}
