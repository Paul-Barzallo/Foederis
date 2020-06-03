package es.uned.foederis.websocket.model;

import es.uned.foederis.sesion.model.Rol;

public class ChatMessage {
    private MessageType type;
    private String 		content;
    private String 		sender;
    private String 		timestamp;
    private int 		idChat;
    private long		rol;

    public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        REMOVE,
        END
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

	public int getIdChat() {
		return idChat;
	}

	public void setIdChat(int idChat) {
		this.idChat = idChat;
	}

	public long getRol() {
		return rol;
	}

	public void setRol(long rol) {
		this.rol = rol;
	}
    
    
}
