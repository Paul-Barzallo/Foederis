package es.uned.foederis.archivos.model;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import es.uned.foederis.eventos.model.Evento;
import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Archivo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idArchivo;
	
	private String		nombreArchivo;
	private Timestamp 	timestamp;

	//foreign key
    
    public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@ManyToOne
    @JoinColumn(name="id_evento", nullable=false)
	private Evento idEvento;
	
    @ManyToOne
    @JoinColumn(name="id_usuario", nullable=false)
    private Usuario idUsuario;

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

	public Evento getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(Evento idEvento) {
		this.idEvento = idEvento;
	}

	public Usuario getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Override
	public String toString() {
		return "Archivo [idArchivo=" + idArchivo + ", nombreArchivo=" + nombreArchivo + ", idEvento=" + idEvento.toString() + ", Timestamp=" + timestamp.toString() + "]";
	}
    
}
