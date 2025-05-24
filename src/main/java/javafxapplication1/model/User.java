package javafxapplication1.model;

/**
 * Modelo que representa un usuario del sistema
 */
public class User extends BaseEntity {
    
    private String username;
    private String passwordHash;
    private String role;
    
    public User() {
    }
    
    public User(String username, String passwordHash, String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public String toString() {
        return "User{" + "id=" + getId() + ", username=" + username + ", role=" + role + "}";
    }
}