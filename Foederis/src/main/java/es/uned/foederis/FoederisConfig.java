package es.uned.foederis;

import java.text.SimpleDateFormat;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

import es.uned.foederis.eventos.service.EventoServiceImpl;
import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.sesion.model.Usuario;

@Configuration
public class FoederisConfig {
	
	@PersistenceContext
	EntityManager entityManager;
	
	private IEventoService eventoService;
	private Usuario userLogin;
	private SimpleDateFormat sdf;

	@Bean
	public IEventoService eventoService() {
		eventoService = new EventoServiceImpl();
		return eventoService;
	}
	
	@Bean
	public Usuario user() {
		userLogin = new Usuario();
		return userLogin;
	}

	
	@Bean
	public SimpleDateFormat timeFormat() {
		sdf = new SimpleDateFormat("HH:mm");
		return sdf;
	}
	
}
