package es.uned.foederis.sesion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.salas.repository.ISalasRepository;

@Controller
public class PruebaController {
	@Autowired
	private ISalasRepository repo;

	@GetMapping("/salas")
	public String getSalas(Model model) {
		String salas = "";
		for (Sala sala : repo.findAll()) {
			salas += sala.toString()+",  \n";
		}
		model.addAttribute("salas", salas);
		return "salas";
	}
}

