package javafxapplication1.model;

import java.time.LocalDateTime;

/**
 * Modelo para las reseñas de citas
 */
public class Review extends BaseEntity {
    
    private Integer appointmentId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    
    /**
     * Constructor por defecto
     */
    public Review() {
    }
    
    /**
     * Constructor con todos los campos excepto id
     * 
     * @param appointmentId ID de la cita asociada
     * @param rating Calificación (1-5)
     * @param comment Comentario de la reseña
     * @param createdAt Fecha de creación
     */
    public Review(Integer appointmentId, Integer rating, String comment, LocalDateTime createdAt) {
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }
    
    /**
     * Constructor con todos los campos
     * 
     * @param id ID de la reseña
     * @param appointmentId ID de la cita asociada
     * @param rating Calificación (1-5)
     * @param comment Comentario de la reseña
     * @param createdAt Fecha de creación
     */
    public Review(Integer id, Integer appointmentId, Integer rating, String comment, LocalDateTime createdAt) {
        super(id);
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    /**
     * @return ID de la cita asociada
     */
    public Integer getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId ID de la cita a establecer
     */
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return Calificación de la reseña
     */
    public Integer getRating() {
        return rating;
    }

    /**
     * @param rating Calificación a establecer (1-5)
     */
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    /**
     * @return Comentario de la reseña
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment Comentario a establecer
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return Fecha de creación
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt Fecha de creación a establecer
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Review{" + "id=" + getId() + ", appointmentId=" + appointmentId + ", rating=" + rating + ", comment=" + comment + ", createdAt=" + createdAt + '}';
    }
}