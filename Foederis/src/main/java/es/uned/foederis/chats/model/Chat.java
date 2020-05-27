package es.uned.foederis.chats.model;

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
public class Chat {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idChat;
	
	@NotEmpty
	private String texto;
	
	@Column(name="fecha")
	private Timestamp timestamp;
	
    @ManyToOne
    @JoinColumn(name="id_evento_fk", nullable=false)
	private Evento evento;
	
    @ManyToOne
    @JoinColumn(name="id_usuario", nullable=false)
    private Usuario usuario;
    
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
	
	@Override
	public String toString() {
		return "Chat [idChat=" + idChat + ", texto=" + texto + ", timestamp=" + timestamp + 
				", idEvento=" + evento.toString() + ",idUsuario=" + usuario.toString() + "]";
	}
	
	
}
