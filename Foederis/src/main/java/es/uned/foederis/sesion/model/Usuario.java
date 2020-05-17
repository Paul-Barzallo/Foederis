package es.uned.foederis.sesion.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.eventos.model.Usuario_Evento;

@Entity
public class Usuario implements UserDetails{
	private static final long serialVersionUID = -6174649516690350773L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private long id;
	private String nombre;
	private String apellidos;
	@Column(unique = true)
	private String username;
	private String password;
	@OneToOne
	private Rol rol;
	private boolean activo;
	
	@OneToMany(mappedBy="idUsuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Usuario_Evento> eventosDelUsuario= new ArrayList<Usuario_Evento>();
	
	public Usuario() {
		this.activo = true;
	}
	
	public Usuario(String username, String password) {
		this.username = username;
		this.password = password;
		this.activo = false;
		//this.eventosDelUsuario = new HashSet<Usuario_Evento> ();
	}
	
	public Usuario(String nombre, String apellidos, String username, String password, Rol rol, List<Usuario_Evento> eventosDelUsuario) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.eventosDelUsuario=eventosDelUsuario;
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
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	@Override
	public String getUsername() {
		return username;
	}
	public void setUsername(String user) {
		this.username = user;
	}
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
		return "Usuario [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", user=" 
				+ username+ ", rol=" + rol + "]";
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

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


	
}
