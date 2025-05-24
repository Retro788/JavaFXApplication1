package javafxapplication1.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafxapplication1.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilidad para migrar contraseñas de texto plano a hash BCrypt
 */
public class PasswordMigrationUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordMigrationUtil.class);
    
    /**
     * Migra todas las contraseñas de la tabla users a formato BCrypt
     * @return Número de contraseñas migradas
     */
    public static int migrateAllPasswords() {
        List<User> users = getAllUsersWithPlainPasswords();
        int migratedCount = 0;
        
        for (User user : users) {
            if (updatePasswordWithHash(user.getId(), user.getPasswordHash())) {
                migratedCount++;
            }
        }
        
        logger.info("Se migraron {} contraseñas a formato BCrypt", migratedCount);
        return migratedCount;
    }
    
    /**
     * Obtiene todos los usuarios con contraseñas en texto plano
     * @return Lista de usuarios
     */
    private static List<User> getAllUsersWithPlainPasswords() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password_hash, role FROM users";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRole(rs.getString("role"));
                
                // Verificar si la contraseña ya está en formato BCrypt
                if (!isPasswordHashed(user.getPasswordHash())) {
                    users.add(user);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al obtener usuarios para migración", e);
        }
        
        return users;
    }
    
    /**
     * Actualiza la contraseña de un usuario con hash BCrypt
     * @param userId ID del usuario
     * @param plainPassword Contraseña en texto plano
     * @return true si se actualizó correctamente, false en caso contrario
     */
    private static boolean updatePasswordWithHash(int userId, String plainPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE id = ?";
        String hashedPassword = HashingUtil.hash(plainPassword);
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hashedPassword);
            ps.setInt(2, userId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            logger.error("Error al actualizar contraseña con hash para el usuario {}", userId, e);
            return false;
        }
    }
    
    /**
     * Verifica si una contraseña ya está en formato BCrypt
     * @param password Contraseña a verificar
     * @return true si está en formato BCrypt, false en caso contrario
     */
    private static boolean isPasswordHashed(String password) {
        // Las contraseñas BCrypt comienzan con $2a$, $2b$ o $2y$
        return password != null && 
               (password.startsWith("$2a$") || 
                password.startsWith("$2b$") || 
                password.startsWith("$2y$"));
    }
    
    /**
     * Método principal para ejecutar la migración desde línea de comandos
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        logger.info("Iniciando migración de contraseñas...");
        int migratedCount = migrateAllPasswords();
        logger.info("Migración completada. Se migraron {} contraseñas.", migratedCount);
    }
}