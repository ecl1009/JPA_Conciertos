package es.ubu.lsi.service.conciertos;

import java.util.Date;
import java.util.List;


import javax.persistence.EntityManager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ubu.lsi.dao.conciertos.DAOCliente;
import es.ubu.lsi.dao.conciertos.DAOCompra;
import es.ubu.lsi.dao.conciertos.DAOConcierto;
import es.ubu.lsi.dao.conciertos.DAOGrupo;
import es.ubu.lsi.model.conciertos.Cliente;
import es.ubu.lsi.model.conciertos.Compra;
import es.ubu.lsi.model.conciertos.Concierto;
import es.ubu.lsi.model.conciertos.Grupo;
import es.ubu.lsi.service.PersistenceException;
import es.ubu.lsi.service.PersistenceService;

/**
 * Clase ServiceImpl que implementa la logica de negocio. 
 * Hereda de PersistenceService e implementa la interfaz Service.
 * 
 * @author Eduardo Manuel Cabeza Lopez
 */
public class ServiceImpl extends PersistenceService implements Service {

	private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

	private EntityManager em;
	private DAOGrupo<Grupo, Integer> grupoDAO;
	private DAOConcierto<Concierto, Integer> conciertoDAO;
	private DAOCliente<Cliente, String> clienteDAO;
	private DAOCompra<Compra, Integer> compraDAO;

	/**
	 * Constructor de la clase ServiceImpl.
	 */
	public ServiceImpl() {
		super();
	}

	/**
	 * Implementación de la transacción comprar. Inserta una compra en la base de datos.
	 * El método comprueba que todos los parámetros pasados existan en la base de datos 
	 * y que la disponibilidad de tickets puedan satisfacer la demanda.
	 * 
	 * @param fecha Fecha del concierto
	 * @param nif NIF del cliente
	 * @param grupo PK del grupo que toca en el concierto
	 * @param tickets Cantidad de tickets a comprar
	 */
	@Override
	public void comprar(Date fecha, String nif, int grupo, int tickets) throws PersistenceException {
		try {

			em = this.createSession();
			grupoDAO = new DAOGrupo<Grupo, Integer>(em);
			conciertoDAO = new DAOConcierto<Concierto, Integer>(em);
			clienteDAO = new DAOCliente<Cliente, String>(em);
			compraDAO = new DAOCompra<Compra, Integer>(em);
			beginTransaction(em); // Inicia transacción		

			Cliente cliente = clienteDAO.findById(nif); // Busca al cliente
			if (cliente == null) { // Comprueba que el cliente existe

				throw new IncidentException(IncidentError.NOT_EXIST_CLIENT);
			}
			Grupo grupoOb = grupoDAO.findById(grupo);

			if (grupoOb == null) { // Comprueba que el grupo existe
				throw new IncidentException(IncidentError.NOT_EXIST_MUSIC_GROUP);
			}

			List<Concierto> concierto = conciertoDAO.findByFechaAndGrupo(fecha, grupo); // Busca el concierto
			if (concierto.size() == 0) { // Comprueba que el concierto existe por grupo y fecha.
				throw new IncidentException(IncidentError.NOT_EXIST_CONCERT);
			}

			int ticketsDisponibles = concierto.get(0).getTickets();
			if (tickets > ticketsDisponibles) { // Comprobar que hay suficientes tickets disponibles
				throw new IncidentException(IncidentError.NOT_AVAILABLE_TICKETS);
			}

			conciertoDAO.updateTickets(ticketsDisponibles - tickets, concierto.get(0)); // Actualiza tickets

			int idCompra = compraDAO.findNextId(); // Obtiene siguiente PK para compra

			Compra compra = new Compra(); // Crea nueva compra
			compraDAO.insertCompra(idCompra, cliente, concierto.get(0), tickets, compra); // Inseta la compra
			em.persist(compra); // Hace a la entidad persistente

			commitTransaction(em); //Cometer transacción

		} catch (Exception e) {
			logger.error("Exception");
			if (em.getTransaction().isActive()) {
				System.out.println("Comit rollback");
				rollbackTransaction(em);
			}
			logger.error(e.getLocalizedMessage());
			if (e instanceof IncidentException) {
				throw (IncidentException) e;
			} else {
				throw e;
			}
		} finally {
			em.close();
		}
	}

	/**
	 * Implementación de la transacción desactivar. 
	 * Desactiva a un grupo y elimina toda la información asociada que se tenga en la base de datos.
	 * 
	 * @param grupo Grupo a desactivar
	 */
	@Override
	public void desactivar(int grupo) throws PersistenceException {

		em = this.createSession();
		grupoDAO = new DAOGrupo<Grupo, Integer>(em);
		conciertoDAO = new DAOConcierto<Concierto, Integer>(em);
		clienteDAO = new DAOCliente<Cliente, String>(em);
		compraDAO = new DAOCompra<Compra, Integer>(em);

		try {
			beginTransaction(em); // Inicia transacción

			Grupo grupoOb = grupoDAO.findById(grupo); // Busca el grupo

			if (grupoOb == null) { // Comprueba que el grupo existe
				throw new IncidentException(IncidentError.NOT_EXIST_MUSIC_GROUP);
			}

			grupoDAO.desactivarGrupo(grupoOb); // Desactiva al grupo

			/*List<Compra> compras = compraDAO.findAllByGroup(grupo); // Busca todas las compras asociadas al grupo

			if (compras.size() != 0) { // Si existen compras asociadas al grupo las elimina
				for (Compra compra : compras) {
					compraDAO.remove(compra);
				}
			}*/

			List<Concierto> conciertos = conciertoDAO.findByGrupo(grupo); // Busca todos los conciertos del grupo

			if (conciertos.size() != 0) { // Si existen conciertos asociados al grupo los elimina
				for (Concierto concierto : conciertos) {
					compraDAO.removeByConcierto(concierto);
					conciertoDAO.remove(concierto);
					
				}
			}

			commitTransaction(em); // Cometer transacción
		} catch (Exception e) {
			logger.error("Exception");
			if (em.getTransaction().isActive()) {
				System.out.println("Comit rollback");
				rollbackTransaction(em);
			}
			logger.error(e.getLocalizedMessage());
			if (e instanceof IncidentException) {
				throw (IncidentException) e;
			} else {
				throw e;
			}
		}finally {
			em.close();
		}

	}

	/**
	 * Implementación de la transacción consultarGrupos.
	 * Devuelve toda la información de todos los grupos, incluyendo conciertos,
	 * compras para cada concierto y cliente de cada compra. Todo esto lo hace usando un grafo de entidades
	 * con subgrafos
	 * 
	 * @return Lista de grupos
	 */
	@Override
    public List<Grupo> consultarGrupos() throws PersistenceException {
        EntityManager em = this.createSession();
        try {
            beginTransaction(em); // Inicia la transacción
            DAOGrupo<Grupo,Integer> grupoDAO = new DAOGrupo<Grupo,Integer>(em);  
            List<Grupo> listado = grupoDAO.consultar("gruposConConciertosComprasyClientes", "javax.persistence.fetchgraph"); // obtiene la información de los grupos usando el grafo
            commitTransaction(em); // comete la transacción
            return listado; // Retorna los resultados
            
        } catch (Exception e){
        	logger.error("Exception");
			if (em.getTransaction().isActive()) {
				System.out.println("Comit rollback");
				rollbackTransaction(em);
			}
			throw e;			
			/*logger.error(e.getLocalizedMessage());
			if (e instanceof IncidentException) {
				throw (IncidentException) e;
			} else {
				throw e;
			}*/
        } finally {
            em.close();
        }
    }

}
