package es.uned.foederis.eventos.controller;


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
	 * Carga la variable user, solo llamamos a este metodo cuando no ha sido cargado
	 * 
	 */
	public void iniciarUsuario() {
		user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	/**
	 * Devuelve el listado de eventos sin confirmar. Actualiza el evento, actualiza
	 * la variable confirmado a 0.
	 * 
	 * @param model, Id que recupera el evento
	 * @return html
	 */
	@GetMapping(Rutas.EVENTOS_BAJA)
	public ModelAndView baja(Model model, @RequestParam(value = "id") Evento eventoSeleccionado) {

		ModelAndView mav = new ModelAndView();

		ActualizarConfirmacionEvento(usuarioEventoConstantes.CONFIRMADO_NO, eventoSeleccionado, null, false);

		eventoService.mensajeConfirmacion(model);

		mav.setViewName("listarEventos");

		return mav;
	}

	/**
	 * Devuelve el listado de eventos sin confirmar. Actualiza el evento, actualiza
	 * la variable confirmado a 1 y y pasandole el horario elegido por el usuario.
	 * 
	 * @param model, checkBox presencial, radioButton con la seleccion del horario y
	 *               el evento seleccionado
	 * @return html
	 */
	@PostMapping(Rutas.EVENTOS_CONFIRMAR_INVITADO)
	public ModelAndView confirmar(Model model,
			@RequestParam(value = "checkPresencial", required = false) boolean chkPresencial,
			@RequestParam(value = "seleccionHorario", required = false) String horarioElegido, Evento eventoSeleccionado) {

		ModelAndView mav = new ModelAndView();
		
		Horarios elegido= new Horarios();

		if(horarioElegido != null) {
			// Obtener el objeto Horario que se ha seleccionado por el usuario.
			List<Horarios> h = (List<Horarios>) HorarioRepo.findAll();
	
			int idHorarioElegido = Integer.parseInt(horarioElegido);
			elegido = h.get(idHorarioElegido-1);
		
		}			

		for (Usuario_Evento aux : user.getEventosDelUsuario()) {
			if (aux.getEvento().getIdEvento() == eventoSeleccionado.getIdEvento()) {
				ActualizarConfirmacionEvento(usuarioEventoConstantes.CONFIRMADO_SI, aux.getEvento(), elegido, chkPresencial);
			}
		}

		eventoService.mensajeConfirmacion(model);

//		return "redirect:/Evento/listarFiltro?filtroListado=sin";
		mav.setViewName("listarEventos");

		return mav;
	}

	/**
	 * Funcion que se ejecuta cuando un invitado al evento confirma o rechaza su
	 * asistencia al evento. Se viene aqui desde confirmar y baja Actualiza el
	 * evento indicando si el usuario confirma asistencia Si esta confirmando
	 * asistencia almacenamos el horario elegido.
	 */
	private void ActualizarConfirmacionEvento(int pValor, Evento eventoSeleccionado, Horarios pHorarioElegido,
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
	 * Solo visible el boton para los jefes de proyecto creadores del evento.
	 * Confirmamos los asistentes al evento que elija el jefe de proyecto
	 * Confirmamos el horario mas legido por los confirmados al evento.
	 */
	@PostMapping(Rutas.EVENTOS_CONFIRMAR_CREADOR_EVENTO)
	public String confirmarAsistenciaEvento(Model model,
			@RequestParam(value = "checkAsistenteElegido", required = false) List<Integer> lstCheckedAsistentes,
			@RequestParam(value = "horarioVotado") String horarioVotado,
			@RequestParam(value = "checkPresencial") String chkPresencial,
			@RequestParam(value = "eventoSeleccionado") Integer idEvento,
			@RequestParam(value = "idSala") String idSala) {
		
		Evento evento = user.getEventosDelUsuario().stream().filter(c -> c.getEvento().getIdEvento() == idEvento)
				.findFirst().get().getEvento();
		
		//Si ha existido cambio de sala lo guardamos
		if(!idSala.isEmpty()) {
			
			Optional<Sala> salaCambiada = salaRepo.findById(Long.parseLong(idSala));
			evento.setSalaEvento(salaCambiada.get());
			
			usuRepo.save(user);
			
			eventoService.mensajeConfirmacion(model);
			
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
			
			eventoService.mensajeConfirmacion(model);
		}		
		
		return "redirect:/Evento/listarFiltro?filtroListado=sin";

	}

	// **Listar evento.HTML ***//

	/****
	 * Nos permite acceder desde el listado de eventos al evento seleccionado, donde
	 * poner ver lo relativo a el evento
	 * 
	 * @param evento
	 * @return verEvento
	 */
	@GetMapping(Rutas.EVENTOS_VER_DETALLE)
	public ModelAndView entrar(Model model, @RequestParam(value = "id") Evento evento) {
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
	 * @return timestamp
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
	 * Muestra el listado de eventos creados por el usuario creador de eventos logado
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

	/****
	 * Nos muestra el listado de eventos del usuario, podemos filtrar por:
	 * Todos los evento, los de hoy, los rechazados, los pendientes de confirmar y los futuros
	 * 
	 * @param filtroListado
	 * @return listarEventos
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
	public String postGuardarSala(Model model, HttpServletRequest request, @Validated Evento evento, BindingResult result) throws ParseException {
		Usuario user = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdminOrJP()) {
			String idSala = request.getParameter("idSala");
			String[] idUsuarios = request.getParameterValues("usuarios");
			String hApertura1 = request.getParameter("horarioApertura1");
			String hApertura2 = request.getParameter("horarioApertura2");
			String hApertura3 = request.getParameter("horarioApertura3");
			String hCierre1 = request.getParameter("horarioCierre1");
			String hCierre2 = request.getParameter("horarioCierre2");
			String hCierre3 = request.getParameter("horarioCierre3");

			List<Usuario_Evento> usuariosEvento = new ArrayList<>();
			List<Horarios> horarios = new ArrayList<>();
			List<Usuario> usuarios = new ArrayList<>();

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

			if (hApertura1 != null && !hApertura1.isBlank() && hCierre1 != null && !hCierre1.isBlank()) {
				Horarios horario = new Horarios();
				long ms = dateTimeFormat.parse(hApertura1).getTime();
				horario.setHorario_Fecha_Inicio(new Timestamp(ms));
				ms = dateTimeFormat.parse(hCierre1).getTime();
				horario.setHorario_Fecha_Fin(new Timestamp(ms));
				horario.setEvento(evento);
				horarios.add(horario);
				evento.addHorario(horario);
			}

			if (hApertura2 != null && !hApertura2.isBlank() && hCierre2 != null && !hCierre2.isBlank()) {
				Horarios horario = new Horarios();
				long ms = dateTimeFormat.parse(hApertura2).getTime();
				horario.setHorario_Fecha_Inicio(new Timestamp(ms));
				ms = dateTimeFormat.parse(hCierre2).getTime();
				horario.setHorario_Fecha_Fin(new Timestamp(ms));
				horario.setEvento(evento);
				horarios.add(horario);
				evento.addHorario(horario);
			}

			if (hApertura3 != null && !hApertura3.isBlank() && hCierre3 != null && !hCierre3.isBlank()) {
				Horarios horario = new Horarios();
				long ms = dateTimeFormat.parse(hApertura3).getTime();
				horario.setHorario_Fecha_Inicio(new Timestamp(ms));
				ms = dateTimeFormat.parse(hCierre3).getTime();
				horario.setHorario_Fecha_Fin(new Timestamp(ms));
				horario.setEvento(evento);
				horarios.add(horario);
				evento.addHorario(horario);
			}

			evento.setSalaEvento(salaRepo.findById(Long.parseLong(idSala)).get());
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
	public String getEventoInvitado(HttpServletRequest req, @PathVariable(value="token") String token) {
		Claims claims = jwtInvitado.decodeJWT(token);
		String idEvento = claims.getId();
		
		if (idEvento!= null && !idEvento.isEmpty()) {
			Optional<Evento> opEvento = eventoRepo.findById(Integer.parseInt(idEvento));
			if (opEvento.isPresent()) {
				Evento evento = opEvento.get();
				long timeIni = evento.getHorarioElegido().getHorario_Fecha_Inicio().getTime();
				long timeFin = evento.getHorarioElegido().getHorario_Fecha_Fin().getTime();
				long ahora = new Date().getTime();
				
				// Se disminuye 5 min a la hora de inicio para poder entrar antes
				timeIni -= (5*60*1000);
				
				if (ahora < timeFin && ahora > timeIni) { 
				    req.setAttribute("token", token);
				    
				    // envia a chat correspondiente
					return "forward:/chat_invitado";
				} else {
					// Si la fecha no es correcta se muestra error de fecha
					return Vistas.TOKEN_ERROR;
				}
			}
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
