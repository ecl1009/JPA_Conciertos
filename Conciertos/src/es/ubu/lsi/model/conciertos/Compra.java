package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import javax.persistence.*;



/**
 * The persistent class for the COMPRA database table.
 * 
 */
@Entity
@NamedQuery(name="Compra.findAll", query="SELECT c FROM Compra c")
public class Compra implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idcompra;

	@Column(name="N_TICKETS")
	private int nTickets;

	//bi-directional many-to-one association to Cliente
	@ManyToOne
	@JoinColumn(name="NIF")
	private Cliente cliente;

	//bi-directional many-to-one association to Concierto
	@ManyToOne
	@JoinColumn(name="IDCONCIERTO")
	private Concierto concierto;

	/**
	 * Constructor de la entidad compra.
	 */
	public Compra() {
	}

	// Métodos setter y getter
	public int getIdcompra() {
		return this.idcompra;
	}

	public void setIdcompra(int idcompra) {
		this.idcompra = idcompra;
	}

	public int getNTickets() {
		return this.nTickets;
	}

	public void setNTickets(int nTickets) {
		this.nTickets = nTickets;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Concierto getConcierto() {
		return this.concierto;
	}

	public void setConcierto(Concierto concierto) {
		this.concierto = concierto;
	}
	
	@Override
	public String toString() {
		return "Compra: Id: " + idcompra + ". Tickets: " + nTickets + ".";
	}

}