package es.uned.foederis.chats.model;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idChat;
	
	private String 		texto;
	private Timestamp 	timestamp;
	
	
	//foreign key
	private long idUsuario;
	private int idEvento;
	
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
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

		
	public long getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(long idUsuario) {
		this.idUsuario = idUsuario;
	}
	
	
	public int getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(int idEvento) {
		this.idEvento = idEvento;
	}
	
	@Override
	public String toString() {
		return "Chat [idChat=" + idChat + ", texto=" + texto + ", timestamp=" + timestamp + 
				", idUsuario=" + idUsuario + ", idEvento=" + idEvento + "]";
	}
	
	
}
