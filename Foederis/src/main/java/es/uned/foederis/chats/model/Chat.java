package es.uned.foederis.chats.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idChat;
	
	private String texto;
	private Date timestamp;
	
	
	//foreign key
	private long idUsuarioPropietario;
	private int idRepositorioCompartido;
	
	// getters, setters
	public int getIdChat() {
		return idChat;
	}
	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

		
	public long getIdUsuarioPropietario() {
		return idUsuarioPropietario;
	}
	public void setIdUsuarioPropietario(long idUsuarioPropietario) {
		this.idUsuarioPropietario = idUsuarioPropietario;
	}
	
	public int getIdRepositorioCompartido() {
		return idRepositorioCompartido;
	}
	public void setIdRepositorioCompartido(int idRepositorioCompartido) {
		this.idRepositorioCompartido = idRepositorioCompartido;
	}
	
	@Override
	public String toString() {
		return "Chat [idChat=" + idChat + ", texto=" + texto + ", timestamp=" + timestamp + ", idUsuarioPropietario="
				+ idUsuarioPropietario + ", idRepositorioConpartido="
				+ idRepositorioCompartido + "]";
	}
	
	
}
