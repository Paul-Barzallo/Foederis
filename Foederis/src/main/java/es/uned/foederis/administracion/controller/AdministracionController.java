package es.uned.foederis.administracion.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.uned.foederis.Constantes;
import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;

@Controller
public class AdministracionController {
	
	@Autowired
	private IRolRepository rolRepo;
	
	@GetMapping("/administracion/perfil")
	public String getSalas(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Rol> roles = new ArrayList<>();
		for (Rol rol : rolRepo.findAll()) {
			roles.add(rol);
		}
		
		model.addAttribute(Constantes.Atributo.PANTALLA, Constantes.Pantalla.PERFIL);
		model.addAttribute(Constantes.Atributo.USUARIO, user);
		model.addAttribute(Constantes.Atributo.ROLES, roles);
		
		return "/administracion/perfil";
	}
}
