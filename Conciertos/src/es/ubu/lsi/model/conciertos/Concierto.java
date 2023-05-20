package es.ubu.lsi.model.conciertos;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;


/**
 * The persistent class for the CONCIERTO database table.
 * 
 */
@Entity
@NamedQuery(name="Concierto.findAll", query="SELECT c FROM Concierto c")
public class Concierto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idconcierto;

	private String ciudad;

	private Date fecha;

	private String nombre;

	private double precio;

	private int tickets;

	//bi-directional many-to-one association to Compra
	@OneToMany(mappedBy="concierto")
	private Set<Compra> compras;

	//bi-directional many-to-one association to Grupo
	@ManyToOne
	@JoinColumn(name="IDGRUPO")
	private Grupo grupo;

	/**
	 * Constructor de la entidad concierto
	 */
	public Concierto() {
	}

	// Métodos setter y getter
	public int getIdconcierto() {
		return this.idconcierto;
	}

	public void setIdconcierto(int idconcierto) {
		this.idconcierto = idconcierto;
	}

	public String getCiudad() {
		return this.ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Date getFecha() {
		return this.fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return this.precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public int getTickets() {
		return this.tickets;
	}

	public void setTickets(int tickets) {
		this.tickets = tickets;
	}

	public Set<Compra> getCompras() {
		return this.compras;
	}

	public void setCompras(Set<Compra> compras) {
		this.compras = compras;
	}

	public Grupo getGrupo() {
		return this.grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
	@Override
	public String toString() {
		return "Concierto: Id: " + idconcierto + ". Nombre: " + nombre + ". Ciudad: " + ciudad + ". Fecha: " + fecha + ". Precio: " + precio + ". Tickets: " + tickets + ".";
	}

}