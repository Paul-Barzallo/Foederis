package es.uned.foederis.eventos.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

	//** Ver evento.HTML ***//

	/**
	 * Carga la variable user, solo llamamos a este metodo cuando no ha sido cargado
	 * 
	 */
	public void iniciarUsuario(){
		user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	/**
	 * Devuelve el listado de eventos sin confirmar.
	 * Actualiza el evento, actualiza la variable confirmado a 0.
	 * @param model
	 * @return html
	 */
	@GetMapping("/baja")
	public String baja(@RequestParam(value="id") Evento eventoSeleccionado) {

		ActualizarConfirmacionEvento(0, eventoSeleccionado, new Horarios());

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
	public String confirmar(Model model, @RequestParam(value="seleccionHorario") String horarioElegido,Evento eventoSeleccionado) {		

		//Obtener el Horario
		List<Horarios> h = (List<Horarios>) HorarioRepo.findAll(); 

		int idHorarioElegido = Integer.parseInt(horarioElegido);
		Horarios elegido = h.get(idHorarioElegido);

		for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
			if(aux.getEvento().getIdEvento()==eventoSeleccionado.getIdEvento()) 
			{
				ActualizarConfirmacionEvento(1, aux.getEvento(), elegido);
			}
		}		

		return "redirect:../Evento/listarFiltro?filtroListado=sin";
	}


	/**
	 * Actualiza el evento indicando si el usuario confirma asistencia
	 * Si esta confirmando asistencia almacenamos el horario elegido.
	 */
	private void ActualizarConfirmacionEvento(int pValor, Evento eventoSeleccionado, Horarios pHorarioElegido) {

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

		//Objetos que necesito   
		mav.addObject("eventoSeleccionado", evento);     

		//Obtenemos el usuario_evento
		for(Usuario_Evento aux: user.getEventosDelUsuario()) {    
			if(aux.getIdUsuario().getIdUsuario() == evento.getIdEvento())  {					
				mav.addObject("usuarioEventoSeleccionado", aux);             
				break;
			}					                
		}  


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
			Date dt = new Date();				

			for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
				if(aux.getConfirmado()==1) {
					if(aux.getEvento().getFechaInicio().getDate() >= dt.getDate())
						lstEventos.add(aux.getEvento());			

				}
			}


		}else if(filtroListado.equalsIgnoreCase("hoy")){  
			lstEventos.clear();
			Date dt = new Date();

			for(Usuario_Evento aux: user.getEventosDelUsuario()) {   
				if(aux.getConfirmado()==1) {
					if(aux.getEvento().getFechaInicio().getDate() == dt.getDate())
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
			return eventoService.irANuevoEvento(model);
		}
		eventoService.mensajeNoAccesoEventos(model);
		return Vistas.HOME;
	}

}
