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

import es.uned.foederis.Constantes;
import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.sesion.model.Usuario;

@Controller
@RequestMapping("/administracion/usuario")
public class UsuarioController {
	@Autowired
	private AdministracionService service;

	/**
	 * Cargar la pantalla de busqueda de clientes
	 * La busqueda se puede realizar por:
	 * 		Nombre -> Se busca en nombre y apellido
	 * 		Username
	 * 		Rol
	 * @param model
	 * @return html
	 */
	@GetMapping("/usuarios")
	public String getPantallaUsuarios(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			// se agrega una lista con los parametros por los que se puede buscar el usuario
			service.cargarParamsBusqUsuarios(model);
			return service.irAUsuarios(model);
		} 
		service.mensajeNoAccesoUsuarios(model);
		return "redirect:/";
		
	}
	
	/**
	 * Devuelve el formulario de perfil vacio
	 * para crear un nuevo usuario
	 * @param model
	 * @return html
	 */
	@GetMapping("/nuevo")
	public String getFormularioNuevo(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			service.cargarRoles(model);
			Usuario usuario = new Usuario();
			model.addAttribute(Constantes.USUARIO, usuario);
			return service.irAUsuario(model);
		}
		service.mensajeNoAccesoUsuarios(model);
		return Constantes.Vista.HOME;
	}
	
	/**
	 * Devuelve el formulario de modificacion de usuario
	 * con los datos del usuario seleccionado rellenos
	 * @param model
	 * @param idUsuario id para buscar los datos del usuario a cargar 
	 * @return html
	 */
	@GetMapping("/modificar")
	public String getFormularioModificar(Model model, Long idUsuario) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			// se carga el usuario
			if (service.cargarUsuario(model, idUsuario)) {
				// Se carga la lista de roles si se encontró usuario a cargar
				service.cargarRoles(model);
				return service.irAUsuario(model);
			}
			// si no se encuentra el usuario se añade valores para mensaje de error
			service.mensajeUsuarioNoEncontrado(model);
		} else {
			// si el usuario no es admin se añade valores para mensaje de error
			service.mensajeNoAccesoUsuarios(model);
		}
		return Constantes.Vista.HOME;
	}
	
	/**
	 * Activa o desactiva un usuario
	 * si el usuario se desactiva no podrá iniciar sesión
	 * @param model
	 * @param idUsuario
	 * @param activar
	 * @return
	 */
	@GetMapping("/activar")
	public String getActivarUsuario(Model model, Long idUsuario, Boolean activar) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			if (service.activarUsuario(model, idUsuario, activar)) {
				return "redirect:"+"/administracion/usuarios";
			} else {
				service.mensajeUsuarioNoEncontrado(model);
			}
		} else {
			service.mensajeNoAccesoUsuarios(model);
		}
		return Constantes.Vista.HOME;
	}
	
	/**
	 * Guarda la nueva información del usuario en la db
	 * @param model
	 * @param usuario
	 * @return
	 */
	@PostMapping("/guardar")
	public String postGuardarUsuario(Model model, Optional<String> confirmPassword, @Validated Usuario usuario, BindingResult result) {
		if (usuario.getIdUsuario() == null && !confirmPassword.get().equals(usuario.getPassword())) {
			result.rejectValue("password", "RepeatPassword.usuario.password");
		}
		if (service.isUsernameRepetido(usuario)) {
			result.rejectValue("password", "RepeatUsername.usuario.username");
		}
		if (result.hasErrors()) {
			service.cargarRoles(model);
			model.addAttribute(Constantes.USUARIO, usuario);
	        return Constantes.Vista.PERFIL;
	    }
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			service.guardarUsuario(model, usuario);
		} else {
			service.mensajeNoAccesoUsuarios(model);
		}
		return Constantes.Vista.HOME;
	}
	
	/**
	 * Peticion ajax que busca los usuarios que coincidan con los parametros de busqueda
	 * La busqueda no es case sensitive
	 * Se busca que contenga el valor escrito no que empiece por el
	 * @param model
	 * @param busqueda
	 * @param valor
	 * @return
	 */
	@GetMapping("/usuarios/ajax/buscar")
	@ResponseBody
	public ModelAndView ajaxUsuarios(Model model, Optional<String> paramBusq, Optional<String> valorBusq) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			String parametro = (paramBusq.isPresent())? paramBusq.get() : "";
			String valor = (valorBusq.isPresent())? valorBusq.get() : "";
			//se cargan los usuarios, si los valores de busqueda estan vacios se carga la lista completa
			service.cargarUsuarios(model, parametro, valor);	
			// Se añaden las acciones que realizará el boton de acciones de la lista de resultados
			service.cargarAccionesUsuarios(model);
			return new ModelAndView("fragmentos :: tabla_usuarios");
		} 
		service.mensajeNoAccesoUsuarios(model);;
		return new ModelAndView(Constantes.Vista.HOME);
	}
	
}
