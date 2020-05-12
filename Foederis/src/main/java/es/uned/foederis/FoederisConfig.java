package es.uned.foederis;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.uned.foederis.eventos.service.EventoServiceImpl;
import es.uned.foederis.eventos.service.IEventoService;

@Configuration
public class FoederisConfig {
	
	@PersistenceContext
	EntityManager entityManager;
	
	private IEventoService eventoService;

	@Bean
	public IEventoService eventoService() {
		eventoService = new EventoServiceImpl();
		return eventoService;
	}
}
