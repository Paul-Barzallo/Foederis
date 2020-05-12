package es.uned.foederis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

@SpringBootApplication
public class FoederisApplication implements ApplicationRunner{
	@Autowired
	private IUsuarioRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(FoederisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Iterable<Usuario> usuarios = userRepo.findAll();
		for (Usuario usuario : usuarios) {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		userRepo.saveAll(usuarios);
	}
}
