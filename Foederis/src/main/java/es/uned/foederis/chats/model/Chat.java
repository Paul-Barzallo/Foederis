package es.uned.foederis.chats.model;

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
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idChat;
	
	private String 		texto;
	private Timestamp 	timestamp;
	
	
    @ManyToOne
    @JoinColumn(name="id_evento", nullable=false)
	private Evento idEvento;
	
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
	
	public Evento getIdEvento() {
		return idEvento;
	}
	public void setIdEvento(Evento idEvento) {
		this.idEvento = idEvento;
	}
	
	@Override
	public String toString() {
		return "Chat [idChat=" + idChat + ", texto=" + texto + ", timestamp=" + timestamp + 
				", idEvento=" + idEvento.toString() + "]";
	}
	
	
}
