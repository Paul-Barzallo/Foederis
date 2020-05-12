package es.uned.foederis.administracion.controller;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.WordUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.Constantes;
import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

@Controller
@RequestMapping("/administracion")
public class AdministracionController {
	
	@Autowired
	private IRolRepository rolRepo;
	
	@Autowired
	private IUsuarioRepository userRepo;
	
	@GetMapping("/perfil")
	public String getPerfil(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Rol> roles = new ArrayList<>();
		for (Rol rol : rolRepo.findAll()) {
			roles.add(rol);
		}
		
		model.addAttribute(Constantes.PANTALLA, Constantes.Pantalla.PERFIL);
		model.addAttribute(Constantes.USUARIO, user);
		model.addAttribute(Constantes.ROLES, roles);
		
		return "/administracion/perfil";
	}
	
	@GetMapping("/buscar/usuarios")
	public String getUsuarios(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<String> paramsBusqueda = new ArrayList<>();
		paramsBusqueda.add(Constantes.Usuario.USERNAME);
		paramsBusqueda.add(Constantes.Usuario.NOMBRE);
		paramsBusqueda.add(Constantes.Usuario.ROL);
		
		model.addAttribute(Constantes.PANTALLA, Constantes.Pantalla.PERFIL);
		model.addAttribute(Constantes.PARAMS_BUSQUEDA, paramsBusqueda);
		
		return "/administracion/buscar/usuarios";
	}
	
	@GetMapping("/buscar/usuarios/ajax")
	@ResponseBody
	public ModelAndView ajaxUsuarios(Model model, String busqueda, String valor) {
		List<Usuario> usuarios = null;
		valor = valor.toUpperCase();
		
		switch (busqueda) {
		case Constantes.Usuario.NOMBRE:
			usuarios = userRepo.findByNombreContainingOrApellidosContaining(valor, valor);
			break;
		case Constantes.Usuario.ROL:
			usuarios = userRepo.findByRolContaining(valor);
			break;
		case Constantes.Usuario.USERNAME:
			usuarios = userRepo.findByUsernameContaining(valor);
			break;
		default:
			usuarios = new ArrayList<>();
			for(Usuario user : userRepo.findAll()) {
				usuarios.add(user);
			}
			break;
		}
		if (usuarios!=null) {
			model.addAttribute(Constantes.USUARIOS, usuarios);
		}
		return new ModelAndView("fragmentos :: tabla_usuarios");
	}
}
