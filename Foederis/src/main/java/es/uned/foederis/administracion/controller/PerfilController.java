package es.uned.foederis.administracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uned.foederis.Constantes;
import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.sesion.model.Usuario;

@Controller
@RequestMapping("/administracion/perfil")
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
	public String getPerfil(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.addAttribute(Constantes.USUARIO, user);
		return service.irAPerfil(model);
	}
}
