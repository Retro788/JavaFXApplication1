package javafxapplication1.dao;

import javafxapplication1.model.BaseEntity;
import java.util.List;

/**
 * Interfaz base para todos los DAOs
 * @param <T> Tipo de entidad
 * @param <ID> Tipo del identificador
 */
public interface BaseDAO<T extends BaseEntity, ID> {
    
    /**
     * Guarda una entidad
     * @param entity Entidad a guardar
     * @return Entidad guardada
     */
    T save(T entity);
    
    /**
     * Actualiza una entidad
     * @param entity Entidad a actualizar
     * @return Entidad actualizada
     */
    T update(T entity);
    
    /**
     * Elimina una entidad por su ID
     * @param id ID de la entidad a eliminar
     */
    void delete(ID id);
    
    /**
     * Busca una entidad por su ID
     * @param id ID de la entidad
     * @return Entidad encontrada o null
     */
    T findById(ID id);
    
    /**
     * Obtiene todas las entidades
     * @return Lista de entidades
     */
    List<T> findAll();
}