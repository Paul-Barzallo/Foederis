package es.uned.foederis.administracion.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.constantes.Rutas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.salas.model.Sala;
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
	public String getPantallaSalas(Model model) {
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
	public ModelAndView ajaxSalas(Model model, Optional<String> paramBusq, Optional<String> valorBusq) {
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
	
	/**
	 * Devuelve el formulario de sala vacio para crear una nueva sala
	 * Verifica que la petición la haga un administrador si no redirige a HOME
	 * Busca y carga los roles en el fomrulario
	 * Genera un nuevo usuario vacio donde poner los datos
	 * @param model
	 * @return html
	 */
	@GetMapping(Rutas.NUEVO)
	public String getFormularioNuevo(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			Sala sala = new Sala();
			model.addAttribute(Atributos.SALA, sala);
			return service.irASala(model);
		}
		service.mensajeNoAccesoSalas(model);
		return Vistas.HOME;
	}
	
	/**
	 * Devuelve el formulario de modificacion de sala
	 * con los datos de la sala seleccionada rellenos
	 * @param model
	 * @param id identificador para buscar los datos de la sala a cargar 
	 * @return html
	 */
	@GetMapping(Rutas.MODIFICAR)
	public String getFormularioModificar(Model model, Long id) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			// se carga la sala
			if (service.cargarSala(model, id)) {
				return service.irASala(model);
			}
			// si no se encuentra el usuario se añade valores para mensaje de error
			service.mensajeSalaNoEncontrada(model);
		} else {
			// si el usuario no es admin se añade valores para mensaje de error
			service.mensajeNoAccesoSalas(model);
		}
		return Vistas.HOME;
	}
	
	/**
	 * Activa la sala
	 * @param model
	 * @param id
	 * @param activar
	 * @return
	 */
	@GetMapping(Rutas.ACTIVAR)
	public String getActivarSala(Model model, Long id) {
		return service.activarSala(model, id, true)? "redirect:"+Rutas.ADM_SALA_SALAS : Vistas.HOME;
	}
	
	/**
	 * Desactiva la sala
	 * si la sala se desactiva no se mostrará el horario
	 * ni se podrá seleccionar en nuevos eventos
	 * @param model
	 * @param id
	 * @param activar
	 * @return
	 */
	@GetMapping(Rutas.DESACTIVAR)
	public String getDesactivarSala(Model model, Long id) {
		return service.activarSala(model, id, false)? "redirect:"+Rutas.ADM_SALA_SALAS : Vistas.HOME;
	}
	
	/**
	 * Guarda la nueva información de la sala en la db
	 * @param model
	 * @param usuario
	 * @param result errores de validación del formulario
	 * @return
	 */
	@PostMapping(Rutas.GUARDAR)
	public String postGuardarSala(Model model, @Validated Sala sala, BindingResult result) {
		if (service.isNombreSalaRepetido(sala)) {
			result.rejectValue("nombre", "RepeatNombre.sala.nombre");
		}
		if (result.hasErrors()) {
			model.addAttribute(Atributos.SALA, sala);
	        return service.irASala(model);
	    }
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			service.guardarSala(model, sala);
		} else {
			service.mensajeNoAccesoSalas(model);
		}
		return Vistas.HOME;
	}
	
	/**
	 * Elimina la sala
	 * Opción no recomendable si la sala se puede usar en el futuro
	 * Es mejor desactivar
	 * @param model
	 * @param sala
	 * @return
	 */
	@PostMapping(Rutas.ELIMINAR)
	public String postEliminarSala(Model model, Long id) {
		if (service.eliminarSala(model, id)) {
			service.mensajeSalaEliminada(model);
		} else {
			service.mensajeErrorEliminarSala(model);
		}
		return Vistas.HOME;
	}
	
}
