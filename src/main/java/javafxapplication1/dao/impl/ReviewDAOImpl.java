package javafxapplication1.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafxapplication1.dao.ReviewDAO;
import javafxapplication1.model.Review;
import javafxapplication1.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del DAO para reseñas
 */
public class ReviewDAOImpl implements ReviewDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewDAOImpl.class);
    private static final String TABLE = "review";
    
    @Override
    public Review save(Review review) {
        String sql = "INSERT INTO " + TABLE + "(appointment_id, rating, comment, created_at) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, review.getAppointmentId());
            ps.setInt(2, review.getRating());
            ps.setString(3, review.getComment());
            ps.setTimestamp(4, Timestamp.valueOf(review.getCreatedAt()));
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating review failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating review failed, no ID obtained.");
                }
            }
            
            return review;
        } catch (SQLException e) {
            logger.error("Error saving review", e);
            return null;
        }
    }
    
    @Override
    public Review update(Review review) {
        String sql = "UPDATE " + TABLE + " SET appointment_id = ?, rating = ?, comment = ?, created_at = ? WHERE id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, review.getAppointmentId());
            ps.setInt(2, review.getRating());
            ps.setString(3, review.getComment());
            ps.setTimestamp(4, Timestamp.valueOf(review.getCreatedAt()));
            ps.setInt(5, review.getId());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating review failed, no rows affected.");
            }
            
            return review;
        } catch (SQLException e) {
            logger.error("Error updating review", e);
            return null;
        }
    }
    
    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM " + TABLE + " WHERE id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            logger.error("Error deleting review", e);
        }
    }
    
    @Override
    public Review findById(Integer id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReview(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding review by id", e);
        }
        
        return null;
    }
    
    @Override
    public List<Review> findAll() {
        String sql = "SELECT * FROM " + TABLE + " ORDER BY created_at DESC";
        List<Review> reviews = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all reviews", e);
        }
        
        return reviews;
    }
    
    @Override
    public List<Review> findByAppointmentId(Integer appointmentId) {
        String sql = "SELECT * FROM " + TABLE + " WHERE appointment_id = ? ORDER BY created_at DESC";
        List<Review> reviews = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, appointmentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding reviews by appointment id", e);
        }
        
        return reviews;
    }
    
    @Override
    public List<Review> findByMinRating(Integer minRating) {
        String sql = "SELECT * FROM " + TABLE + " WHERE rating >= ? ORDER BY rating DESC, created_at DESC";
        List<Review> reviews = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, minRating);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapResultSetToReview(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding reviews by minimum rating", e);
        }
        
        return reviews;
    }
    
    @Override
    public boolean existsByAppointmentId(Integer appointmentId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE + " WHERE appointment_id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, appointmentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error checking if review exists by appointment id", e);
        }
        
        return false;
    }
    
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setAppointmentId(rs.getInt("appointment_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return review;
    }
}