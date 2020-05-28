package es.uned.foederis.eventos.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import es.uned.foederis.archivos.model.Archivo;
import es.uned.foederis.chats.model.Chat;
import es.uned.foederis.eventos.EventoConstantes;
import es.uned.foederis.salas.model.Sala;
import es.uned.foederis.sesion.model.Usuario;

@Entity
public class Evento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int idEvento;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="id_usuario_creador_fk")
	private Usuario	UsuarioCreador;
	
	@NotNull
	@OneToOne
	@JoinColumn(name="id_sala_evento_fk")
	private Sala salaEvento;
	
	@OneToOne
	@JoinColumn(name="id_horario_fk")
	private Horarios horarioElegido;
	
	@NotBlank
	private String nombre;

	@NotNull
	private String estado;

	@OneToMany(mappedBy="evento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	public List<Horarios> lstHorarios = new ArrayList<>();
		
	@OneToMany(mappedBy="evento", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Usuario_Evento> usuariosEvento = new ArrayList<>();
	
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Chat> mensajesChat = new ArrayList<>();
	
	@OneToMany(mappedBy="usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Archivo> archivos = new ArrayList<>();
	
	public Evento() {
		this.estado = EventoConstantes.ESTADO_INACTIVO;
	}
	
	public Horarios getHorarioElegido() {
		return horarioElegido;
	}

	public void setHorarioElegido(Horarios horarioElegido) {
		this.horarioElegido = horarioElegido;
	}

	public void addEvento(Usuario_Evento comment) {
		  usuariosEvento.add(comment);
	        comment.setEvento(this);
    }
 
    public void removeEvento(Usuario_Evento comment) {
    	usuariosEvento.remove(comment);
        comment.setEvento(this);
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

	public String getEstado() {
		return estado;
	}
	
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Sala getSalaEvento() {
		return salaEvento;
	}

	public void setSalaEvento(Sala salaEvento) {
		this.salaEvento = salaEvento;
	}

	public Usuario getUsuarioCreador() {
		return UsuarioCreador;
	}

	public void setUsuarioCreador(Usuario usuarioCreador) {
		UsuarioCreador = usuarioCreador;
	}

	public List<Usuario_Evento> getEventosDelUsuario() {
		return usuariosEvento;
	}
	
	/**
	 * Devuelve un entero con los usuarios del evento da igual el estado
	 * @return
	 */
	public int getEventosDelUsuarioTotal() {
		return usuariosEvento.size();
	}
	
	
	/**
	 * Devuelve un entero con el total de usuarios confirmados al evento.
	 * @return total usuarios confirmados al evento
	 */
	public int getTotalConfirmadosEvento() {
		
		int totalConfirmados=0;
		for(Usuario_Evento aux: usuariosEvento) {
			if(aux.getConfirmado()==1) {
				totalConfirmados += 1;
			}
		}
		
		return totalConfirmados;
	}
	
	/***
	 * Obtenemos el total de asistentes al evento
	 * @return total Asistentes
	 */
	public int getTotalAsistentesEvento() {
		
		int totalAsistentes = (int) usuariosEvento.stream().filter(obj -> obj.isAsistente() == true)
				.count();
		
		return totalAsistentes;
	}
	
	/***
	 * Obtenemos el total de asistentes al evento
	 * @return total Asistentes
	 */
	public int getTotalListaEsperaEvento() {
		
		int totalListaEspera = (int) usuariosEvento.stream()
				.filter(obj -> obj.isAsistente() == false && obj.getConfirmado() == 1).count();
		
		return totalListaEspera;
	}
	

	public Usuario_Evento getUsuariosEvento(long idUsuario) {
		
		for(Usuario_Evento aux: usuariosEvento) {
			if(aux.getUsuario().getIdUsuario() == idUsuario)
				return aux;
		}
		return null;
	}

	public void setUsuariosEvento(List<Usuario_Evento> eventosDelUsuario) {
		this.usuariosEvento = eventosDelUsuario;
	}

	public List<Horarios> getHorarios() {
		return lstHorarios;
	}

	public void setHorarios(List<Horarios> lstHorarios) {
		this.lstHorarios = lstHorarios;
	}
	
	public void addUsuarioEvento(Usuario_Evento usuarioEvento) {
		this.usuariosEvento.add(usuarioEvento);
	}
	
	public void delUsuarioEvento(Usuario_Evento usuarioEvento) {
		this.usuariosEvento.remove(usuarioEvento);
	}
	
	public void addMensaje(Chat chat) {
		this.mensajesChat.add(chat);
	}
	
	public void delMensaje(Chat chat) {
		this.mensajesChat.remove(chat);
	}
	
	public void addArchivo(Archivo archivo) {
		this.archivos.add(archivo);
	}
	
	public void delArchivo(Archivo archivo) {
		this.archivos.remove(archivo);
	}
	
	public void addHorario(Horarios horario) {
		this.lstHorarios.add(horario);
	}
	
	public void delHorario(Horarios horario) {
		this.lstHorarios.remove(horario);
	}
	
}
