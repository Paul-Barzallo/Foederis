package es.uned.foederis.eventos.controller;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import es.uned.foederis.administracion.service.AdministracionService;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.constantes.Rutas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.eventos.EventoConstantes;
import es.uned.foederis.eventos.constantes.usuarioEventoConstantes;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Horarios;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.repository.IEventoRepository;
import es.uned.foederis.eventos.repository.IHorarioRepository;
import es.uned.foederis.eventos.repository.IUsuarioEventoRepository;
import es.uned.foederis.eventos.service.IEventoService;
import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.salas.repository.ISalaRepository;
import es.uned.foederis.sesion.constantes.UsuarioConstantes;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuarioRepository;
import es.uned.foederis.sesion.token.JWTInvitado;
import io.jsonwebtoken.Claims;;

@Controller
@RequestMapping("Evento")
public class EventosController {

	@Autowired
	private IEventoService eventoService;

	@Autowired
	private ISalaRepository salaRepo;

	@Autowired
	private IUsuarioRepository usuarioRepo;
	
	@Autowired
	private IUsuarioEventoRepository usuarioEventoRepo;

	@Autowired
	private IEventoRepository eventoRepo;

	@Autowired
	private IUsuarioRepository usuRepo;

	@Autowired
	private IHorarioRepository HorarioRepo;

	private Usuario user;

	@Autowired
	private AdministracionService administracionService;
	
	@Autowired
	private JWTInvitado jwtInvitado;

	@Autowired
	@Qualifier("dateTimeFormat")
	private SimpleDateFormat dateTimeFormat;

	// ** Ver evento.HTML ***//

	/**
	 * Carga la variable user con el usuario cargado.
	 */
	public void iniciarUsuario() {
		user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
			
	/**
	 * Confirma la baja del usuario invitado al evento seleccionado
	 * El usuario será dado de baja y aparecera el evento en el listado de rechazados.
	 * @param model
	 * @param eventoSeleccionado
	 * @param redirectAtrributtes
	 * @return 
	 */
	@GetMapping(Rutas.EVENTOS_BAJA)
	public String baja(Model model, @RequestParam(value = "id") Evento eventoSeleccionado,
			RedirectAttributesModelMap redirectAtrributtes) {

		actualizarConfirmacionEvento(usuarioEventoConstantes.CONFIRMADO_NO, eventoSeleccionado, null, false);

		eventoService.mensajeConfirmacion(model);
		
		
		redirectAtrributtes.addFlashAttribute(Atributos.ALERTA_TITULO, model.getAttribute(Atributos.ALERTA_TITULO));
		redirectAtrributtes.addFlashAttribute(Atributos.ALERTA, model.getAttribute(Atributos.ALERTA));

		return "redirect:/Evento/listarFiltro?filtroListado=rechazados";
	}

		
	/**
	 * Confirmación del invitado al evento seleccionado.
	 * El usuario confirma el check de presencial y el horario preferido.
	 * Tras confirmar, si el evento tiene aforo libre le añade como asistente.
	 * @param model
	 * @param chkPresencial, obtenemos el valor de combobox
	 * @param horarioElegido, obtenemos el radioButton con el id del horario seleccionado
	 * @param eventoSeleccionado, obtenemos el evento seleccionado
	 * @param redirectAtrributtes
	 * @return
	 */
	@PostMapping(Rutas.EVENTOS_CONFIRMAR_INVITADO)
	public String confirmar(Model model,
			@RequestParam(value = "checkPresencial", required = false) boolean chkPresencial,
			@RequestParam(value = "seleccionHorario", required = false) String horarioElegido, Evento eventoSeleccionado,
			RedirectAttributesModelMap redirectAtrributtes) {

		Horarios elegido= new Horarios();

		if(horarioElegido != null) {
			// Obtener el objeto Horario que se ha seleccionado por el usuario.
			List<Horarios> h = (List<Horarios>) HorarioRepo.findAll();
	
			int idHorarioElegido = Integer.parseInt(horarioElegido);
			elegido = h.get(idHorarioElegido-1);
		
		}			

		for (Usuario_Evento aux : user.getEventosDelUsuario()) {
			if (aux.getEvento().getIdEvento() == eventoSeleccionado.getIdEvento()) {
				actualizarConfirmacionEvento(usuarioEventoConstantes.CONFIRMADO_SI, aux.getEvento(), elegido, chkPresencial);
			}
		}

		eventoService.mensajeConfirmacion(model);


		redirectAtrributtes.addFlashAttribute(Atributos.ALERTA_TITULO, model.getAttribute(Atributos.ALERTA_TITULO));
		redirectAtrributtes.addFlashAttribute(Atributos.ALERTA, model.getAttribute(Atributos.ALERTA));

		return "redirect:/Evento/listarFiltro?filtroListado=sin";
	}

	/**
	 * Funcion que se ejecuta cuando un invitado al evento confirma o rechaza su
	 * asistencia al evento. Se viene aqui desde confirmar y baja. Actualiza el
	 * evento indicando si el usuario confirma asistencia. Si esta confirmando
	 * asistencia almacenamos el horario elegido.
	 */
	private void actualizarConfirmacionEvento(int pValor, Evento eventoSeleccionado, Horarios pHorarioElegido,
			boolean pCheckPresencial) {

		this.iniciarUsuario();

		Usuario_Evento usuarioEvento = eventoSeleccionado.getUsuariosEvento(user.getIdUsuario());

		// Obtengo el usuario evento de la variable user para poner modificarla en el y
		// guardarla actualizada.
		List<Usuario_Evento> lstUsuariosEvento = user.getEventosDelUsuario();
		Usuario_Evento usuEvento = lstUsuariosEvento.stream().filter(c -> c.getIdUsuarioEvento()==usuarioEvento.getIdUsuarioEvento()).findFirst().get() ;

		
		//Si es jefe de proyecto del evento solo se guarda si es presencial o no la reunion
		if(user.getIdUsuario() == eventoSeleccionado.getUsuarioCreador().getIdUsuario()) {
			usuEvento.setPresencial(pCheckPresencial);
		}else {
			// Confirmación evento
			usuEvento.setConfirmado(pValor);

			// Si se da de baja comprobar si estuviera como asistente
			if (pValor == usuarioEventoConstantes.CONFIRMADO_NO && usuEvento.isAsistente())
				usuEvento.setAsistente(false);

			if (pHorarioElegido != null) {

				// Guardamos horario
				usuEvento.setHorario(pHorarioElegido);
				usuEvento.setPresencial(pCheckPresencial);

				// Comprobamos si hay plazas libres
				if (usuEvento.getEvento().getSalaEvento().getAforo() > usuEvento.getEvento().getTotalAsistentesEvento())
					usuEvento.setAsistente(true);
			}
		}		

		usuRepo.save(user);
	}

	/**
	 * El creador del evento confirma todo lo relativo al evento creado.
	 * Se confirma su tipo de asistencia, el horario mas elegido del evento,
	 * y la lista con los usuarios que van a asistir.
	 * @param model
	 * @param lstCheckedAsistentes, listado de los usuarios elegidos para asistir
	 * @param horarioVotado, gemos el horario mas votado por los invitados
	 * @param chkPresencial, tipo de asistencia del creador
	 * @param idEvento, id del evento seleccionado.
	 * @param idSala, si se modifica la sala guardamos la nueva sala donde se celebrará el evento
	 * @param redirectAtrributtes
	 * @return
	 */
	@PostMapping(Rutas.EVENTOS_CONFIRMAR_CREADOR_EVENTO)
	public String confirmarAsistenciaEvento(Model model,
			@RequestParam(value = "checkAsistenteElegido", required = false) List<Integer> lstCheckedAsistentes,
			@RequestParam(value = "horarioVotado") String horarioVotado,
			@RequestParam(value = "checkPresencial") String chkPresencial,
			@RequestParam(value = "eventoSeleccionado") Integer idEvento,
			@RequestParam(value = "idSala") String idSala,
			RedirectAttributesModelMap redirectAtrributtes) {
		
		boolean mostrarMensajeConfirmacion= false;
		
		Evento evento = user.getEventosDelUsuario().stream().filter(c -> c.getEvento().getIdEvento() == idEvento)
				.findFirst().get().getEvento();
		
		//Si ha existido cambio de sala lo guardamos
		if(!idSala.isEmpty()) {
			
			Optional<Sala> salaCambiada = salaRepo.findById(Long.parseLong(idSala));
			evento.setSalaEvento(salaCambiada.get());
			
			usuRepo.save(user);
			
			mostrarMensajeConfirmacion= true;			
			
			//Actualizar sala actual del model
			eventoService.mensajeInfoSala(model, String.valueOf(Long.parseLong(idSala)));
		}

		// Guardo el horario que ha sido mas elegido
		if (horarioVotado.isEmpty() || horarioVotado == null) {							
		} else {

			// Control de aforo
			int aforo = user.getEventosDelUsuario().stream().filter(obj -> obj.getEvento().getIdEvento() == idEvento)
					.findFirst().get().getEvento().getSalaEvento().getAforo();	
			
			//Guardamos el horario
			Optional<Horarios> horarioMasVotado = HorarioRepo.findById(Integer.parseInt(horarioVotado));
			evento.setHorarioElegido(horarioMasVotado.get());
			
			//Se actualiza el aforo
					
			//El creador del evento siempre tendra estos valores por defecto y siempre asistira
			//Si es el usuario logado creador del evento no puede darse de baja 
			Usuario_Evento userlogado =  user.getEventosDelUsuario().stream().
					filter(c->c.getEvento().getIdEvento() == idEvento).findFirst().get();
			userlogado.setConfirmado(usuarioEventoConstantes.CONFIRMADO_SI);
			userlogado.setAsistente(true);
			userlogado.setHorario(horarioMasVotado.get());
			aforo--;

			// marco todos los usuarios-evento del evento con asistencia a 0 y solo en los
			// que traigo de la vista los pongo a true siempre que tengan el mismo horario;
			for(Usuario_Evento aux : evento.getEventosDelUsuario()) {				
				
				if(aux.getUsuario().getIdUsuario() == user.getIdUsuario()) {
									
				//Si es usuario clickado
				}else if(lstCheckedAsistentes.contains(aux.getIdUsuarioEvento()) && aforo > 0 &&
						(aux.getConfirmado()==usuarioEventoConstantes.CONFIRMADO_SI 
						&& aux.getHorario().getIdHorario() == horarioMasVotado.get().getIdHorario())) {
					
					aux.setAsistente(true);
					aux.setConfirmado(usuarioEventoConstantes.CONFIRMADO_SI);
					
					aforo--;
				}//resto, hay que darles de baja
				else {
					aux.setConfirmado(usuarioEventoConstantes.CONFIRMADO_NO);
					aux.setAsistente(false);
				}	
								
				usuarioEventoRepo.save(aux);
			}				
			
			usuRepo.save(user);
			
			mostrarMensajeConfirmacion= true;
		}	
		
		if(mostrarMensajeConfirmacion)
		{
			eventoService.mensajeConfirmacion(model);
			redirectAtrributtes.addFlashAttribute(Atributos.ALERTA_TITULO, model.getAttribute(Atributos.ALERTA_TITULO));
			redirectAtrributtes.addFlashAttribute(Atributos.ALERTA, model.getAttribute(Atributos.ALERTA));
		}
		
		return "redirect:/Evento/listarFiltro?filtroListado=todos";

	}

	// **Listar evento.HTML ***//
	/**
	 * Nos permite acceder desde el listado de eventos a los detalles del evento seleccionado,
	 * @param model
	 * @param evento
	 * @return
	 */
	@GetMapping(Rutas.EVENTOS_VER_DETALLE)
	public ModelAndView entrar(Model model, @RequestParam(value = "id") Evento evento
			) {
		this.iniciarUsuario();

		ModelAndView mav = new ModelAndView();

		Usuario_Evento usuarioEv = new Usuario_Evento();

		// Obtenemos el usuario_evento
		for (Usuario_Evento aux : user.getEventosDelUsuario()) {
			if (aux.getEvento().getIdEvento() == evento.getIdEvento()) {
				usuarioEv = aux;
				break;
			}
		}

		// SOlo para el jefe de prroyecto creador muestro el horario mas elegido
		if (user.getIdUsuario() == evento.getUsuarioCreador().getIdUsuario()) {
			
			//Si ya existe horario no se vuelve a calcular
//			if(evento.getHorarioElegido()== null) 
			{		

				List<Integer> lstH = new ArrayList<Integer>();
	
				List<Usuario_Evento> lstusuEvento = (List<Usuario_Evento>) usuarioEventoRepo.findAll();
	
				lstusuEvento = lstusuEvento.stream().filter(c -> c.getEvento().getIdEvento() == evento.getIdEvento())
						.collect(Collectors.toList());
	
				lstusuEvento.forEach(c -> {
					if (c.getHorario() != null && c.getHorario().getIdHorario() > -1) {
						lstH.add(c.getHorario().getIdHorario());
					} 
				});
				if(lstH.size()!= 0) {				
					
					Map<Integer, Long> repeticiones = lstH.stream()
							.collect(Collectors.groupingBy(w -> w, Collectors.counting()));
		
					Stream<Map.Entry<Integer, Long>> ordenados = repeticiones.entrySet().stream()
							.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
		
					Integer masFrecuente = ordenados.findFirst().get().getKey();
		
					// mostraremos el mas elegido a modo información
					Optional<Horarios> horarioMasVotado = HorarioRepo.findById(masFrecuente);
		
					if (horarioMasVotado.isEmpty() || horarioMasVotado == null)
						mav.addObject("horarioVotado", null);
					else
						mav.addObject("horarioVotado", horarioMasVotado.get());
				}
			}
			//Si tenemos mas usuarios confirmados que aforo en la sala mostramos modal para sugerir un cambio de sala
			if(evento.getSalaEvento().getAforo() < evento.getTotalConfirmadosEvento() && evento.getEstado().equals(EventoConstantes.ESTADO_INACTIVO))
				eventoService.mensajeInfoAforo(model);
		}
				
		mav.addObject("eventoSeleccionado", evento);
		mav.addObject("usuarioEventoSeleccionado", usuarioEv);
		mav.addObject("userLogin", user);

		// cargar modal con el atributo sala, aparecera cuando pulse el numero de la
		// sala.
		eventoService.mensajeInfoSala(model, String.valueOf(evento.getSalaEvento().getIdSala()));		

		// Vista a la que vamos
		mav.setViewName("verEvento");

		return mav;

	}
		
	/**
	 * Calculamos el cambio horario local con la hora de Nueva York
	 * @return Timestamp con el cambio horario al huso actual
	 */
	private Timestamp getHoraNY() {
		
		ZonedDateTime fecha = ZonedDateTime.now();
		
		ZonedDateTime prueba=  fecha.withZoneSameInstant(ZoneId.of("America/New_York"));
		 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedString = prueba.format(formatter);
		
		 Timestamp timestamp2 = Timestamp.valueOf(formattedString);
		 
		 
		return timestamp2;
	}
	
	/**
	 * Seteamos el estado de los eventos del usuario para comprobar si el evento esta online
	 */
	private void actualizarEstadoEventos() {
		
		Timestamp ts = getHoraNY();
		
		//Descontamos 5 minutos a la hora actual
		ts.setTime(ts.getTime() - TimeUnit.MINUTES.toMillis(5));
		
		//Si el estado es inactivo y la fecha actual menos 5 minutos no es antes de la fecha del evento seteamos estado
		user.getEventosDelUsuario().stream().
		forEach(c -> { 
			if(c.getEvento().getEstado() == EventoConstantes.ESTADO_INACTIVO &&					 
					!c.getEvento().getHorarioElegido().getHorario_Fecha_Inicio().before(ts)) {
				c.getEvento().setEstado(EventoConstantes.ESTADO_ONLINE);
			}			
		});
		
		usuRepo.save(user);
	}
	
	/**
	 * Muestra el listado de eventos creados por el usuario creador de eventos logado.
	 * Este acceso esta restringido a los usuarios con rol jefe de proyecto o admin, 
	 * que son los unicos que pueden crear eventos.
	 * @return
	 */
	@RequestMapping(Rutas.EVENTOS_LISTAR_FILTRO_CREADOR)
	public ModelAndView listarFiltroCreador() {
		
		this.iniciarUsuario();		
		
		//Actualizar estados a finalizado dependiendo de la hora
		actualizarEstadoEventos();		

		ModelAndView mav = new ModelAndView();
		List<Evento> lstEventos = new ArrayList<Evento>();
		
		lstEventos = user.getEventosCreados();
		
		mav.addObject("lstEventos", lstEventos);

		mav.setViewName("listarEventosCreador");
		return mav;
	}

	/**
	 * Nos muestra el listado de eventos del usuario, podemos filtrar por:
	 * Todos los evento, los de hoy, los rechazados, los pendientes de confirmar y los futuros
	 * Segun el filtro que se realice en la vista.
	 * @param filtroListado, obtenemos el tipo de filtro seleccionado por el usuario.
	 * @return
	 */
	@RequestMapping(Rutas.EVENTOS_LISTAR_FILTRO)
	public ModelAndView listarFiltro(@RequestParam(required = false, name = "filtroListado") String filtroListado) {

		this.iniciarUsuario();
		
		actualizarEstadoEventos();

		//Fecha de hoy transformada a la time Zone de NY
		Timestamp ts = getHoraNY();
		

		ModelAndView mav = new ModelAndView();
		List<Evento> lstEventos = new ArrayList<Evento>();
		
		//Filtros de seleccion

		if (filtroListado == null || filtroListado.equalsIgnoreCase(usuarioEventoConstantes.FILTRO_TODOS)) {
			filtroListado = usuarioEventoConstantes.FILTRO_TODOS;

			for (Usuario_Evento aux : user.getEventosDelUsuario()) {
				if (aux.getConfirmado() == usuarioEventoConstantes.CONFIRMADO_SI)
					lstEventos.add(aux.getEvento());
			}

		} else if (filtroListado.equalsIgnoreCase(usuarioEventoConstantes.FILTRO_FUTURO)) {
			lstEventos.clear();
			// Buscar fechas a partir de la fecha y hora actual del sistema

			for (Usuario_Evento aux : user.getEventosDelUsuario()) {
				if (aux.getConfirmado() == usuarioEventoConstantes.CONFIRMADO_SI) {

					// Si hay horario elegido por el JP
					if ((aux.getEvento().getHorarioElegido() != null
							&& aux.getEvento().getHorarioElegido().getHorario_Fecha_Inicio().after(ts))) {
						lstEventos.add(aux.getEvento());
					} else if (aux.getHorario() != null && aux.getHorario().getHorario_Fecha_Inicio().after(ts)) {
						// Si el usuario ya ha confirmado su horario
						lstEventos.add(aux.getEvento());
					}
				}
			}

		} else if (filtroListado.equalsIgnoreCase(usuarioEventoConstantes.FILTRO_HOY)) {
			lstEventos.clear();
			
			for (Usuario_Evento aux : user.getEventosDelUsuario()) {
				if (aux.getConfirmado() == usuarioEventoConstantes.CONFIRMADO_SI) {
					if ((aux.getHorario() != null && aux.getHorario().getHorario_Fecha_Inicio().getDate() == ts.getDate())
							|| (aux.getEvento().getHorarioElegido() != null && aux.getEvento().getHorarioElegido().getHorario_Fecha_Inicio().getDate() == ts.getDate()
									))
						lstEventos.add(aux.getEvento());

				}
			}
		} else if (filtroListado.equalsIgnoreCase(usuarioEventoConstantes.FILTRO_RECHAZADOS)) {
			lstEventos.clear();

			for (Usuario_Evento aux : user.getEventosDelUsuario()) {

				if (aux.getConfirmado() == usuarioEventoConstantes.CONFIRMADO_NO) {
					lstEventos.add(aux.getEvento());
				}
			}
		} else {
			lstEventos.clear();

			// Ver sin confirmar
			for (Usuario_Evento aux : user.getEventosDelUsuario()) {
				if (aux.getConfirmado() == usuarioEventoConstantes.CONFIRMADO_PENDIENTE) {
					lstEventos.add(aux.getEvento());
				}
			}
		}

		// Objetos que pasamos a la vista
		mav.addObject("lstEventos", lstEventos);
		mav.addObject("filtroListado", filtroListado);
		mav.addObject("userLogin", user);
		
		mav.setViewName("listarEventos");
		return mav;
	}

	/**
	 * Devuelve el formulario de evento vacio para crear un nuevo evento Verifica
	 * que la petición la haga un jefe de proyecto si no redirige a HOME Busca y
	 * carga los roles en el fomrulario Genera un nuevo usuario vacio donde poner
	 * los datos
	 * 
	 * @param model
	 * @return html
	 */
	@GetMapping(Rutas.NUEVO)
	public String getFormularioNuevo(Model model) {
		Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdminOrJP()) {
			Evento evento = new Evento();
			model.addAttribute(Atributos.EVENTO, evento);
			model.addAttribute(Atributos.USER, user);
			administracionService.cargarParamsBusqSalas(model);
			administracionService.cargarUsuarios(model, UsuarioConstantes.ESTADO, null);
			return eventoService.irANuevoEvento(model);
		}
		eventoService.mensajeNoAccesoEventos(model);
		return Vistas.HOME;
	}

	/**
	 * Peticion ajax que busca las salas activas que coincidan con los parametros de
	 * busqueda La busqueda no es case sensitive Se busca que contenga el valor
	 * escrito no que empiece por el
	 * 
	 * @param model
	 * @param paramBusq
	 * @param valorBusq
	 * @return
	 */
	@GetMapping(Rutas.BUSQ_SALAS)
	@ResponseBody
	public ModelAndView ajaxSalas(Model model, Optional<String> paramBusq, Optional<String> valorBusq) {

		Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdminOrJP()) {
			String parametro = (paramBusq.isPresent()) ? paramBusq.get() : "";
			String valor = (valorBusq.isPresent()) ? valorBusq.get() : "";
			// se cargan las salas, si los valores de busqueda estan vacios se carga la
			// lista completa
			eventoService.cargarSalas(model, parametro, valor);
			return new ModelAndView("fragmentos :: lista_salas");
		}
		eventoService.mensajeNoAccesoEventos(model);
		return new ModelAndView(Vistas.HOME);
	}

	/**
	 * Guarda la nueva información del evento en la db
	 * 
	 * @param model
	 * @param evento
	 * @param result errores de validación del formulario
	 * @return
	 * @throws ParseException
	 */
	@PostMapping(Rutas.GUARDAR)
	public String postGuardarEvento(Model model, HttpServletRequest request, @Validated Evento evento, BindingResult result) throws ParseException {
		Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdminOrJP()) {
			Horarios horario;
			Timestamp fechaIni;
			Timestamp fechaFin;
			Time horaIni;
			Time horaFin;
			long ms;
			boolean horaIniIncorrecta;
			boolean horaFinIncorrecta;
			
			String idSala = request.getParameter("idSala");
			String[] idUsers = request.getParameterValues("usuarios");
			String hApertura1 = request.getParameter("horarioApertura1");
			String hApertura2 = request.getParameter("horarioApertura2");
			String hApertura3 = request.getParameter("horarioApertura3");
			String hCierre1 = request.getParameter("horarioCierre1");
			String hCierre2 = request.getParameter("horarioCierre2");
			String hCierre3 = request.getParameter("horarioCierre3");
			
			if (idSala.isEmpty()) {
				result.rejectValue("estado", "NoBlanco.evento.salaEvento");
				model.addAttribute(Atributos.EVENTO, evento);
				model.addAttribute(Atributos.USER, user);
				administracionService.cargarParamsBusqSalas(model);
				administracionService.cargarUsuarios(model, UsuarioConstantes.ESTADO, null);
		        return eventoService.irANuevoEvento(model);
			}
			Sala sala = salaRepo.findById(Long.parseLong(idSala)).get();

			List<Usuario_Evento> usuariosEvento = new ArrayList<>();
			List<Horarios> horarios = new ArrayList<>();
			List<Usuario> usuarios = new ArrayList<>();
			
			String[] idUsuarios = new String[idUsers.length+1];
			idUsuarios[0] = user.getIdUsuario().toString();
			for (int i=0; i<idUsers.length; i++) {
				idUsuarios[i+1] = idUsers[i];
			}
			if (idUsuarios == null || idUsuarios.length<4) {
				result.rejectValue("estado", "AforoMin.evento");
				model.addAttribute(Atributos.EVENTO, evento);
				model.addAttribute(Atributos.USER, user);
				administracionService.cargarParamsBusqSalas(model);
				administracionService.cargarUsuarios(model, UsuarioConstantes.ESTADO, null);
		        return eventoService.irANuevoEvento(model);
			} else {
				for (String idUsuario : idUsuarios) {
					Usuario usuario = usuRepo.findById(Long.parseLong(idUsuario)).get();
					usuarios.add(user);
					Usuario_Evento usuarioEvento = new Usuario_Evento();
					usuarioEvento.setUsuario(usuario);
					usuarioEvento.setEvento(evento);
					if (usuario.getIdUsuario() == user.getIdUsuario()) {
						usuarioEvento.setAsistente(true);
						usuarioEvento.setConfirmado(1);
					}
					usuariosEvento.add(usuarioEvento);
					usuario.addEvento(usuarioEvento);
					evento.addUsuarioEvento(usuarioEvento);
				}
			}
			
			if ( (hApertura1.isBlank() || hCierre1.isBlank()) &&  (hApertura2.isBlank() || hCierre2.isBlank()) &&  (hApertura3.isBlank() || hCierre3.isBlank())) {
				result.rejectValue("estado", "HorarioMin.evento");
			}
			
			if (hApertura1 != null && !hApertura1.isBlank() && hCierre1 != null && !hCierre1.isBlank()) {
				horario = new Horarios();
				ms = dateTimeFormat.parse(hApertura1).getTime();
				fechaIni = new Timestamp(ms);
				horaIni = new Time(ms);
				ms = dateTimeFormat.parse(hCierre1).getTime();
				fechaFin = (new Timestamp(ms));
				horaFin = new Time(ms);
				
				horaIniIncorrecta = horaIni.getHours()<sala.getHoraInicio().getHours() || (horaIni.getHours()>horaFin.getHours() && horaIni.getHours()>=sala.getHoraFin().getHours());
				horaFinIncorrecta = horaFin.getHours()>sala.getHoraFin().getHours() || (horaIni.getHours()>horaFin.getHours() && horaFin.getHours()<=sala.getHoraInicio().getHours());
				if (horaIniIncorrecta || horaFinIncorrecta) {
					result.rejectValue("estado", "Cerrado1.evento.salaEvento");
				}
				if (fechaFin.getTime() - fechaIni.getTime() > 10*60*60*1000) { // Se comprueba que la reunion no dure mas de 10h
					result.rejectValue("estado", "TimeMax1.evento.salaEvento");
				}
				if (fechaFin.compareTo(fechaIni) <= 0) {
					result.rejectValue("estado", "IniLessFin1.evento.salaEvento");
				}
				if (salaRepo.findBySalaOcupada(Long.parseLong(idSala), fechaIni, fechaFin).isPresent()) {
					result.rejectValue("estado", "Ocupado1.evento.salaEvento");
				} 
				horario.setHorario_Fecha_Inicio(fechaIni);
				horario.setHorario_Fecha_Fin(fechaFin);
				horario.setEvento(evento);
				horarios.add(horario);
				evento.addHorario(horario);
			}
			if (hApertura2 != null && !hApertura2.isBlank() && hCierre2 != null && !hCierre2.isBlank()) {
				horario = new Horarios();
				ms = dateTimeFormat.parse(hApertura2).getTime();
				fechaIni = new Timestamp(ms);
				horaIni = new Time(ms);
				ms = dateTimeFormat.parse(hCierre2).getTime();		
				fechaFin = new Timestamp(ms);
				horaFin = new Time(ms);
				
				horaIniIncorrecta = horaIni.getHours()<sala.getHoraInicio().getHours() || (horaIni.getHours()>horaFin.getHours() && horaIni.getHours()>=sala.getHoraFin().getHours());
				horaFinIncorrecta = horaFin.getHours()>sala.getHoraFin().getHours() || (horaIni.getHours()>horaFin.getHours() && horaFin.getHours()<=sala.getHoraInicio().getHours());
				if (horaIniIncorrecta || horaFinIncorrecta) {
					result.rejectValue("estado", "Cerrado2.evento.salaEvento");
				}
				if (horaFin.getTime() - horaIni.getTime() > 10*60*60*1000) { // Se comprueba que la reunion no dure mas de 10h
					result.rejectValue("estado", "TimeMax2.evento.salaEvento");
				}
				if (fechaFin.compareTo(fechaIni) <= 0) {
					result.rejectValue("estado", "IniLessFin2.evento.salaEvento");
				}
				if (salaRepo.findBySalaOcupada(Long.parseLong(idSala), fechaIni, fechaFin).isPresent()) {
					result.rejectValue("estado", "Ocupado2.evento.salaEvento");
				} 
				horario.setHorario_Fecha_Inicio(fechaIni);
				horario.setHorario_Fecha_Fin(fechaFin);
				horario.setEvento(evento);
				horarios.add(horario);
				evento.addHorario(horario);
			}
			if (hApertura3 != null && !hApertura3.isBlank() && hCierre3 != null && !hCierre3.isBlank()) {
				horario = new Horarios();
				ms = dateTimeFormat.parse(hApertura3).getTime();
				fechaIni = new Timestamp(ms);
				horaIni = new Time(ms);
				ms = dateTimeFormat.parse(hCierre3).getTime();		
				fechaFin = new Timestamp(ms);
				horaFin = new Time(ms);
				
				horaIniIncorrecta = horaIni.getHours()<sala.getHoraInicio().getHours() || (horaIni.getHours()>horaFin.getHours() && horaIni.getHours()>=sala.getHoraFin().getHours());
				horaFinIncorrecta = horaFin.getHours()>sala.getHoraFin().getHours() || (horaIni.getHours()>horaFin.getHours() && horaFin.getHours()<=sala.getHoraInicio().getHours());
				if (horaIniIncorrecta || horaFinIncorrecta) {
					result.rejectValue("estado", "Cerrado3.evento.salaEvento");
				}
				if (horaFin.getTime() - horaIni.getTime() > 10*60*60*1000) { // Se comprueba que la reunion no dure mas de 10h
					result.rejectValue("estado", "TimeMax3.evento.salaEvento");
				}
				if (fechaFin.compareTo(fechaIni) <= 0) {
					result.rejectValue("estado", "IniLessFin3.evento.salaEvento");
				}
				if (salaRepo.findBySalaOcupada(Long.parseLong(idSala), fechaIni, fechaFin).isPresent()) {
					result.rejectValue("estado", "Ocupado3.evento.salaEvento");
				} 
				horario.setHorario_Fecha_Inicio(fechaIni);
				horario.setHorario_Fecha_Fin(fechaFin);
				horario.setEvento(evento);
				horarios.add(horario);
				evento.addHorario(horario);
			}
			
			if (result.hasErrors()) {
				model.addAttribute(Atributos.EVENTO, evento);
				model.addAttribute(Atributos.USER, user);
				administracionService.cargarParamsBusqSalas(model);
				administracionService.cargarUsuarios(model, UsuarioConstantes.ESTADO, null);
		        return eventoService.irANuevoEvento(model);
		    }

			evento.setSalaEvento(sala);
			evento.setUsuarioCreador(user);

			eventoRepo.save(evento);
			usuarioEventoRepo.saveAll(usuariosEvento);
			usuarioRepo.saveAll(usuarios);
			HorarioRepo.saveAll(horarios);

		} else {
			eventoService.mensajeNoAccesoEventos(model);
		}
		return Vistas.HOME;
	}
	
	@GetMapping("/invitado/{token}")
	public String getEventoInvitado(Model model, HttpServletRequest req, @PathVariable(value="token") String token) {
		Claims claims = jwtInvitado.decodeJWT(token);
		String idEvento = claims.getId();
		
		if (idEvento!= null && !idEvento.isEmpty()) {
			Optional<Evento> opEvento = eventoRepo.findById(Integer.parseInt(idEvento));
			if (opEvento.isPresent()) {
				Evento evento = opEvento.get();
				Horarios horario = evento.getHorarioElegido();
				if (horario != null) {
					long timeIni = horario.getHorario_Fecha_Inicio().getTime();
					long timeFin = horario.getHorario_Fecha_Fin().getTime();
					long ahora = new Date().getTime();
					
					// Se disminuye 5 min a la hora de inicio para poder entrar antes
					timeIni -= (5*60*1000);
					
					if (ahora < timeFin && ahora >= timeIni) { 
					    req.setAttribute("token", token);
					    
					    // envia a chat correspondiente
						return "forward:/chat_invitado";
					} else if ( ahora < timeIni) {
						double segundos = (timeIni - ahora) / 1000;
						double minutos = segundos / 60;
						double horas = minutos / 60;
						String falta;
						if (segundos <= 200) {
							falta = (int)segundos + " segundos";
						} else if (minutos <= 200) {
							falta = (int)minutos + " minutos";
						} else {
							falta = (int)horas + " horas"; 
						}
						model.addAttribute(Atributos.ALERTA_TITULO, "Acceso denegado");
						model.addAttribute(Atributos.ALERTA, "Aun es pronto, faltan "+falta);
					} else {
						model.addAttribute(Atributos.ALERTA_TITULO, "Acceso denegado");
						model.addAttribute(Atributos.ALERTA, "El evento ya terminó");
					}
				}
				model.addAttribute(Atributos.ALERTA_TITULO, "Acceso denegado");
				model.addAttribute(Atributos.ALERTA, "Aun no se ha definido la fecha del evento");
			} else {
				model.addAttribute(Atributos.ALERTA_TITULO, "Token incorrecto");
				model.addAttribute(Atributos.ALERTA, "Compruebe que la URL de invitado es correcta");
			}
		} else {
			model.addAttribute(Atributos.ALERTA_TITULO, "Token incorrecto");
			model.addAttribute(Atributos.ALERTA, "Compruebe que la URL de invitado es correcta");
		}
		// Si el token no contiene id o no encuentra el evento devuelve una pagina de error
		return Vistas.TOKEN_ERROR;
	}
	
	@GetMapping("/token")
	public ModelAndView getToken(HttpServletRequest req, Model model, String nombre, String idEvento) {
		Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int id_evento = Integer.parseInt(idEvento);
		// Como el token solo sirve durante el evento se pone un periodo de duración alto
		long tCaducidad = 30*24*60*60*1000; //30 dias
		String token = jwtInvitado.createJWT(id_evento, user.getUsername(), nombre, tCaducidad);
		String root = req.getRequestURL().toString().replaceAll("/token", "/")+"invitado/";
		model.addAttribute("token", root+token);
		return new ModelAndView("fragmentos :: token");
	}

}
