package es.uned.foederis.administracion.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import es.uned.foederis.Constantes;
import es.uned.foederis.sesion.constante.UsuarioConstantes;
import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;
import es.uned.foederis.sesion.repository.IRolRepository;
import es.uned.foederis.sesion.repository.IUsuarioRepository;

@Service
public class AdministracionService {
	@Autowired
	private IRolRepository rolRepo;
	 
	@Autowired
	private IUsuarioRepository userRepo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public void cargarRoles(Model model) {
		List<Rol> roles = new ArrayList<>();
		for (Rol rol : rolRepo.findAll()) {
			roles.add(rol);
		}
		model.addAttribute(Constantes.ROLES, roles);
	}
	
	public boolean cargarUsuario(Model model, Long idUsuario) {
		// Se busca el usuario y se comprueba que no sea null
		Optional<Usuario> opUser = userRepo.findById(idUsuario);
		if (opUser.isPresent()) {
			Usuario user = opUser.get();
			model.addAttribute(Constantes.USUARIO, user);
			return true;
		}
		return false;
	}
	
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
			model.addAttribute(Constantes.USUARIOS, usuarios);
		}
	}
	
	/**
	 * Solo valida cuando es un usuario nuevo
	 * En el resto de casos devuelve false
	 * Valida que no exista ya un usuario con el mimso username
	 * @param usuario
	 * @return
	 */
	public boolean isUsernameRepetido(Usuario usuario) {
		if (usuario.getIdUsuario() == null && usuario.getUsername() != null && !usuario.getUsername().isBlank()) {
			Optional<Usuario> repetido = userRepo.findByUsername(usuario.getUsername());
			return repetido.isPresent();
		}
		return false;
	}
	
	public boolean guardarUsuario(Model model, Usuario usuario) {
		if (usuario != null) {
			usuario.setApellidos(usuario.getApellidos().toLowerCase());
			usuario.setNombre(usuario.getNombre().toLowerCase());
			usuario.setUsername(usuario.getUsername().toLowerCase());
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
			userRepo.save(usuario);
			model.addAttribute(Constantes.ALERTA_TITULO, "Información");
			model.addAttribute(Constantes.ALERTA, "El usuario se a guardado correctamente");
			return true;
		}
		return false;
	}
	
	public boolean activarUsuario(Model model, Long idUsuario, boolean activar) {
		Optional<Usuario> opUser = userRepo.findById(idUsuario);
		if (opUser.isPresent()) {
			Usuario user = opUser.get();
			user.setActivo(activar);
			userRepo.save(user);
			return true;
		}
		return false;
	}
	
	public void cargarParamsBusqUsuarios(Model model) {
		List<String> paramsBusqueda = new ArrayList<>();
		paramsBusqueda.add(UsuarioConstantes.USERNAME);
		paramsBusqueda.add(UsuarioConstantes.NOMBRE);
		paramsBusqueda.add(UsuarioConstantes.ROL);
		model.addAttribute(Constantes.PARAMS_BUSQUEDA, paramsBusqueda);
	}
	
	public void cargarAccionesUsuarios(Model model) {
		List<String> acciones = new ArrayList<>();
		acciones.add(Constantes.Accion.MODIFICAR);
		acciones.add(Constantes.Accion.ACTIVAR);
		model.addAttribute(Constantes.ACCIONES, acciones);
	}
	
	public void mensajeNoAccesoUsuarios(Model model) {
		model.addAttribute(Constantes.ALERTA_TITULO, "Aceso Denegado");
		model.addAttribute(Constantes.ALERTA, "No tiene permisos de acceso a la administración de usuarios");
	}
	
	public void mensajeUsuarioNoEncontrado(Model model) {
		model.addAttribute(Constantes.ALERTA_TITULO, "Error");
		model.addAttribute(Constantes.ALERTA, "No se ha encontrado el usuario");
	}
	
	public void mensajeErrorGuardarUsuario(Model model) {
		model.addAttribute(Constantes.ALERTA_TITULO, "Error");
		model.addAttribute(Constantes.ALERTA, "No se ha podido guardar el usuario");
	}
	
	public void mensajeErrorGuardarSala(Model model) {
		model.addAttribute(Constantes.ALERTA_TITULO, "Error");
		model.addAttribute(Constantes.ALERTA, "No se ha podido guardar la sala");
	}
	
	public void mensajeNoAccesoSalas(Model model) {
		model.addAttribute(Constantes.ALERTA_TITULO, "Aceso Denegado");
		model.addAttribute(Constantes.ALERTA, "No tiene permisos de acceso a la administración de salas");
	}
	
	public void mensajeUsuarioGuardado(Model model, Long idUsuario) {
		model.addAttribute(Constantes.ALERTA_TITULO, "Información");
		String mensaje;
		if (idUsuario==null) {
			mensaje = "El usuario se ha creado correctamente";
		} else {
			mensaje = "El usuario se ha modificado correctamente";
		}
		model.addAttribute(Constantes.ALERTA, mensaje);
	}
	
	public String irAPerfil(Model model) {
		model.addAttribute(Constantes.PANTALLA, Constantes.Pantalla.USUARIOS);
		return Constantes.Vista.PERFIL;
	}
	
	public String irAUsuario(Model model) {
		model.addAttribute(Constantes.PANTALLA, Constantes.Pantalla.USUARIOS);
		return Constantes.Vista.PERFIL;
	}
	
	public String irAUsuarios(Model model) {
		model.addAttribute(Constantes.PANTALLA, Constantes.Pantalla.USUARIOS);
		return Constantes.Vista.USUARIOS;
	}
}
