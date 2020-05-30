package es.uned.foederis.sesion.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Horarios;
import es.uned.foederis.eventos.model.Usuario_Evento;
import es.uned.foederis.sesion.constantes.UsuarioConstantes;

/**
 * Implementa UserDetails para ser el objeto usuario de spring-security
 * @author barza
 *
 */
@Entity
public class Usuario implements UserDetails{
	private static final long serialVersionUID = -6174649516690350773L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idUsuario;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name="id_rol_fk")
	private Rol rol;
	
	@NotBlank
	private String nombre;
	
	private String apellidos;
	
	@NotBlank
	@Column(unique = true)
	private String username;
	
	@NotEmpty
	@Size(min = 3)
	private String password;
	
	@NotNull
	private boolean activo;
	
	@OneToMany(mappedBy="usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Usuario_Evento> eventosDelUsuario = new ArrayList<>();
	
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Chat> mensajesChat = new ArrayList<>();
	
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Archivo> archivos = new ArrayList<>();
	
	@OneToMany(mappedBy="UsuarioCreador", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Evento> eventosCreados = new ArrayList<>();
	
	/**
	 * Por defecto el usuario está desactivado
	 * Se usa cuando los datos de usuario en el login están mal
	 * Es necesario devolver algun valor en usermane y password
	 */
	public Usuario() {
		this.activo = true;
	}
	
	public Usuario(String nombre, String apellidos, String username, String password, Rol rol, boolean activo, List<Usuario_Evento> eventosDelUsuario) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.activo = activo;
		this.eventosDelUsuario=eventosDelUsuario;
	}	
	
	public Long getIdUsuario() {
		return idUsuario;
	}
	
	public void setIdUsuario(Long id) {
		this.idUsuario = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellidos() {
		return apellidos;
	}
	
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	
	/**
	 * Implementación de metodo se spring-security
	 */
	@Override
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String user) {
		this.username = user;
	}
	
	/**
	 * Implementación de metodo se spring-security
	 */
	@Override
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Rol getRol() {
		return rol;
	}
	
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	
	public boolean isActivo() {
		return activo;
	}
	
	public void setActivo(boolean estado) {
		this.activo = estado;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + idUsuario + ", nombre=" + nombre + ", apellidos=" + apellidos + ", user=" 
				+ username+ ", rol=" + rol + "]";
	}

	/**
	 * metodo de roles de spring-security, se ignora
	 * se usará sistema de roles simple mediante atributo rol
	 */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	/**
	 * Controla si la cuenta a expirado
	 * No se hará control de expiración de control
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * Controla si la cuenta se ha bloqueado
	 * No se hará control de bloqueo de sesión
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * Controla si la cuenta a expirado
	 * No se hará control de xpiración de contraseña
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * Impementación de metodo de spring-security
	 */
	@Override
	public boolean isEnabled() {
		return isActivo();
	}

	public List<Usuario_Evento> getEventosDelUsuario() {
		return eventosDelUsuario;
	}

	public void setEventosDelUsuario(List<Usuario_Evento> eventosDelUsuario) {
		this.eventosDelUsuario = eventosDelUsuario;
	}

	public boolean isAdmin() {
		return this.rol.getIdRol() == UsuarioConstantes.ROL_ADMIN;
	}
	
	public boolean isJefeProyecto() {
		return this.rol.getIdRol() == UsuarioConstantes.ROL_JEFE_PROY;
	}
	
	public boolean isAdminOrJP() {
		return this.rol.getIdRol() == UsuarioConstantes.ROL_JEFE_PROY
				|| this.rol.getIdRol() == UsuarioConstantes.ROL_ADMIN;
	}
	
	public void addEvento(Usuario_Evento usuarioEvento) {
		this.eventosDelUsuario.add(usuarioEvento);
	}
	
	public void delEvento(Usuario_Evento usuarioEvento) {
		this.eventosDelUsuario.remove(usuarioEvento);
	}
	
	public void addMensaje(Chat chat) {
		this.mensajesChat.add(chat);
	}
	
	public void delMensaje(Chat chat) {
		this.mensajesChat.remove(chat);
	}
	
	public void addArchivo(Archivo archivo) {
		this.archivos.add(archivo);
	}
	
	public void delArchivo(Archivo archivo) {
		this.archivos.remove(archivo);
	}
	
	public void addEventoCreado(Evento evento) {
		this.eventosCreados.add(evento);
	}
	
	public void delEventoCreado(Evento evento) {
		this.eventosCreados.remove(evento);
	}
}
