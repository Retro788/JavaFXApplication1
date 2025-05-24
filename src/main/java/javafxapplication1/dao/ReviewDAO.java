package javafxapplication1.dao;

import java.util.List;
import javafxapplication1.model.Review;

/**
 * Interfaz DAO para las operaciones con reseñas
 */
public interface ReviewDAO extends BaseDAO<Review, Integer> {
    
    /**
     * Busca reseñas por ID de cita
     * 
     * @param appointmentId ID de la cita
     * @return Lista de reseñas asociadas a la cita
     */
    List<Review> findByAppointmentId(Integer appointmentId);
    
    /**
     * Busca reseñas por calificación mínima
     * 
     * @param minRating Calificación mínima (1-5)
     * @return Lista de reseñas con calificación igual o superior a la especificada
     */
    List<Review> findByMinRating(Integer minRating);
    
    /**
     * Verifica si ya existe una reseña para una cita específica
     * 
     * @param appointmentId ID de la cita
     * @return true si ya existe una reseña para la cita, false en caso contrario
     */
    boolean existsByAppointmentId(Integer appointmentId);
}