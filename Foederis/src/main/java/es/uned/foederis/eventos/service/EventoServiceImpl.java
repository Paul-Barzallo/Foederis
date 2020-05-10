package es.uned.foederis.eventos.service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.repository.*;

public class EventoServiceImpl implements IEventoService {

	@Autowired
	private IEventoRepository EventoRepository;
	
	

//	@Override
//	public void guardar(Evento producto) {
//		EventoRepository.save(producto);
//	}
	
	@Override
	public List<Evento> obtenerEventosFuturos(Date fechaInicio) {
		return (List<Evento>) EventoRepository.findByfechaInicioAfter(fechaInicio); 
	}

	@Override
	public List<Evento> ObtenerEventos() {
		return (List<Evento>) EventoRepository.findAll();
	}

//	@Override
//	public Optional<Evento> obtenerEvento(Integer id) {
//		return EventoRepository.findById(id);
//	}

	@Override
	public void eliminar(Integer id) {
		EventoRepository.deleteById(id);	
	}

	@Override
	public String toString() {
		return "EventoServiceImpl [EventoRepository=" + EventoRepository + "]";
	}


}
