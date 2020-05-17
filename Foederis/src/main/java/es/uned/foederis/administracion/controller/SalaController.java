package es.uned.foederis.administracion.controller;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.constantes.Rutas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.sesion.model.Usuario;

@Controller
@RequestMapping(Rutas.ADM_SALA)
public class SalaController {
	@Autowired
	private AdministracionService service;

	/**
	 * Cargar la pantalla de busqueda de salas
	 * La busqueda se puede realizar por:
	 * 		Nombre 
	 * @param model
	 * @return html
	 */
	@GetMapping(Rutas.SALAS)
	public String getPantallaUsuarios(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			// se agrega una lista con los parametros por los que se puede buscar la sala
			service.cargarParamsBusqSalas(model);
			return service.irASalas(model);
		} 
		service.mensajeNoAccesoSalas(model);
		return "redirect:"+Vistas.HOME;
		
	}
	
	/**
	 * Peticion ajax que busca las salas que coincidan con los parametros de busqueda
	 * La busqueda no es case sensitive
	 * Se busca que contenga el valor escrito no que empiece por el
	 * @param model
	 * @param busqueda
	 * @param valor
	 * @return
	 */
	@GetMapping(Rutas.BUSQ_SALAS)
	@ResponseBody
	public ModelAndView ajaxUsuarios(Model model, Optional<String> paramBusq, Optional<String> valorBusq) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			String parametro = (paramBusq.isPresent())? paramBusq.get() : "";
			String valor = (valorBusq.isPresent())? valorBusq.get() : "";
			// se cargan las salas, si los valores de busqueda estan vacios se carga la lista completa
			service.cargarSalas(model, parametro, valor);	
			// Se añaden las acciones que realizará el boton de acciones de la lista de resultados
			service.cargarAccionesSalas(model);
			return new ModelAndView("fragmentos :: tabla_salas");
		} 
		service.mensajeNoAccesoSalas(model);;
		return new ModelAndView(Vistas.HOME);
	}
}
