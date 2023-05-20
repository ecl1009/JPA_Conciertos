package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Set;


/**
 * The persistent class for the CLIENTE database table.
 * 
 */
@Entity
@NamedQuery(name="Cliente.findAll", query="SELECT c FROM Cliente c")
public class Cliente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nif;

	private String apellidos;

	// Tipo embebido definido en la clase Direccion
	@Embedded
	private Direccion direccion;

	private String nombre;

	//bi-directional many-to-one association to Compra
	@OneToMany(mappedBy="cliente")
	private Set<Compra> compras;

	/**
	 * Constructor de la entidad.
	 */
	public Cliente() {
	}

	// Métodos setter y getter
	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

	@Override
	public String toString() {
		return "Cliente: Id: " + nif + ". Apellidos: " + apellidos + ". Nombre: " + nombre + ". Dirección: " + direccion.getDireccion() + ". Ciudad: " + direccion.getCiudad() + ". Cod. Postal: " + direccion.getCp() + ".";
	}
}