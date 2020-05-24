package es.uned.foederis.eventos.model;

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
import javax.persistence.OneToOne;
import javax.validation.constraints.Null;

import com.sun.istack.Nullable;

import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Usuario_Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int idUsuarioEvento;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="id")
	private Usuario idUsuario;
	
	@ManyToOne(cascade = CascadeType.ALL)//(fetch = FetchType.LAZY)
    @JoinColumn(name="evento")
	private Evento evento;
	
	//VAlores -1 aun sin confirmar, 0 no confirma, 1 si confirma
	private int confirmado;
	private boolean asistente;
	private boolean presencial;
	
	@OneToOne
	@JoinColumn(name="horario")
	private Horarios horario;

	//Get y Set
	
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
