package es.uned.foederis.salas.model;

import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Sala {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idSala;
	@NotBlank
	private String nombre;
	@Min(4)
	private int aforo;
	@NotNull
	private boolean presentacion;
	@NotNull
	private boolean megafonia;
	@NotNull
	private boolean grabacion;
	@NotNull
	private boolean streaming;
	@NotNull
	private boolean wifi;
	@NotNull
	private boolean activa;
	@NotNull
	private Time horaInicio;
	@NotNull
	private Time horaFin;
	
	public Sala() {}

	public Sala(String nombre, int aforo, boolean presentacion, boolean megafonia, boolean grabacion, boolean streaming,
			boolean wifi) {
		this.nombre = nombre;
		this.aforo = aforo;
		this.presentacion = presentacion;
		this.megafonia = megafonia;
		this.grabacion = grabacion;
		this.streaming = streaming;
		this.wifi = wifi;
	}

	public long getIdSala() {
		return idSala;
	}
	public void setIdSala(long id) {
		this.idSala = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getAforo() {
		return aforo;
	}

	public void setAforo(int aforo) {
		this.aforo = aforo;
	}
	
	public boolean isPresentacion() {
		return presentacion;
	}

	public void setPresentacion(boolean presentacion) {
		this.presentacion = presentacion;
	}

	public boolean isMegafonia() {
		return megafonia;
	}

	public void setMegafonia(boolean megafonia) {
		this.megafonia = megafonia;
	}

	public boolean isGrabacion() {
		return grabacion;
	}

	public void setGrabacion(boolean grabacion) {
		this.grabacion = grabacion;
	}

	public boolean isStreaming() {
		return streaming;
	}

	public void setStreaming(boolean streaming) {
		this.streaming = streaming;
	}

	public boolean isWifi() {
		return wifi;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	public Time getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Time getHoraFin() {
		return horaFin;
	}

	public void setHoraFin(Time horaFin) {
		this.horaFin = horaFin;
	}
	
	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	@Override
	public String toString() {
		return "Sala [nombre=" + nombre + ", aforo=" + aforo + "]";
	}
	
}
