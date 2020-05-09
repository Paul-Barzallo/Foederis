package es.uned.foederis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

@SpringBootApplication
public class FoederisApplication implements ApplicationRunner{
	@Autowired
	private IUsuarioRepository usuario;
	
	@Autowired
	private IRolRepository rol;
	
	public static void main(String[] args) {
		SpringApplication.run(FoederisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("admin");
		String password2 = passwordEncoder.encode("user");

		Usuario user = new Usuario();
		user.setNombre("Administrador");
		user.setPassword(password);
		user.setUsername("admin");
		user.setRol(rol.findById((long) 3).get());
		usuario.save(user);
		
		user = new Usuario();
		user.setNombre("Usuario");
		user.setPassword(password2);
		user.setUsername("user");
		user.setRol(rol.findById((long) 1).get());
		usuario.save(user);
	}
}
