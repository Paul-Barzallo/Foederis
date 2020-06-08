package es.uned.foederis;

import java.text.SimpleDateFormat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import es.uned.foederis.eventos.service.EventoServiceImpl;
import es.uned.foederis.eventos.service.IEventoService;

@Configuration
public class FoederisConfig {
	
	@PersistenceContext
	EntityManager entityManager;
	
	private IEventoService eventoService;
	private SimpleDateFormat sdf;

	@Bean
	public IEventoService eventoService() {
		eventoService = new EventoServiceImpl();
		return eventoService;
	}
	
	@Bean("timeFormat")
	public SimpleDateFormat timeFormat() {
		sdf = new SimpleDateFormat("HH:mm");
		return sdf;
	}
	
	@Bean("dateTimeFormat")
	public SimpleDateFormat dateTimeFormat() {
		sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		return sdf;
	}
	
}
