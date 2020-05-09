package es.uned.foederis.salas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Sala {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String nombre;
	private int aforo;
	private boolean presentacion;
	private boolean megafonia;
	private boolean grabacion;
	private boolean streaming;
	private boolean wifi;
	
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
