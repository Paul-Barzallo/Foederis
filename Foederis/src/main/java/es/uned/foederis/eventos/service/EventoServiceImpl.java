package es.uned.foederis.eventos.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.constantes.Pantallas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.repository.*;
import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.salas.repository.ISalaRepository;
import es.uned.foederis.sesion.constantes.UsuarioConstantes;
import es.uned.foederis.sesion.model.Usuario;

public class EventoServiceImpl implements IEventoService {

	@Autowired
	private IEventoRepository EventoRepository;
	
	@Autowired
	private ISalaRepository salaRepo;
	
//	@Override
//	public List<Evento> obtenerEventosFuturos(Date fechaInicio, long usuarioLogado) {
//		List<Evento> lst= (List<Evento>) EventoRepository.findByfechaInicioAfter(fechaInicio, Sort.by(Sort.Direction.ASC, "fechaInicio"));
//						
//		return lst;
//	}
	
	
//	@Override
//	public List<Evento> obtenerEventosHoy(Date fechaInicio, long usuarioLogado) {
//		//return (List<Evento>) EventoRepository.findAllByOrderByfechaInicioAscfechaInicioAfter(fechaInicio); 
//		List<Evento> lst= (List<Evento>) EventoRepository.findByfechaInicio(fechaInicio);
//				
//		return lst;				
//		
//	}

//	@Override
//	public List<Evento> ObtenerEventos(long usuarioLogado) {
//		List<Evento> lst= (List<Evento>) EventoRepository.findAll();	
//				
//		return lst;					
//	}

	@Override
	public void eliminar(Integer id) {
		EventoRepository.deleteById(id);	
	}
	
	@Override
	public Evento getEventById(int id){
		return EventoRepository.findByIdEvento(id);
	}

	@Override
	public String toString() {
		return "EventoServiceImpl [EventoRepository=" + EventoRepository + "]";
	}
	
	@Override
	public String irANuevoEvento(Model model) {
		model.addAttribute(Atributos.PANTALLA, Pantallas.EVENTOS);
		return Vistas.NUEVO_EVENTO;
	}
	
	@Override
	public void mensajeNoAccesoEventos(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Aceso Denegado");
		model.addAttribute(Atributos.ALERTA, "No tiene permisos de acceso a los eventos");
	}
	
	@Override
	public void mensajeConfirmacion(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Confirmacion");
		model.addAttribute(Atributos.ALERTA, "El evento se confirmó correctamente");
	}
	
	@Override
	public void mensajeInfoAforo(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Info");
		model.addAttribute(Atributos.ALERTA, "Aforo superado, se recomienda cambiar de sala.");
	}
	@Override
	public void mensajeInfoSala(Model model, String paramBusq) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Info");		
		
		List<Sala> salas = new ArrayList<Sala>();
		salas.add(salaRepo.findById(Long.parseLong(paramBusq)).get());
		
		model.addAttribute(Atributos.SALAS, salas);
	}
	
	
	/**
	 * Añade las salas que coincidan con la busqueda al model
	 * @param model
	 * @param paramBusq tipos de busqueda: NOMBRE, ...
	 * @param valorBusq 
	 */
	public void cargarSalas(Model model, String paramBusq, String valorBusq) {
		List<Sala> salas = null;
		// ponemos el valor de la busqueda en minusculas porque así se guarda en base de datos
		valorBusq = valorBusq.toLowerCase();
		
		switch (paramBusq) {
		case UsuarioConstantes.NOMBRE:
			salas = salaRepo.findByNombreContainingAndActivaTrue(valorBusq);
			break;
		default:
			salas = new ArrayList<>();
			for(Sala sala : salaRepo.findByActivaTrue()) {
				salas.add(sala);
			}
			break;
		}
		if (salas!=null) {
			model.addAttribute(Atributos.SALAS, salas);
		}
	}
	
	public 	void setFinEvento(Evento evento) {
		EventoRepository.save(evento);
	}

	
}
