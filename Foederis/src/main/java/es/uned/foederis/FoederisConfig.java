package es.uned.foederis;

import java.text.SimpleDateFormat;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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

	
	@Bean("timeFormat")
	public SimpleDateFormat timeFormat() {
		sdf = new SimpleDateFormat("HH:mm");
		return sdf;
	}
	
	@Bean("dateTimeFormat")
	public SimpleDateFormat dateTimeFormat() {
		sdf = new SimpleDateFormat("DD/MM/YYY HH:mm");
		return sdf;
	}
	
}
