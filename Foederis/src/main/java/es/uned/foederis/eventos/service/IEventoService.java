package es.uned.foederis.eventos.service;

import org.springframework.ui.Model;

import es.uned.foederis.eventos.model.Evento;

public interface IEventoService {
		
	
	/**
	 * Elimina el producto correspondiente al id
	 * 
	 * @param id Id del producto
	 */
	void eliminar(Integer id);


	Evento getEventById(int id);
	
	public String irANuevoEvento(Model model);
	
	public void mensajeNoAccesoEventos(Model model);
	
	public void mensajeConfirmacion(Model model);
	
	public void mensajeInfoAforo(Model model);
	
	public void cargarSalas(Model model, String paramBusq, String valorBusq);


	void mensajeInfoSala(Model model, String paramBusq);


	void setFinEvento(Evento evento);

}
