package es.uned.foederis.eventos.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Usuario_Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int idUsuarioEvento;

	@ManyToOne
    @JoinColumn(name="id", nullable=false)
	private Usuario idUsuario;
	
	@ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name="evento")
	private Evento evento;
	
	private boolean confirmado;
	private boolean asistente;
	private boolean presencial;
	
	@OneToOne
	private Horarios horario;

	public int getIdUsuarioEvento() {
		return idUsuarioEvento;
	}

	public void setIdUsuarioEvento(int idUsuarioEvento) {
		this.idUsuarioEvento = idUsuarioEvento;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento idEvento) {
		this.evento = idEvento;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
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
