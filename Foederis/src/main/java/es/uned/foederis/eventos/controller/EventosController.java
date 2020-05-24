package es.uned.foederis.eventos.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.constantes.Rutas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Horarios;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.repository.IEventoUsuarioRepository;
import es.uned.foederis.eventos.repository.IHorarioRepository;
import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.sesion.constantes.UsuarioConstantes;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;
import es.uned.foederis.sesion.repository.IUsuarioRepository;;

@Controller
@RequestMapping("Evento") //Empieces url navegador y asi evitamos este en todos los metodos
public class EventosController { 
	
	@Autowired
	private IEventoService eventoService;

	@Autowired
	private IRolRepository rolRepo; 
	
	@Autowired
	private IEventoUsuarioRepository eventoUsuarioRepo;
	
	@Autowired
	IUsuarioRepository usuRepo;
	
	@Autowired
	IHorarioRepository HorarioRepo;
	
	@Autowired
	private Usuario user;
	
	@Autowired
	private AdministracionService administracionService;

	//** Ver evento.HTML ***//
	
	/**
	 * Carga la variable user, solo llamamos a este metodo cuando no ha sido cargado
	 * 
	 */
	public void iniciarUsuario(){
		 user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
//	@GetMapping("/verSala")
//    public ModelAndView verSala(@RequestParam(value="id") Sala sala) { 
//		
////		 modelMap.put("sala", sala.getIdSala());
//	
//	 	ModelAndView mav = new ModelAndView();	 	  	   
//	 
////	 	mav.addObject("salas", sala);   		 	
//	 	
//	 	//Vista a la que vamos
//	 	mav.setViewName("salas");         
//	 	
//		return mav; 		
//    }
		
	/**
	 * Devuelve el listado de eventos sin confirmar.
	 * Actualiza el evento, actualiza la variable confirmado a 0.
	 * @param model
	 * @return html
	 */
	 @GetMapping("/baja")
	    public String baja(@RequestParam(value="id") Evento eventoSeleccionado) {
		 
		 ActualizarConfirmacionEvento(0, eventoSeleccionado, new Horarios(), false);
		     
		 return "redirect:../Evento/listarFiltro?filtroListado=sin"; 
	 }
	 
		/**
		 * Devuelve el listado de eventos sin confirmar.
		 * Actualiza el evento, actualiza la variable confirmado a 1 y
		 * y pasandole el horario elegido por el usuario.
		 * @param model
		 * @return html
		 */
	@PostMapping("/confirmar")
	public String confirmar(Model model, @RequestParam("checkPresencial") boolean chkPresencial, @RequestParam(value="seleccionHorario") String horarioElegido,Evento eventoSeleccionado) {		
		 
		//Obtener el Horario
		List<Horarios> h = (List<Horarios>) HorarioRepo.findAll(); 
		
		int idHorarioElegido = Integer.parseInt(horarioElegido);
		Horarios elegido = h.get(idHorarioElegido);
		
		for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
			if(aux.getEvento().getIdEvento()==eventoSeleccionado.getIdEvento()) 
			{
				ActualizarConfirmacionEvento(1, aux.getEvento(), elegido, chkPresencial);
			}
		}		
				 
		 return "redirect:../Evento/listarFiltro?filtroListado=sin";
    }
	 
	 
	/**
	 * Actualiza el evento indicando si el usuario confirma asistencia
	 * Si esta confirmando asistencia almacenamos el horario elegido.
	 */
	 private void ActualizarConfirmacionEvento(int pValor, Evento eventoSeleccionado, Horarios pHorarioElegido, boolean pCheckPresencial) {
		 
		 if(user.getIdUsuario()== null)
			 this.iniciarUsuario();
		 
		 Usuario_Evento usuarioEvento =  eventoSeleccionado.getEventoDeUnUsuario(user.getIdUsuario());
		 
		 //Obtengo el usuario evento de la variable user para poner modificarla en el y guardarla actualizada.
		 List<Usuario_Evento> lstUsuariosEvento = user.getEventosDelUsuario();
		 Usuario_Evento usuEvento = lstUsuariosEvento.get(usuarioEvento.getIdUsuarioEvento());		 
	
		 usuEvento.setConfirmado(pValor);
		 
		 if(pHorarioElegido != null) {
			 
			 //Guardamos horario
			 usuEvento.setHorario(pHorarioElegido);		
			 usuEvento.setPresencial(pCheckPresencial);
			 
		 }
		 
		 usuRepo.save(user); 		 
	 } 
 
	 
	//**Listar evento.HTML ***//
	 
	 /****
	  * Nos permite acceder desde el listado de eventos al evento seleccionado, donde poner ver lo relativo a el evento
	  * @param evento
	  * @return verEvento
	  */
	 @GetMapping("/entrar")
	    public ModelAndView entrar(@RequestParam(value="id") Evento evento) { 
		 if(user.getIdUsuario()== null)
			 this.iniciarUsuario();
			//user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		 	ModelAndView mav = new ModelAndView();
		 	  	   
		 	Usuario_Evento usuarioEv= new Usuario_Evento();
		 	
		 	//Obtenemos el usuario_evento
		 	for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
				if(aux.getEvento().getIdEvento() == evento.getIdEvento())  {					
					usuarioEv= aux;
					break;
				}					                
			}   		 	
		 	
		 	mav.addObject("eventoSeleccionado", evento);  
		 	mav.addObject("usuarioEventoSeleccionado", usuarioEv);
		 	mav.addObject("userLogin", user);   		 	
		 	
		 	//Vista a la que vamos
		 	mav.setViewName("verEvento");         
		 	
			return mav; 
			
	    }	
	 
	 /****
	  * Nos muestra el listado de eventos del usuario, podemos filtrar segun la fecha y segun si hemos confirmado o rechazado
	  * @param filtroListado
	  * @return listarEventos
	  */
	 	@RequestMapping(value = "/listarFiltro")
	    public ModelAndView listarFiltro(@RequestParam(required= false, name="filtroListado") String filtroListado) {

	 		if(user.getIdUsuario()== null)
				 this.iniciarUsuario();	 
	 		
	 		//Fecha de hoy formato timestamp
	 		Date dt = new Date();
			 long time = dt.getTime();
			 Timestamp ts = new Timestamp(time);
	 		
	 		ModelAndView mav = new ModelAndView();  
			List<Evento> lstEventos = new ArrayList<Evento>(); 
			
			if(filtroListado==null || filtroListado.equalsIgnoreCase("todos")) {  
				filtroListado="todos";
				
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
					if(aux.getConfirmado()==1)  
						lstEventos.add(aux.getEvento());             
				}				
				
			}else if(filtroListado.equalsIgnoreCase("futuro")){
				lstEventos.clear();
				//Buscar fechas a partir de la fecha y hora actual del sistema
								
			
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
					if(aux.getConfirmado()==1) {
						
						//Si hay horario elegido por el JP
						if((aux.getEvento().getHorarioElegido() != null && aux.getEvento().getHorarioElegido().getHorario_Fecha_Inicio().after(ts))) {
							lstEventos.add(aux.getEvento());	
						}else if(aux.getHorario()!= null &&  aux.getHorario().getHorario_Fecha_Inicio().after(ts)) {
							//Si el usuario ya ha confirmado su horario
							lstEventos.add(aux.getEvento());	
						}
							
					}
				}
				
				
			}else if(filtroListado.equalsIgnoreCase("hoy")){  
				lstEventos.clear();
				
				
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
					if(aux.getConfirmado()==1) {
						if((aux.getHorario()!= null && aux.getHorario().getHorario_Fecha_Fin().getDate() ==ts.getDate()) ||( aux.getEvento().getHorarioElegido().getHorario_Fecha_Inicio().getDate() ==ts.getDate()))
							lstEventos.add(aux.getEvento());

					}
				}
			}else if(filtroListado.equalsIgnoreCase("rechazados")){   
				lstEventos.clear();
				
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {  
					
						if(aux.getConfirmado()==0) {
							lstEventos.add(aux.getEvento());  
					} 
				}
			}else {  
				lstEventos.clear();
				
				//Ver sin confirmar
				for(Usuario_Evento aux: user.getEventosDelUsuario()) {  
					if(aux.getConfirmado()==-1) {
						lstEventos.add(aux.getEvento());
					}
				}					 
			}    
			
			//Objetos que pasamos a la vista
			mav.addObject("lstEventos", lstEventos);
			mav.addObject("filtroListado", filtroListado);
			mav.addObject("userLogin", user);
			
			mav.setViewName("listarEventos");
			return mav;
	    }	
		
	/**
	 * Devuelve el formulario de evento vacio para crear un nuevo evento
	 * Verifica que la petición la haga un jefe de proyecto si no redirige a HOME
	 * Busca y carga los roles en el fomrulario
	 * Genera un nuevo usuario vacio donde poner los datos
	 * @param model
	 * @return html
	 */
	@GetMapping(Rutas.NUEVO)
	public String getFormularioNuevo(Model model) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isJefeProyecto()) {
			Evento evento = new Evento();
			model.addAttribute(Atributos.EVENTO, evento);
			administracionService.cargarParamsBusqSalas(model);
			administracionService.cargarUsuarios(model, UsuarioConstantes.ESTADO, Long.toString(UsuarioConstantes.ROL_ADMIN));
			return eventoService.irANuevoEvento(model);
		}
		eventoService.mensajeNoAccesoEventos(model);
		return Vistas.HOME;
	}

	/**
	 * Peticion ajax que busca las salas activas que coincidan con los parametros de busqueda
	 * La busqueda no es case sensitive
	 * Se busca que contenga el valor escrito no que empiece por el
	 * @param model
	 * @param paramBusq
	 * @param valorBusq
	 * @return
	 */
	@GetMapping(Rutas.BUSQ_SALAS)
	@ResponseBody
	public ModelAndView ajaxSalas(Model model, Optional<String> paramBusq, Optional<String> valorBusq) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isJefeProyecto()) {
			String parametro = (paramBusq.isPresent())? paramBusq.get() : "";
			String valor = (valorBusq.isPresent())? valorBusq.get() : "";
			// se cargan las salas, si los valores de busqueda estan vacios se carga la lista completa
			eventoService.cargarSalas(model, parametro, valor);
			return new ModelAndView("fragmentos :: lista_salas");
		} 
		administracionService.mensajeNoAccesoSalas(model);;
		return new ModelAndView(Vistas.HOME);
	}

}
