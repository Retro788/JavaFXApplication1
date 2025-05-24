package javafxapplication1.service;

import javafxapplication1.model.User;
import java.util.List;

/**
 * Servicio para la gestión de usuarios
 */
public interface UserService {
    
    /**
     * Registra un nuevo usuario
     * @param username Nombre de usuario
     * @param password Contraseña sin encriptar
     * @param role Rol del usuario
     * @return Usuario registrado o null si falla
     */
    User register(String username, String password, String role);
    
    /**
     * Autentica un usuario
     * @param username Nombre de usuario
     * @param password Contraseña sin encriptar
     * @return Usuario autenticado o null si falla
     */
    User authenticate(String username, String password);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario especificado
     * @param username Nombre de usuario
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsername(String username);
    
    /**
     * Obtiene todos los usuarios
     * @return Lista de usuarios
     */
    List<User> getAllUsers();
    
    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado o null
     */
    User getUserById(Integer id);
    
    /**
     * Actualiza un usuario
     * @param user Usuario a actualizar
     * @return Usuario actualizado o null si falla
     */
    User updateUser(User user);
    
    /**
     * Elimina un usuario por su ID
     * @param id ID del usuario a eliminar
     */
    void deleteUser(Integer id);
}