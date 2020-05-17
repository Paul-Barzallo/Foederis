package es.uned.foederis.administracion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.constantes.Rutas;

@Controller
@RequestMapping(Rutas.ADM_SALA)
public class SalaController {
	@Autowired
	private AdministracionService service;

}
