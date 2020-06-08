package es.uned.foederis;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import es.uned.foederis.eventos.EventoConstantes;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Horarios;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.eventos.repository.IEventoRepository;
import es.uned.foederis.eventos.repository.IHorarioRepository;
import es.uned.foederis.eventos.repository.IUsuarioEventoRepository;
import es.uned.foederis.salas.repository.ISalaRepository;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

@SpringBootApplication
public class FoederisApplication implements ApplicationRunner{
	@Autowired
	private IUsuarioRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	@Qualifier("dateTimeFormat")
	private SimpleDateFormat dateTimeFormat;
	
	@Autowired
	private ISalaRepository salaRepo;
	
	@Autowired
	private IUsuarioEventoRepository usuarioEventoRepo;

	@Autowired
	private IEventoRepository eventoRepo;

	@Autowired
	private IHorarioRepository HorarioRepo;
		
	public static void main(String[] args) {
		SpringApplication.run(FoederisApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
		// Se encripta las password de los usuarios creados por defecto
		Iterable<Usuario> usuarios = userRepo.findAll();
		for (Usuario usuario : usuarios) {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		userRepo.saveAll(usuarios);
		
		//Se crea eventos de activos en el momento de iniciar el servidor para pruebas
		long[] idUsuarios1 = {3,4,5,6};
		long[] idUsuarios2 = {3,4,5,6, 7, 8, 9, 10};
		crearEvento("Reunion conveniente", 6, 1, idUsuarios1);
		crearEvento("Reunion muy conveniente", 6, 7, idUsuarios2);
	}
	
	private void crearEvento(String titulo, long idUsuarioCreador, long idSala, long[] idUsuarios) {
		Evento evento = new Evento();
		
		Calendar fechaInicio = Calendar.getInstance();
		fechaInicio.setTime(new Date());
		fechaInicio.set(Calendar.MINUTE, 0);
		fechaInicio.set(Calendar.MILLISECOND, 0);
		long timeInicio = fechaInicio.getTimeInMillis();
		// 2h de duración del evento
		long timeFin = fechaInicio.getTimeInMillis() + 2*60*60*1000; 

		List<Usuario_Evento> usuariosEvento = new ArrayList<>();
		List<Horarios> horarios = new ArrayList<>();
		List<Usuario> usuarios = new ArrayList<>();
		
		Horarios horario = new Horarios();
		horario.setHorario_Fecha_Inicio(new Timestamp(timeInicio));
		horario.setHorario_Fecha_Fin(new Timestamp(timeFin));
		horario.setEvento(evento);
		horarios.add(horario);
		evento.addHorario(horario);

		Usuario user = userRepo.findById(idUsuarioCreador).get();
		for (long idUsuario : idUsuarios) {
			Usuario usuario = userRepo.findById(idUsuario).get();
			usuarios.add(user);
			Usuario_Evento usuarioEvento = new Usuario_Evento();
			usuarioEvento.setUsuario(usuario);
			usuarioEvento.setEvento(evento);
			usuarioEvento.setAsistente(true);
			usuarioEvento.setConfirmado(1);
			usuarioEvento.setHorario(horario);
			usuariosEvento.add(usuarioEvento);
			usuario.addEvento(usuarioEvento);
			evento.addUsuarioEvento(usuarioEvento);
		}	
		
		evento.setNombre(titulo);
		evento.setHorarioElegido(horario);
		evento.setSalaEvento(salaRepo.findById(idSala).get());
		evento.setUsuarioCreador(user);
		evento.setEstado(EventoConstantes.ESTADO_INACTIVO);

		eventoRepo.save(evento);
		usuarioEventoRepo.saveAll(usuariosEvento);
		userRepo.saveAll(usuarios);
		HorarioRepo.saveAll(horarios);
	}
}
