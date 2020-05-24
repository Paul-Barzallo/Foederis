package es.uned.foederis.eventos.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEvento;
	@NotBlank
	private String nombre;

	@NotNull
	private boolean estado;
	@OneToOne
	@JoinColumn(name="id_chat")
	private Chat chat;
	private int idRepositorioCompartido;
	@NotNull
	@OneToOne
	@JoinColumn(name="usuario_creador_id")
	private Usuario	UsuarioCreador;	
	@NotNull
	@OneToOne
	@JoinColumn(name="sala_Evento_id")
	private Sala salaEvento;
	
	@OneToMany(mappedBy="idEvento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public List<Horarios> lstHorarios;
		
	@OneToMany(mappedBy="evento", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Usuario_Evento> eventosDelUsuario = new ArrayList<Usuario_Evento>();
	
	@OneToOne
	@JoinColumn(name="idHorario")
	private Horarios horarioElegido;
	
	
	//Constructores	
	public Evento(int idEvento) {
		this.idEvento = idEvento;
		this.estado = true;
	}
	
	public Evento() {
		this.estado = true;
	}
	
	//Get y Set

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
//	public Timestamp getFechaInicio() {
//		return fechaInicio;
//	}
//	public void setFechaInicio(Timestamp fechaInicio) {
//		this.fechaInicio = fechaInicio;
//	}
//	public Timestamp getFechaFin() {
//		return fechaFin;
//	}
//	public void setFechaFin(Timestamp fechaFin) {
//		this.fechaFin = fechaFin;
//	}

	public boolean getEstado() {
		return estado;
	}
	
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	
	public Chat getChat() {
		return chat;
	}
	
	public void setChat(Chat chat) {
		this.chat = chat;
	}
	public int getIdRepositorioCompartido() {
		return idRepositorioCompartido;
	}
	public void setIdRepositorioCompartido(int idRepositorioCompartido) {
		this.idRepositorioCompartido = idRepositorioCompartido;
	}
	public Sala getSalaEvento() {
		return salaEvento;
	}

	public void setSalaEvento(Sala salaEvento) {
		this.salaEvento = salaEvento;
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
	
	public int getEventosDelUsuarioTotal() {
		return eventosDelUsuario.size();
	}
	
	/**
	 * Devuelve un entero con el total de usuarios confirmados al evento.
	 * @return total usuarios confirmados al evento
	 */
	public int getEventosDelUsuarioConfirmados() {
		
		int totalConfirmados=0;
		for(Usuario_Evento aux: eventosDelUsuario) {
			if(aux.getConfirmado()==1) {
				totalConfirmados += 1;
			}
		}
		
		return totalConfirmados;
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
		return lstHorarios;
	}

	public void setHorarios(List<Horarios> lstHorarios) {
		this.lstHorarios = lstHorarios;
	}
}
