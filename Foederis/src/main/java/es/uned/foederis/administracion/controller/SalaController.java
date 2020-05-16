package es.uned.foederis.administracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uned.foederis.administracion.service.AdministracionService;

@Controller
@RequestMapping("/administracion/sala")
public class SalaController {
	@Autowired
	private AdministracionService service;

}
