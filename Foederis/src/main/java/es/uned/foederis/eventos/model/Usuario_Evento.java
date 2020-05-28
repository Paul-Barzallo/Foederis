package es.uned.foederis.eventos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Usuario_Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int idUsuarioEvento;

	@NotEmpty
	@ManyToOne
    @JoinColumn(name="id_usuario_fk")
	private Usuario usuario;
	
	@ManyToOne
    @JoinColumn(name="id_evento_fk")
	private Evento evento;
	
	@OneToOne
	@JoinColumn(name="id_horario_fk")
	private Horarios horario;
	
	//Valores -1 aun sin confirmar, 0 no confirma, 1 si confirma
	private int confirmado;
	@NotNull
	private Boolean asistente;
	@NotNull
	private Boolean presencial; 
	
	public Usuario_Evento() {
		this.confirmado = -1;
		this.setPresencial(false);
	}
	
	public int getIdUsuarioEvento() {
		return idUsuarioEvento;
	}

	public void setIdUsuarioEvento(int idUsuarioEvento) {
		this.idUsuarioEvento = idUsuarioEvento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}	

	public int getConfirmado() {
		return confirmado;
	}

	public void setConfirmado(int confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isAsistente() {
		return asistente;
	}

	public void setAsistente(boolean asistente) {
		this.asistente = asistente;
	}

	public boolean isPresencial() {
		return presencial;
	}

	public void setPresencial(boolean presencial) {
		this.presencial = presencial;
	}

	public Horarios getHorario() {
		return horario;
	}

	public void setHorario(Horarios horario) {
		this.horario = horario;
	}
}
