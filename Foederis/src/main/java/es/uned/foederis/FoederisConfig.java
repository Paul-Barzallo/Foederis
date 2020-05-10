package es.uned.foederis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.eventos.service.EventoServiceImpl;

@Configuration
public class FoederisConfig {
	
	private IEventoService eventoService;

	@Bean
	public IEventoService eventoService() {
		eventoService = new EventoServiceImpl();
		return eventoService;
	}
	
}
