package es.ubu.lsi.model.conciertos;

import javax.persistence.*;

/**
 * Clase embedable Direccion.
 * Contiene los campos relativos a la dirección de un cliente.
 * 
 * @author Eduardo Manuel Cabeza Lopez
 *
 */
@Embeddable
public class Direccion {

	// Campos relativos a la dirección
	private String direccion;
	private String ciudad;
	private String cp;

	// Metodos getter y setter 
	public String getDireccion() {
		return direccion;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getCp() {
		return cp;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public void setCp(String cp) {
		this.cp = cp;
	}
}
