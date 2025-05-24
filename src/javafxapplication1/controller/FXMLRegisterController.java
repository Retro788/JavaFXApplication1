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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapplication1.model.User;
import javafxapplication1.service.UserService;
import javafxapplication1.service.impl.UserServiceImpl;
import javafxapplication1.util.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la pantalla de registro
 */
public class FXMLRegisterController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(FXMLRegisterController.class);
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private PasswordField txtConfirmPassword;
    
    @FXML
    private ComboBox<String> cmbUserType;
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private Button btnRegister;
    
    @FXML
    private Button btnBack;
    
    private final UserService userService;
    
    public FXMLRegisterController() {
        this.userService = new UserServiceImpl();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar combo de tipos de usuario
        cmbUserType.getItems().add(ConfigLoader.getProperty("role.dentist"));
        cmbUserType.getItems().add(ConfigLoader.getProperty("role.operator"));
        cmbUserType.getSelectionModel().selectFirst();
        
        lblStatus.setText("");
    }
    
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String userType = cmbUserType.getValue();
        
        // Validar campos
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || userType == null) {
            showAlert(AlertType.ERROR, "Error de registro", "Por favor, complete todos los campos.");
            return;
        }
        
        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            showAlert(AlertType.ERROR, "Error de registro", "Las contraseñas no coinciden.");
            return;
        }
        
        // Validar que el nombre de usuario no exista
        if (userService.existsByUsername(username)) {
            showAlert(AlertType.ERROR, "Error de registro", "El nombre de usuario ya existe.");
            return;
        }
        
        // Registrar usuario
        User user = userService.register(username, password, userType);
        
        if (user != null) {
            logger.info("Usuario registrado exitosamente: {}", username);
            showAlert(AlertType.INFORMATION, "Registro exitoso", "Usuario registrado correctamente.");
            
            // Volver a la pantalla de login
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/javafxapplication1/view/FXMLDocument.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("DentalCare - Login");
                stage.show();
                
            } catch (IOException e) {
                logger.error("Error al cargar la pantalla de login", e);
                showAlert(AlertType.ERROR, "Error", "No se pudo cargar la pantalla de login.");
            }
        } else {
            lblStatus.setText("Error al registrar usuario");
            logger.error("Error al registrar usuario: {}", username);
        }
    }
    
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Volver a la pantalla de login
            Parent root = FXMLLoader.load(getClass().getResource("/javafxapplication1/view/FXMLDocument.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("DentalCare - Login");
            stage.show();
            
        } catch (IOException e) {
            logger.error("Error al cargar la pantalla de login", e);
            showAlert(AlertType.ERROR, "Error", "No se pudo cargar la pantalla de login.");
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