package es.uned.foederis.eventos.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.constantes.Constantes;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Horarios;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;;

@Controller
@RequestMapping("Evento") //Empieces url navegador y asi evitamos este en todos los metodos
public class EventosController { 
	
	@Autowired
	private IEventoService evento;
	
	@Autowired
	private IRolRepository rolRepo; 
	
	//** Ver evento.HTML ***//
		
	//NO FUNCIONA
	 @GetMapping("/baja")
	    public ModelAndView baja(@RequestParam(value="id") Evento eventoSeleccionado) {
		 Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 
		 //modificamos la columna confirmado a false
		 Usuario_Evento usuEv =  eventoSeleccionado.getEventoDeUnUsuario(user.getIdUsuario());
		 usuEv.setConfirmado(false);
		 
		 ModelAndView mav = new ModelAndView();    
		 mav.setViewName("listarFiltro");           
		 	    
			return mav;      
		 	
		 
	    }
	 
	 //NO ESTA ECHO
	 @GetMapping("/confirmar")
	    public String confirmar(@RequestParam(value="id") Integer idEvento) {
		 
		 
		 
		 //Salir del evento		 
		 evento.eliminar(idEvento);
		 	
		 //Usamos redirect porque necesitamos que entre al controlador a cargar
	        return "redirect:../listarEventos";
	    }
	 
	 
	//**Listar evento.HTML ***//
	 
	 /****
	  * Nos permite acceder desde el listado de eventos al evento seleccionado, donde poner ver lo relativo a el evento
	  * @param evento
	  * @return verEvento
	  */
	 @GetMapping("/entrar")
	    public ModelAndView entrar(@RequestParam(value="id") Evento evento) { 
		 
			Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		 	ModelAndView mav = new ModelAndView();
		 	
		 	//Objetos que necesito
		 	mav.addObject("eventoSeleccionado", evento);
		 	
		 	//Obtenemos el usuario_evento
		 	for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
				if(aux.getIdUsuario().getIdUsuario() == evento.getIdEvento())  {					
					mav.addObject("usuarioEventoSeleccionado", aux);
					break;
				}					                
			}			 	
		 			
		 	//Vista a la que vamos
		 	mav.setViewName("verEvento");     
		 	
			return mav; 
	    }	
	 

	 	@RequestMapping(value = "/listarFiltro")
	    public ModelAndView listarFiltro(@RequestParam(required= false, name="filtroListado") String filtroListado) {
	 		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	 	
	 		
		 ModelAndView mav = new ModelAndView();  
			List<Evento> lstEventos = new ArrayList<Evento>(); 
			
			if(filtroListado==null || filtroListado.equalsIgnoreCase("todos")) {  
				filtroListado="todos";
				//lstEventos =  evento.ObtenerEventos(user.getId());				
				
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
					if(aux.isConfirmado())  
						lstEventos.add(aux.getEvento());             
				}				
				
			}else if(filtroListado.equalsIgnoreCase("futuro")){
				lstEventos.clear();
				//Buscar fechas a partir de la fecha y hora actual del sistema
				Date dt = new Date();
				
				//lstEventos = evento.obtenerEventosFuturos(dt, user.getId()); 
				
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
					if(aux.isConfirmado()) {
						if(aux.getEvento().getFechaInicio().getDate() >= dt.getDate())
							lstEventos.add(aux.getEvento());					
						
					}
				}
				
				
			}else if(filtroListado.equalsIgnoreCase("hoy")){  
				lstEventos.clear();
				Date dt = new Date();
				
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
					if(aux.isConfirmado()) {
						if(aux.getEvento().getFechaInicio().getDate() == dt.getDate())
							lstEventos.add(aux.getEvento());

					}
				}
				   
			}else {  
				lstEventos.clear();
				
				//Ver sin confirmar
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {  
					if(!aux.isConfirmado()) {
						lstEventos.add(aux.getEvento());
					}
				}		
				
			}

			mav.addObject("lstEventos", lstEventos);
			mav.addObject("filtroListado", filtroListado);
			mav.addObject("rol", user.getRol()); 	 	 	   
			mav.addObject("userLogin", user);
			
			mav.setViewName("listarEventos");
			return mav;
	    }		
		
}
