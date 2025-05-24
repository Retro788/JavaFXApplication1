package javafxapplication1.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapplication1.model.User;
import javafxapplication1.service.UserService;
import javafxapplication1.service.impl.UserServiceImpl;
import javafxapplication1.util.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la pantalla de login
 */
public class FXMLDocumentController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(FXMLDocumentController.class);
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnRegister;
    
    private final UserService userService;
    
    public FXMLDocumentController() {
        this.userService = new UserServiceImpl();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización del controlador
        lblStatus.setText("");
    }
    
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.ERROR, "Error de inicio de sesión", "Por favor, ingrese nombre de usuario y contraseña.");
            return;
        }
        
        User user = userService.authenticate(username, password);
        
        if (user != null) {
            // Guardar usuario en sesión
            SessionManager.getInstance().setCurrentUser(user);
            
            try {
                // Cargar pantalla principal
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLHome.fxml"));
                Parent root = loader.load();
                
                // Obtener controlador y establecer rol
                HomeController homeController = loader.getController();
                homeController.setUserRole(user.getRole());
                
                // Mostrar pantalla principal
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("DentalCare - Panel Principal");
                stage.show();
                
                logger.info("Usuario '{}' con rol '{}' ha iniciado sesión", user.getUsername(), user.getRole());
                
            } catch (IOException e) {
                logger.error("Error al cargar la pantalla principal", e);
                showAlert(AlertType.ERROR, "Error", "No se pudo cargar la pantalla principal.");
            }
        } else {
            lblStatus.setText("Credenciales inválidas");
            logger.warn("Intento de inicio de sesión fallido para el usuario: {}", username);
        }
    }
    
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        try {
            // Cargar pantalla de registro
            Parent root = FXMLLoader.load(getClass().getResource("/javafxapplication1/view/FXMLRegister.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("DentalCare - Registro");
            stage.show();
            
        } catch (IOException e) {
            logger.error("Error al cargar la pantalla de registro", e);
            showAlert(AlertType.ERROR, "Error", "No se pudo cargar la pantalla de registro.");
        }
    }
    
    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}