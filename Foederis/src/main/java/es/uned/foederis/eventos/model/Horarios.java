package es.uned.foederis.eventos.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Horarios {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "idHorario")
	private int idHorario;
	private Timestamp Horario_Fecha_Inicio;
	private Timestamp Horario_Fecha_Fin;
	
	@ManyToOne
    @JoinColumn(name="idEvento", nullable=false)
	private Evento idEvento;
	
	//Get y Set
	
	public int getIdHorario() {
		return idHorario;
	}
	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}
	public Evento getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Evento idEvento) {
		this.idEvento = idEvento;
	}
	public Timestamp getHorario_Fecha_Inicio() {
		return Horario_Fecha_Inicio;
	}
	public void setHorario_Fecha_Inicio(Timestamp horario_Fecha_Inicio) {
		Horario_Fecha_Inicio = horario_Fecha_Inicio;
	}
	public Timestamp getHorario_Fecha_Fin() {
		return Horario_Fecha_Fin;
	}
	public void setHorario_Fecha_Fin(Timestamp horario_Fecha_Fin) {
		Horario_Fecha_Fin = horario_Fecha_Fin;
	}


}
