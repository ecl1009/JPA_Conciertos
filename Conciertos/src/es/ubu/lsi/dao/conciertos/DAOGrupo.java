package es.ubu.lsi.dao.conciertos;

import java.util.List;
import es.ubu.lsi.dao.JpaDAO;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import es.ubu.lsi.model.conciertos.Grupo;

/**
 * Clase que implementa el patrón DAO para acceder a los datos de la tabla concierto.
 * 
 * @author Eduardo Manuel Cabeza Lopez
 *
 * @param <E> tipo de la entidad
 * @param <K> tipo de la pk
 */
public class DAOGrupo<E, K> extends JpaDAO<Grupo, Integer> {

	/**
	 * Constructor de la clase DAOGrupo
	 * 
	 * @param em Gestor de entidades
	 */
	public DAOGrupo(EntityManager em) {
		super(em);
	}

	/**
	 * Modifica el estado activo de un grupo a 0
	 * 
	 * @param grupo Grupo a desactivar
	 */
	public void desactivarGrupo(Grupo grupo) {
		if (grupo.getActivo() == 1) {
			grupo.setActivo(0);
		}
	}
	
	/**
	 * Busqueda de todos los grupos y su información asociada en la base de datos.
	 * 
	 * @param nombreGrafo Grafo de entidades
	 * @param pista Pista del grafo
	 * @return Lista de grupos
	 */
	public List<Grupo> consultar(String nombreGrafo,String pista ) {
        return getEntityManager().createNamedQuery("Grupo.findAll", Grupo.class)
                .setHint(pista, getEntityManager().getEntityGraph(nombreGrafo))
                .getResultList();
    }

	/**
	 * Redefinicion del método findAll que realiza una busqueda de todos los grupos en la base de datos.
	 * 
	 * @return Lista de grupos
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Grupo> findAll() {
		Query query = getEntityManager().createQuery("select g "
				+ "from grupo g");
		List<Grupo> grupos = query.getResultList();
		return grupos;
	}
}
