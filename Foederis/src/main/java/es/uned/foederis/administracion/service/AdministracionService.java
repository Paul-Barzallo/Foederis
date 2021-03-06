package es.uned.foederis.administracion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import es.uned.foederis.constantes.Acciones;
import es.uned.foederis.constantes.Atributos;
import es.uned.foederis.constantes.Constantes;
import es.uned.foederis.constantes.Pantallas;
import es.uned.foederis.constantes.Rutas;
import es.uned.foederis.constantes.Vistas;
import es.uned.foederis.sesion.constante.UsuarioConstantes;
import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

/**
 * Servicio para los controles del modulo de administracion
 * Proporciona acceso a los repositorios y aciones repetitivas
 * @author barza
 *
 */
@Service
public class AdministracionService {
	@Autowired
	private IRolRepository rolRepo;
	 
	@Autowired
	private IUsuarioRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/**
	 * Agrega los roles para la pantalla de formulario de usuario
	 * y los agrega al model
	 * @param model
	 */
	public void cargarRoles(Model model) {
		List<Rol> roles = new ArrayList<>();
		for (Rol rol : rolRepo.findAll()) {
			roles.add(rol);
		}
		model.addAttribute(Atributos.ROLES, roles);
	}
	
	/**
	 * Se busca un usuario a partir del ID y se agrega al model
	 * @param model
	 * @param idUsuario
	 * @return true si se encuentra un usuario
	 */
	public boolean cargarUsuario(Model model, Long idUsuario) {
		// Se busca el usuario y se comprueba que no sea null
		Optional<Usuario> opUser = userRepo.findById(idUsuario);
		if (opUser.isPresent()) {
			Usuario user = opUser.get();
			model.addAttribute(Atributos.USUARIO, user);
			return true;
		}
		return false;
	}
	
	/**
	 * Añade los usuarios que coincidan con la busqueda al model
	 * @param model
	 * @param paramBusq tipos de busqueda: NOMBRE, USERNAME, ROL, ...
	 * @param valorBusq 
	 */
	public void cargarUsuarios(Model model, String paramBusq, String valorBusq) {
		List<Usuario> usuarios = null;
		// ponemos el valor de la busqueda en minusculas porque así se guarda en base de datos
		valorBusq = valorBusq.toLowerCase();
		
		switch (paramBusq) {
		case UsuarioConstantes.NOMBRE:
			usuarios = userRepo.findByNombreContainingOrApellidosContaining(valorBusq, valorBusq);
			break;
		case UsuarioConstantes.ROL:
			usuarios = userRepo.findByRolContaining(valorBusq);
			break;
		case UsuarioConstantes.USERNAME:
			usuarios = userRepo.findByUsernameContaining(valorBusq);
			break;
		default:
			usuarios = new ArrayList<>();
			for(Usuario usuario : userRepo.findAll()) {
				usuarios.add(usuario);
			}
			break;
		}
		if (usuarios!=null) {
			model.addAttribute(Atributos.USUARIOS, usuarios);
		}
	}
	
	/**
	 * Valida que no exista otro usuario con el mimso username
	 * @param usuario
	 * @return true si existe algun usuario con ese username
	 */
	public boolean isUsernameRepetido(Usuario usuario) {
		if (usuario.getUsername() != null && !usuario.getUsername().isBlank()) {
			Optional<Usuario> repetido = userRepo.findByUsername(usuario.getUsername());
			return repetido.isPresent() && usuario.getIdUsuario() != repetido.get().getIdUsuario();
		}
		return false;
	}
	
	/**
	 * Activa o desactiva el ususario en funcion del valor de activar
	 * @param model
	 * @param idUsuario
	 * @param activar
	 * @return
	 */
	public boolean activarUsuario(Model model, Long idUsuario, boolean activar) {
		Usuario user = (Usuario)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (user.isAdmin()) {
			Optional<Usuario> opUser = userRepo.findById(idUsuario);
			if (opUser.isPresent()) {
				Usuario usuario = opUser.get();
				usuario.setActivo(activar);
				userRepo.save(usuario);
				return true;
			} else {
				mensajeUsuarioNoEncontrado(model);
			}
		} else {
			mensajeNoAccesoUsuarios(model);
		}
		return false;
	}
	
	/**
	 * Guarda el usuario en la db y añade mensaje de confirmación al modelo
	 * si hay algun error durante el guardado devuelve false
	 * @param model
	 * @param usuario
	 * @return true si se guarda correctamente
	 */
	public boolean guardarUsuario(Model model, Usuario usuario) {
		try {
			usuario.setApellidos(usuario.getApellidos().toLowerCase());
			usuario.setNombre(usuario.getNombre().toLowerCase());
			usuario.setUsername(usuario.getUsername().toLowerCase());
			Long idUsuario = usuario.getIdUsuario();
			// Si es un nuevo usuario hay que cifrar la contraseña
			if (idUsuario==null) {
				usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			}
			userRepo.save(usuario);
			mensajeUsuarioGuardado(model, idUsuario);
			return true;
		} catch(Exception e) {
			mensajeErrorGuardarSala(model);
			return false;
		}
	}
	
	/**
	 * Carga los parametros por los que se va a buscar los usuarios
	 * Esta relacionada con la función cargarUsuarios
	 * En esta función se agregan los diferentes filtros y en la otra
	 * se filtra la busqueda por el parametro seleccionado
	 * @param model
	 */
	public void cargarParamsBusqUsuarios(Model model) {
		List<String> paramsBusqueda = new ArrayList<>();
		paramsBusqueda.add(UsuarioConstantes.USERNAME);
		paramsBusqueda.add(UsuarioConstantes.NOMBRE);
		paramsBusqueda.add(UsuarioConstantes.ROL);
		model.addAttribute(Atributos.PARAMS_BUSQUEDA, paramsBusqueda);
	}
	
	/**
	 * Carga las acciones que se pueden realizar 
	 * @param model
	 */
	public void cargarAccionesUsuarios(Model model) {
		List<String> acciones = new ArrayList<>();
		acciones.add(Acciones.MODIFICAR);
		acciones.add(Acciones.ACTIVAR);
		model.addAttribute(Atributos.ACCIONES, acciones);
	}
	
	public void mensajeNoAccesoUsuarios(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Aceso Denegado");
		model.addAttribute(Atributos.ALERTA, "No tiene permisos de acceso a la administración de usuarios");
	}
	
	public void mensajeUsuarioNoEncontrado(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Error");
		model.addAttribute(Atributos.ALERTA, "No se ha encontrado el usuario");
	}
	
	public void mensajeErrorGuardarUsuario(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Error");
		model.addAttribute(Atributos.ALERTA, "No se ha podido guardar el usuario");
	}
	
	public void mensajeErrorGuardarSala(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Error");
		model.addAttribute(Atributos.ALERTA, "No se ha podido guardar la sala");
	}
	
	public void mensajeNoAccesoSalas(Model model) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Aceso Denegado");
		model.addAttribute(Atributos.ALERTA, "No tiene permisos de acceso a la administración de salas");
	}
	
	public void mensajeUsuarioGuardado(Model model, Long idUsuario) {
		model.addAttribute(Atributos.ALERTA_TITULO, "Información");
		String mensaje;
		if (idUsuario==null) {
			mensaje = "El usuario se ha creado correctamente";
		} else {
			mensaje = "El usuario se ha modificado correctamente";
		}
		model.addAttribute(Atributos.ALERTA, mensaje);
	}
	
	public String irAPerfil(Model model) {
		model.addAttribute(Atributos.PANTALLA, Pantallas.PERFIL);
		return Vistas.PERFIL;
	}
	
	public String irAUsuario(Model model) {
		model.addAttribute(Atributos.PANTALLA, Pantallas.USUARIOS);
		return Vistas.PERFIL;
	}
	
	public String irAUsuarios(Model model) {
		model.addAttribute(Atributos.PANTALLA, Pantallas.USUARIOS);
		return Vistas.USUARIOS;
	}
}
