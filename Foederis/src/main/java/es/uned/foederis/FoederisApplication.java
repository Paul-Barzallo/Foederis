package es.uned.foederis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuariosRepository;

@SpringBootApplication
public class FoederisApplication implements ApplicationRunner{
	@Autowired
	private IUsuariosRepository usuario;
	
	public static void main(String[] args) {
		SpringApplication.run(FoederisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("admin");
		
		Usuario user = new Usuario();
		user.setNombre("administrador");
		user.setPassword(password);
		user.setUser("admin");
		user.setRol("2");
		usuario.save(user);
	}

}
