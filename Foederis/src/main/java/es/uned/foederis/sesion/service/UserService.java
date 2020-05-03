package es.uned.foederis.sesion.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuariosRepository;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private IUsuariosRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		List<GrantedAuthority> roles = new ArrayList<>();
		Usuario user = repo.findByUser(username);
		
		if (user!=null) {
			roles.add(new SimpleGrantedAuthority("USER"));
			roles.add(new SimpleGrantedAuthority(user.getRol()));
			userDetails = new User(user.getUser(), user.getPassword(), roles);
		} else {
			userDetails = new User("null", "null", roles);
		}

		return userDetails;
	}

}
