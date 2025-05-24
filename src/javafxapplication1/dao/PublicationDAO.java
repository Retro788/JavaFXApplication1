package javafxapplication1.dao;

import javafxapplication1.model.Publication;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para operaciones de acceso a datos de publicaciones
 */
public interface PublicationDAO extends BaseDAO<Publication, Integer> {
    
    /**
     * Busca publicaciones por título
     * @param title El título a buscar
     * @return Lista de publicaciones que coinciden con el título
     * @throws SQLException Si ocurre un error de SQL
     */
    List<Publication> findByTitle(String title) throws SQLException;
    
    /**
     * Busca publicaciones con una tarifa menor o igual a la especificada
     * @param maxFee La tarifa máxima
     * @return Lista de publicaciones con tarifa menor o igual a maxFee
     * @throws SQLException Si ocurre un error de SQL
     */
    List<Publication> findByMaxFee(double maxFee) throws SQLException;
}