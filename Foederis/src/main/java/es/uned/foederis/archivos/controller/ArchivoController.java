package es.uned.foederis.archivos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.archivos.service.ArchivoService;

@Controller
@RequestMapping("/archivo")
public class ArchivoController {

    @Autowired
    ArchivoService service;

    
    @PostMapping("/create")
    public void createFile(Archivo file) 
    {
        service.createFile(file);
    }

}
