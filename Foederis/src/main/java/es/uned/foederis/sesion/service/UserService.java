package es.uned.foederis.sesion.service;

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

	@Override
	public Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario user = repo.findByUsername(username);
		if (user==null) {
			user = new Usuario("null", "null");
		} 

		return user;
	}

}
