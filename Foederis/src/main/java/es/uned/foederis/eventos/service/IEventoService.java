package es.uned.foederis.eventos.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import es.uned.foederis.eventos.model.Evento;

public interface IEventoService {
	
	/**
	 * Guarda el evento
	 * 
	 * @param evento
	 */
	//void guardar(Evento evento);
	
	/**
	 * Recupera la lista completa de eventos
	 * 
	 * @return
	 */
	List<Evento> ObtenerEventos(long idUsuario);
	
	/**
	 * Recupera la lista completa de eventos por fecha
	 * 
	 * @return
	 */	

	List<Evento> obtenerEventosFuturos(Date fechaInicio,long idUsuario);
	
	List<Evento> obtenerEventosHoy(Date fechaInicio,long idUsuario);
	
	
	
	/**
	 * Devuelve el evento correspondiente al id si existe
	 * 
	 * @param id Id del evento
	 * @return
	 */
	//Optional<Evento> obtenerEvento(Integer id);
	
	/**
	 * Elimina el producto correspondiente al id
	 * 
	 * @param id Id del producto
	 */
	void eliminar(Integer id);



}
