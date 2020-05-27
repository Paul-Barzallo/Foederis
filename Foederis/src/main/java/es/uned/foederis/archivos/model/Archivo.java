package es.uned.foederis.archivos.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotEmpty;

import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Archivo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idArchivo;
	
	@NotEmpty
	private String nombreArchivo;
	
	@Column(name="fecha")
	private Timestamp timestamp;
	
	@ManyToOne
    @JoinColumn(name="id_evento_fk", nullable=false)
	private Evento evento;
	
    @ManyToOne
    @JoinColumn(name="id_usuario_fk", nullable=false)
    private Usuario usuario;

    public int getIdArchivo() {
		return idArchivo;
	}

	public void setIdArchivo(int idArchivo) {
		this.idArchivo = idArchivo;
	}

	public String getNombreArchivo() {
		return nombreArchivo;
	}

	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public Evento getEvento() {
		return evento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Archivo [idArchivo=" + idArchivo + ", nombreArchivo=" + nombreArchivo + ", idEvento=" + evento.toString() + ", Timestamp=" + timestamp.toString() + "]";
	}
    
}
