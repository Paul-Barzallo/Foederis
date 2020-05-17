package es.uned.foederis.salas.model;

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
	private long id;
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
	private Timestamp desde;
	@NotNull
	private Timestamp hasta;
	
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

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Sala [nombre=" + nombre + ", aforo=" + aforo + "]";
	}
	
}
