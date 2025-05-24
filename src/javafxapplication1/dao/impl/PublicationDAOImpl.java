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
import javafxapplication1.dao.PublicationDAO;
import javafxapplication1.model.Publication;
import javafxapplication1.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del DAO para publicaciones
 */
public class PublicationDAOImpl implements PublicationDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(PublicationDAOImpl.class);
    private static final String TABLE = "publication";
    
    @Override
    public Publication save(Publication publication) {
        String sql = "INSERT INTO " + TABLE + "(title, content, fee, created_at) VALUES(?, ?, ?, ?)";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, publication.getTitle());
            ps.setString(2, publication.getContent());
            ps.setDouble(3, publication.getFee());
            ps.setTimestamp(4, Timestamp.valueOf(publication.getCreatedAt()));
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating publication failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    publication.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating publication failed, no ID obtained.");
                }
            }
            
            return publication;
        } catch (SQLException e) {
            logger.error("Error saving publication", e);
            return null;
        }
    }
    
    @Override
    public Publication update(Publication publication) {
        String sql = "UPDATE " + TABLE + " SET title = ?, content = ?, fee = ?, created_at = ? WHERE id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, publication.getTitle());
            ps.setString(2, publication.getContent());
            ps.setDouble(3, publication.getFee());
            ps.setTimestamp(4, Timestamp.valueOf(publication.getCreatedAt()));
            ps.setInt(5, publication.getId());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating publication failed, no rows affected.");
            }
            
            return publication;
        } catch (SQLException e) {
            logger.error("Error updating publication", e);
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
            logger.error("Error deleting publication", e);
        }
    }
    
    @Override
    public Publication findById(Integer id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPublication(rs);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding publication by id", e);
        }
        
        return null;
    }
    
    @Override
    public List<Publication> findAll() {
        String sql = "SELECT * FROM " + TABLE + " ORDER BY created_at DESC";
        List<Publication> publications = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                publications.add(mapResultSetToPublication(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error finding all publications", e);
        }
        
        return publications;
    }
    
    @Override
    public List<Publication> findByTitle(String title) {
        String sql = "SELECT * FROM " + TABLE + " WHERE title LIKE ? ORDER BY created_at DESC";
        List<Publication> publications = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, "%" + title + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    publications.add(mapResultSetToPublication(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding publications by title", e);
        }
        
        return publications;
    }
    
    @Override
    public List<Publication> findByMaxFee(double maxFee) {
        String sql = "SELECT * FROM " + TABLE + " WHERE fee <= ? ORDER BY fee ASC";
        List<Publication> publications = new ArrayList<>();
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setDouble(1, maxFee);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    publications.add(mapResultSetToPublication(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error finding publications by max fee", e);
        }
        
        return publications;
    }
    
    private Publication mapResultSetToPublication(ResultSet rs) throws SQLException {
        Publication publication = new Publication();
        publication.setId(rs.getInt("id"));
        publication.setTitle(rs.getString("title"));
        publication.setContent(rs.getString("content"));
        publication.setFee(rs.getDouble("fee"));
        publication.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return publication;
    }
}