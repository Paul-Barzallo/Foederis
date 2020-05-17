package es.uned.foederis.sesion.model;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
	@ManyToOne
	@JoinColumn(name="id_rol_fk")
	private Rol rol;
	@NotNull
	private boolean activo;
	
	/**
	 * Por defecto el usuario está desactivado
	 * Se usa cuando los datos de usuario en el login están mal
	 * Es necesario devolver algun valor en usermane y password
	 */
	public Usuario() {
		this.activo = true;
	}
	
	public Usuario(String nombre, String apellidos, String username, String password, Rol rol) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.activo = true;
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
}
