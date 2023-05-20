package es.ubu.lsi.dao.conciertos;

import java.util.List;
import es.ubu.lsi.dao.JpaDAO;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import es.ubu.lsi.model.conciertos.*;
import es.ubu.lsi.service.conciertos.IncidentException;

/**
 * Clase que implementa el patrón DAO para acceder a los datos de la tabla compra.
 * 
 ** @author Eduardo Manuel Cabeza Lopez
 *
 * @param <E> tipo de la entidad
 * @param <K> tipo de la pk
 */
public class DAOCompra<E, K> extends JpaDAO<Compra, Integer> {

	/**
	 * Constructor de la clase DAOCompra.
	 * 
	 * @param em Gestor de entidades
	 */
	public DAOCompra(EntityManager em) {
		super(em);
	}

	/**
	 * Busqueda de compras por cliente a la base de datos.
	 * 
	 * @param idCliente PK del cliente buscado
	 * @return Lista de compras asociadas al cliente
	 * @throws IncidentException 
	 */
	@SuppressWarnings("unchecked")
	public List<Compra> findByCliente(int idCliente) throws IncidentException {
		Query query = getEntityManager().createQuery("select c " + "from Compra c " + "where c.idcliente = :idCliente");
		query.setParameter("idCliente", idCliente);
		List<Compra> compras = query.getResultList();
		if (!compras.isEmpty()) {
			return compras;
		}
		return null;

	}

	/**
	 * Devuelve el siguiente idcompra a usar realizando una busqueda en la base de datos del campo idcompra más alto e incrementandolo en una unidad. 
	 * 
	 * 
	 * @return Siguiente idcompra
	 */
	public int findNextId() {
		Compra compra = getEntityManager().createQuery("select c" + " from Compra c " + "where c.idcompra = "
				+ "(select max(c2.idcompra) " + "from Compra c2)", Compra.class).getSingleResult();
		int currentId = compra.getIdcompra();

		return currentId + 1;
	}

	/**
	 * Inserción de una fila en la tabla compra en la base de datos
	 * 
	 * @param idCompra PK de la compra
	 * @param cliente Cliente asociado a la compra
	 * @param concierto Concierto asociado a la compra.
	 * @param tickets Tickets comprados en la transacción
	 * @param compra Entidad compra a insertar en la base de datos
	 */
	public void insertCompra(int idCompra, Cliente cliente, Concierto concierto, int tickets, Compra compra) {

		compra.setIdcompra(idCompra);
		compra.setCliente(cliente);
		compra.setConcierto(concierto);
		compra.setNTickets(tickets);

	}

	/**
	 * Busqueda de todas las compras para conciertos de un mismo grupo en la base de datos
	 * 
	 * @param grupo Grupo para el que se buscan las compras
	 * @return Lista de compras
	 */
	public List<Compra> findAllByGroup(int grupo) {
		List<Compra> compras = getEntityManager()
				.createQuery("select c " + "from Compra c " + "where c.concierto.grupo.idgrupo = :grupo", Compra.class)
				.setParameter("grupo", grupo).getResultList();
		return compras;
	}

	
	
	/**
	 * Redefinición del método findAll que solicita una busqueda de todas las compras en la base de datos.
	 * 
	 * @return Lista de compras
	 */
	@Override
	public List<Compra> findAll() {
		List<Compra> compras = getEntityManager().createQuery("select c " + "from Compra c", Compra.class)
				.getResultList();

		return compras;
	}

	/**
	 * Busqueda de compras asociadas a un mismo concierto en la base de datos.
	 * 
	 * @param concierto Concierto para el que se buscan las compras
	 * @return Lista de compras
	 */
	@SuppressWarnings("unchecked")
	public List<Compra> findByConcierto(Concierto concierto) {

		Query query = getEntityManager()
				.createQuery("select c " + "from Compra c " + "where idConcierto = :idConcierto");
		query.setParameter("idConcierto", concierto);
		List<Compra> compras = query.getResultList();
		if (!compras.isEmpty()) {
			return compras;
		}
		return null;

	}
	
	/**
	 * Elimina todas las compras asociadas a un concierto.
	 * Realiza la operación contra la base de datos. Hay que tener precaución en su uso.
	 * 
	 * @param concierto Concierto del que se quieren eliminar todas las compras.
	 */
	public void removeByConcierto(Concierto concierto) {		
		getEntityManager().createQuery("delete from Compra c where c.concierto.idconcierto = ?1").setParameter(1, concierto.getIdconcierto()).executeUpdate();
	}

}
