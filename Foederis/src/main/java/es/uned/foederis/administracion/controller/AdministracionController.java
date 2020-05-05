package es.uned.foederis.administracion.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdministracionController {
	@Autowired
	private HttpServletRequest request;
	
	@GetMapping("/administracion")
	public String getSalas(Model model) {
		request.getRemoteUser();
		
		return "/administracion";
	}
}
