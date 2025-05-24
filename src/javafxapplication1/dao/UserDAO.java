package javafxapplication1.dao;

import javafxapplication1.model.User;

/**
 * Interfaz para el DAO de usuarios
 */
public interface UserDAO extends BaseDAO<User, Integer> {
    
    /**
     * Busca un usuario por su nombre de usuario
     * @param username Nombre de usuario
     * @return Usuario encontrado o null
     */
    User findByUsername(String username);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario y rol especificados
     * @param username Nombre de usuario
     * @param role Rol
     * @return true si existe, false en caso contrario
     */
    boolean existsByUsernameAndRole(String username, String role);
}