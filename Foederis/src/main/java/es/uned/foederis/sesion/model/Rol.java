package es.uned.foederis.sesion.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Rol {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long idRol;
	@Column(unique = true, nullable = true)
	private String nombre;
	
	public Rol() {}
	
	public Rol(String nombre) {
		this.nombre = nombre;
	}

	public long getIdRol() {
		return idRol;
	}

	public void setIdRol(long id) {
		this.idRol = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String descripcion) {
		this.nombre = descripcion;
	}

	@Override
	public String toString() {
		return "Rol [id=" + idRol + ", descripcion=" + nombre + "]";
	}
}
