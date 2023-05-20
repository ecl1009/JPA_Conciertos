package es.ubu.lsi.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import es.ubu.lsi.model.conciertos.Cliente;

/**
 * Clase que implementa el patrón DAO para acceder a los datos de la tabla cliente.
 * 
 * @author Eduardo Manuel Cabeza Lopez
 *
 * @param <E> tipo de la entidad
 * @param <K> tipo de la pk
 */
public class DAOCliente<E,K> extends JpaDAO<Cliente, String> {

	/**
	 * Constructor de la clase DAOCliente.
	 * 
	 * @param em Gestor de entidades
	 */
	public DAOCliente(EntityManager em) {
		super(em);
	}

	/**
	 * Busqueda de clientes por nif en la base de datos.
	 * 
	 * @param nif NIF del cliente
	 * @return Objeto Cliente
	 */
	@SuppressWarnings("unchecked")
	public Cliente findByNif(String nif) {
		Query query = getEntityManager().createQuery("select c "
				+ "from cliente c "
				+ "where c.nif = ?1");
		query.setParameter(1, nif);
		List<Cliente> clientes = query.getResultList();
		if(!clientes.isEmpty()) {
			return clientes.get(0);
		}
		return null;
	}

	/**
	 * Redefinición del método findAll. Busca a todos los clientes en la base de datos.
	 * 
	 * @return Lista de clientes
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Cliente> findAll() {
		Query query = getEntityManager().createQuery("select c "
				+ "from Cliente c");
		return query.getResultList();
	}
}
