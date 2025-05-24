package javafxapplication1.util;

import javafxapplication1.model.User;

/**
 * Clase para gestionar la sesión del usuario actual
 */
public class SessionManager {
    
    private static SessionManager instance;
    private User currentUser;
    
    private SessionManager() {
        // Constructor privado para singleton
    }
    
    /**
     * Obtiene la instancia única de SessionManager
     * @return Instancia de SessionManager
     */
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Establece el usuario actual
     * @param user Usuario actual
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    /**
     * Obtiene el usuario actual
     * @return Usuario actual o null si no hay sesión
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Verifica si hay un usuario con sesión activa
     * @return true si hay sesión, false en caso contrario
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * Verifica si el usuario actual tiene el rol especificado
     * @param role Rol a verificar
     * @return true si tiene el rol, false en caso contrario
     */
    public boolean hasRole(String role) {
        return isLoggedIn() && currentUser.getRole().equals(role);
    }
    
    /**
     * Cierra la sesión actual
     */
    public void logout() {
        currentUser = null;
    }
}