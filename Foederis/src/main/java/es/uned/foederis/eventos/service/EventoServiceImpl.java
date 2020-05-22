package es.uned.foederis.eventos.service;


import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.repository.*;
import es.uned.foederis.sesion.model.Usuario;

public class EventoServiceImpl implements IEventoService {

	@Autowired
	private IEventoRepository EventoRepository;
	
	
	@Override
	public List<Evento> obtenerEventosFuturos(Date fechaInicio, long usuarioLogado) {
		List<Evento> lst= (List<Evento>) EventoRepository.findByfechaInicioAfter(fechaInicio, Sort.by(Sort.Direction.ASC, "fechaInicio"));
						
		return lst;
	}
	
	
	@Override
	public List<Evento> obtenerEventosHoy(Date fechaInicio, long usuarioLogado) {
		//return (List<Evento>) EventoRepository.findAllByOrderByfechaInicioAscfechaInicioAfter(fechaInicio); 
		List<Evento> lst= (List<Evento>) EventoRepository.findByfechaInicio(fechaInicio);
				
		return lst;				
		
	}

	@Override
	public List<Evento> ObtenerEventos(long usuarioLogado) {
		List<Evento> lst= (List<Evento>) EventoRepository.findAll();	
				
		return lst;					
	}

	@Override
	public void eliminar(Integer id) {
		EventoRepository.deleteById(id);	
	}

	@Override
	public String toString() {
		return "EventoServiceImpl [EventoRepository=" + EventoRepository + "]";
	}
}
