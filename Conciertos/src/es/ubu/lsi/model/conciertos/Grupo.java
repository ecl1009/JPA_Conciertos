package es.ubu.lsi.model.conciertos;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Set;


/**
 * The persistent class for the GRUPO database table.
 * 
 */
@Entity
@NamedQuery(name="Grupo.findAll", query="SELECT g FROM Grupo g")
@NamedEntityGraph(
        name = "gruposConConciertosComprasyClientes",
        attributeNodes = {
                @NamedAttributeNode(value="conciertos",subgraph = "conciertosComprasClientes")

        },
        subgraphs = {
                @NamedSubgraph(
                        name = "conciertosComprasClientes",
                        attributeNodes = {
                                @NamedAttributeNode(value = "compras",subgraph = "comprasClientes")
                        }),
                @NamedSubgraph(
                        name="comprasClientes",
                        attributeNodes = {
                                @NamedAttributeNode(value = "cliente")
                        })
        }
)
public class Grupo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int idgrupo;

	private int activo;

	private String estilo;

	private String nombre;

	//bi-directional many-to-one association to Concierto
	@OneToMany(mappedBy="grupo")
	private Set<Concierto> conciertos;

	/**
	 * Constructor de la entidad Grupo
	 */
	public Grupo() {
	}

	// Métodos setter y getter
	public int getIdgrupo() {
		return this.idgrupo;
	}

	public void setIdgrupo(int idgrupo) {
		this.idgrupo = idgrupo;
	}

	public int getActivo() {
		return this.activo;
	}

	public void setActivo(int activo) {
		this.activo = activo;
	}

	public String getEstilo() {
		return this.estilo;
	}

	public void setEstilo(String estilo) {
		this.estilo = estilo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<Concierto> getConciertos() {
		return this.conciertos;
	}

	public void setConciertos(Set<Concierto> conciertos) {
		this.conciertos = conciertos;
	}
	
	@Override
	public String toString() {
		return "Grupo: Id: " + idgrupo + ". Activo: " + activo + ". Estilo: " + estilo + ". Nombre: " + nombre + "."; 
	}

}