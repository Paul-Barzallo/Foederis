package es.uned.foederis.eventos.model;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import es.uned.foederis.sesion.model.Rol;
import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEvento;
	private String nombre;
	private Timestamp fechaInicio;
	private Timestamp fechaFin;
		
	@OneToOne
	@JoinColumn(name="usuario_creador_id")
	private Usuario	UsuarioCreador;
	
	private int estado;
	private int idChat;
	private int idRepositorioCompartido;
	private int idSala;	
	
	
	@OneToMany(mappedBy="idEvento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Horarios> horarios;
	
		
	@OneToMany(mappedBy="evento", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Usuario_Evento> eventosDelUsuario = new ArrayList<Usuario_Evento>();
	
	@OneToOne
	private Horarios horarioElegido;
	
	  public Horarios getHorarioElegido() {
		return horarioElegido;
	}

	public void setHorarioElegido(Horarios horarioElegido) {
		this.horarioElegido = horarioElegido;
	}

	public void addEvento(Usuario_Evento comment) {
		  eventosDelUsuario.add(comment);
	        comment.setEvento(this);
	    }
	 
	    public void removeEvento(Usuario_Evento comment) {
	    	eventosDelUsuario.remove(comment);
	        comment.setEvento(this);
	    }
	

	
	
	public Evento(int idEvento) {
		super();
		this.idEvento = idEvento;
	}
	
	public Evento() {
		
	}
		
	public int getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento; 
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Timestamp getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Timestamp getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}

	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		estado = estado;
	}
	public int getIdChat() {
		return idChat;
	}
	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}
	public int getIdRepositorioCompartido() {
		return idRepositorioCompartido;
	}
	public void setIdRepositorioCompartido(int idRepositorioCompartido) {
		this.idRepositorioCompartido = idRepositorioCompartido;
	}
	public int getIdSala() {
		return idSala;
	}
	public void setIdSala(int idSala) {
		this.idSala = idSala;
	}

	public Usuario getUsuarioCreador() {
		return UsuarioCreador;
	}

	public void setUsuarioCreador(Usuario usuarioCreador) {
		UsuarioCreador = usuarioCreador;
	}

	public List<Usuario_Evento> getEventosDelUsuario() {
		return eventosDelUsuario;
	}
	
	public Usuario_Evento getEventoDeUnUsuario(long idUsuario) {
		
		for(Usuario_Evento aux: eventosDelUsuario) {
			if(aux.getIdUsuario().getIdUsuario() == idUsuario)
				return aux;
		}
		return null;
	}

	public void setEventosDelUsuario(List<Usuario_Evento> eventosDelUsuario) {
		this.eventosDelUsuario = eventosDelUsuario;
	}

	public List<Horarios> getHorarios() {
		return horarios;
	}

	public void setHorarios(List<Horarios> horarios) {
		this.horarios = horarios;
	}

	
	

}
