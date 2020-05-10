package es.uned.foederis.eventos.controller;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.service.IEventoService;;

@Controller
@RequestMapping("Evento") //Empieces url navegador y asi evitamos este en todos los metodos
public class EventosController {
	
	@Autowired
	private IEventoService evento;	

		
	 @GetMapping("/baja")
	    public String baja(@RequestParam(value="id") Integer idEvento) {
		 
		 //Salir del evento		 
		 evento.eliminar(idEvento);
		 	
		 //Usamos redirect porque necesitamos que entre al controlador a cargar
	        return "redirect:../listarEventos";
	    }
	 
	 @GetMapping("/entrar")
	    public ModelAndView entrar(@RequestParam(value="id") Evento evento) {
		 	ModelAndView mav = new ModelAndView();
		 	mav.addObject("eventoSeleccionado", evento);
		 //evento.eliminar(idEvento);
		 	
		 //Usamos redirect porque necesitamos que entre al controlador a cargar
	        //return "redirect:../verEvento";
		 	mav.setViewName("verEvento"); 
		 	
			return mav;
	    }	
	
	
		@GetMapping(value = "/listarEventos")
		public ModelAndView listarEventos(@RequestParam(required= false, name="mostrarFuturos") String mostrarFuturos) {
			
			ModelAndView mav = new ModelAndView();
			List<Evento> lstEventos = null;
			
			if(mostrarFuturos==null || mostrarFuturos.equalsIgnoreCase("no")) {
				mostrarFuturos="no";
				lstEventos =  evento.ObtenerEventos();	
			}else if(mostrarFuturos.equalsIgnoreCase("Si")){
				//Buscar fechas a partir de la fecha y hora actual del sistema
				Date dt = new Date();
	  			//mav.addObject("lstEventos", evento.obtenerEventosFuturos(dt));
				lstEventos = evento.obtenerEventosFuturos(dt);
			}
 
			mav.addObject("lstEventos", lstEventos);
			mav.addObject("mostrarFuturos", mostrarFuturos);
			mav.setViewName("listarEventos");
			return mav;
		}		
		
}
