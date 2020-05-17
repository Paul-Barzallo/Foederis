package es.uned.foederis.administracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.constantes.Constantes;
import es.uned.foederis.constantes.Rutas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.sesion.constantes.UsuarioConstantes;
import es.uned.foederis.sesion.model.Usuario;

@Controller
@RequestMapping(Rutas.ADM_PERFIL)
public class PerfilController {
	@Autowired
	private AdministracionService service;

	/**
	 * Devuelve el formulario de perfil con los datos rellenos
	 * para modificar los datos de usuario
	 * @param model
	 * @return html
	 */
	@GetMapping
	public String getPantallaPerfil(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute(Atributos.USUARIO, user);
		return service.irAPerfil(model);
	}
	
	/**
	 * Guarda la nueva información del usuario en la db
	 * @param model
	 * @param usuario
	 * @return
	 */
	@PostMapping(Rutas.GUARDAR)
	public String postModificarPerfil(Model model, @Validated Usuario usuario, BindingResult result) {
		if (service.isUsernameRepetido(usuario)) {
			result.rejectValue("password", "RepeatUsername.usuario.username");
		}
		if (result.hasErrors()) {
			model.addAttribute(Atributos.USUARIO, usuario);
	        return service.irAPerfil(model);
	    }
		service.guardarUsuario(model, usuario);
		return Vistas.HOME;
	}
}
