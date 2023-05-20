package es.ubu.lsi.dao.conciertos;


import java.util.Date;
import java.util.List;
import es.ubu.lsi.dao.JpaDAO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import es.ubu.lsi.model.conciertos.Concierto;
import es.ubu.lsi.service.conciertos.IncidentException;

/**
 * Clase que implementa el patrón DAO para acceder a los datos de la tabla concierto.
 * 
 * @author Eduardo Manuel Cabeza Lopez
 *
 * @param <E> tipo de la entidad
 * @param <K> tipo de la pk
 */
public class DAOConcierto<E, K> extends JpaDAO<Concierto, Integer> {

	
	/**
	 * Constructor de la clase DAOConcierto
	 * 
	 * @param em Gestor de entidades
	 */
	public DAOConcierto(EntityManager em) {
		super(em);		
	}

	/**
	 * Busqueda de conciertos para un grupo y una fecha determinados en la base de datos.
	 * 
	 * @param fecha Fecha del concierto
	 * @param idGrupo PK del grupo
	 * @return Lista de conciertos
	 * @throws IncidentException
	 */
	// @SuppressWarnings("unchecked")
	public List<Concierto> findByFechaAndGrupo(Date fecha, int idGrupo) throws IncidentException {

		List<Concierto> concierto = getEntityManager()
				.createQuery("select c "
						+ "from Concierto c "
						+ "where c.fecha = ?1 "
						+ "and c.grupo.idgrupo = ?2", Concierto.class)
				.setParameter(1, fecha, TemporalType.TIMESTAMP).setParameter(2, idGrupo).getResultList();
		return concierto;
	}
			
	/**
	 * Busqueda de conciertos asociados a un grupo en la base de datos.
	 * 
	 * @param grupo Grupo para el que se buscan los conciertos
	 * @return Lista de conciertos
	 */
	@SuppressWarnings("unchecked")
	public List<Concierto> findByGrupo(int grupo) {
		return getEntityManager().createQuery("select c "
				+ "from Concierto c "
				+ "where c.grupo.idgrupo = :idGrupo")
		.setParameter("idGrupo", grupo).getResultList();		
	}

	/**
	 *Actualiza los tickets de un concierto.
	 * 
	 * @param tickets Nueva cantidad de tickets
	 * @param concierto Concierto al que se le modifica el número de tickets
	 */
	public void updateTickets(int tickets, Concierto concierto) {
		concierto.setTickets(tickets);
	}

	/**
	 * Redefinición del método findAll que realiza una busqueda de todos los conciertos en la base de datos
	 * 
	 * @return Lista de conciertos
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Concierto> findAll() {
		Query query = getEntityManager().createQuery("select c "
				+ "from concierto c");
		List<Concierto> conciertos = query.getResultList();
		return conciertos;
	}
}
