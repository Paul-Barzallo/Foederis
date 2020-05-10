package es.uned.foederis.eventos.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEvento;
	private String nombre;
	private Date fechaInicio;
	private Date fechaFin;
	
	//foreign key
	private int	idUsuarioCreador;
	private int estado;
	private int idChat;
	private int idRepositorioCompartido;
	private int idSala;
	
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
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public int getIdUsuarioCreador() {
		return idUsuarioCreador;
	}
	public void setIdUsuarioCreador(int idUsuarioCreador) {
		this.idUsuarioCreador = idUsuarioCreador;
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

	
	

}
