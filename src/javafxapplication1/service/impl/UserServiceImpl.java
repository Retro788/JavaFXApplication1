package javafxapplication1.service.impl;

import java.util.List;
import javafxapplication1.dao.UserDAO;
import javafxapplication1.dao.impl.UserDAOImpl;
import javafxapplication1.model.User;
import javafxapplication1.service.UserService;
import javafxapplication1.util.HashingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del servicio de usuarios
 */
public class UserServiceImpl implements UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;
    
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }
    
    @Override
    public User register(String username, String password, String role) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty() || 
            role == null || role.trim().isEmpty()) {
            logger.warn("Attempted to register with invalid data: username={}, role={}", username, role);
            return null;
        }
        
        // Verificar si el usuario ya existe
        if (userDAO.findByUsername(username) != null) {
            logger.warn("Attempted to register with existing username: {}", username);
            return null;
        }
        
        // Crear nuevo usuario
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(HashingUtil.hash(password));
        user.setRole(role);
        
        // Guardar usuario
        User savedUser = userDAO.save(user);
        if (savedUser != null) {
            logger.info("User registered successfully: {}", username);
        } else {
            logger.error("Failed to register user: {}", username);
        }
        
        return savedUser;
    }
    
    @Override
    public User authenticate(String username, String password) {
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            logger.warn("Attempted to authenticate with invalid credentials");
            return null;
        }
        
        // Buscar usuario por nombre de usuario
        User user = userDAO.findByUsername(username);
        if (user == null) {
            logger.warn("Authentication failed: user not found: {}", username);
            return null;
        }
        
        // Verificar contraseña
        if (!HashingUtil.verify(password, user.getPasswordHash())) {
            logger.warn("Authentication failed: invalid password for user: {}", username);
            return null;
        }
        
        logger.info("User authenticated successfully: {}", username);
        return user;
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userDAO.findByUsername(username) != null;
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }
    
    @Override
    public User getUserById(Integer id) {
        return userDAO.findById(id);
    }
    
    @Override
    public User updateUser(User user) {
        if (user == null || user.getId() == null) {
            logger.warn("Attempted to update invalid user");
            return null;
        }
        
        // Verificar si el usuario existe
        User existingUser = userDAO.findById(user.getId());
        if (existingUser == null) {
            logger.warn("Attempted to update non-existing user: {}", user.getId());
            return null;
        }
        
        // Actualizar usuario
        User updatedUser = userDAO.update(user);
        if (updatedUser != null) {
            logger.info("User updated successfully: {}", user.getUsername());
        } else {
            logger.error("Failed to update user: {}", user.getUsername());
        }
        
        return updatedUser;
    }
    
    @Override
    public void deleteUser(Integer id) {
        if (id == null) {
            logger.warn("Attempted to delete user with null id");
            return;
        }
        
        // Verificar si el usuario existe
        User existingUser = userDAO.findById(id);
        if (existingUser == null) {
            logger.warn("Attempted to delete non-existing user: {}", id);
            return;
        }
        
        // Eliminar usuario
        userDAO.delete(id);
        logger.info("User deleted successfully: {}", existingUser.getUsername());
    }
}