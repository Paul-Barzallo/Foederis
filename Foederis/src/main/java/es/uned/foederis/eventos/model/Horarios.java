package es.uned.foederis.eventos.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Horarios {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idHorario;
	
	@Column(name = "fecha_inicio")
	private Timestamp Horario_Fecha_Inicio;
	
	@Column(name = "fecha_fin")
	private Timestamp Horario_Fecha_Fin;
	
	@ManyToOne
    @JoinColumn(name="id_evento_fk")
	private Evento evento;
	
	public int getIdHorario() {
		return idHorario;
	}
	
	public void setIdHorario(int idHorario) {
		this.idHorario = idHorario;
	}
	
	public Evento getEvento() {
		return evento;
	}
	
	public void setEvento(Evento evento) {
		this.evento = evento;
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
	
	@Override
	public String toString() {
		return "Horarios [idHorario=" + idHorario + ", Horario_Fecha_Inicio=" + Horario_Fecha_Inicio
				+ ", Horario_Fecha_Fin=" + Horario_Fecha_Fin + ", evento=" + evento + "]";
	}

}
