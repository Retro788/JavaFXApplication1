package javafxapplication1.model;

import java.time.LocalDateTime;

/**
 * Modelo que representa una publicación en el sistema
 */
public class Publication extends BaseEntity {
    
    private String title;
    private String content;
    private double fee;
    private LocalDateTime createdAt;
    
    public Publication() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Publication(String title, String content, double fee) {
        this.title = title;
        this.content = content;
        this.fee = fee;
        this.createdAt = LocalDateTime.now();
    }
    
    public Publication(String title, String content, double fee, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.fee = fee;
        this.createdAt = createdAt;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public double getFee() {
        return fee;
    }
    
    public void setFee(double fee) {
        this.fee = fee;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Publication{" + "id=" + getId() + ", title=" + title + ", fee=" + fee + ", createdAt=" + createdAt + "}";
    }
}