package javafxapplication1.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapplication1.dao.PublicationDAO;
import javafxapplication1.dao.impl.PublicationDAOImpl;
import javafxapplication1.model.Publication;
import javafxapplication1.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la vista de añadir publicaciones
 */
public class AddPublicationController {
    
    private static final Logger logger = LoggerFactory.getLogger(AddPublicationController.class);
    
    @FXML private TextField tfTitle;
    @FXML private TextField tfFee;
    @FXML private TextArea taContent;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    @FXML private Button btnBack;
    @FXML private Label lblStatus;
    
    private PublicationDAO pubDao = new PublicationDAOImpl();
    private String currentRole;
    
    /**
     * Establece el rol actual del usuario y configura la interfaz según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        
        // Solo Practicante, Operator y Docente pueden crear publicaciones
        if (!"Practicante".equals(role) && !"Operator".equals(role) && !"Docente".equals(role)) {
            btnSave.setDisable(true);
            showAlert("Acceso restringido", "No tiene permisos para crear publicaciones", AlertType.WARNING);
        }
    }
    
    /**
     * Maneja el evento de guardar una nueva publicación
     * @param event El evento de acción
     */
    @FXML
    private void onSave(ActionEvent event) {
        // Validar campos obligatorios
        if (tfTitle.getText().isBlank()) {
            showAlert("Error", "El título es obligatorio", AlertType.WARNING);
            tfTitle.requestFocus();
            return;
        }
        
        if (tfFee.getText().isBlank()) {
            showAlert("Error", "La cuota es obligatoria", AlertType.WARNING);
            tfFee.requestFocus();
            return;
        }
        
        // Validar que la cuota sea un número decimal válido
        if (!ValidationUtil.isDecimal(tfFee.getText())) {
            showAlert("Error", "La cuota debe ser un valor numérico válido", AlertType.WARNING);
            tfFee.requestFocus();
            return;
        }
        
        try {
            // Crear y guardar la publicación
            Publication publication = new Publication();
            publication.setTitle(tfTitle.getText());
            publication.setContent(taContent.getText());
            publication.setFee(Double.parseDouble(tfFee.getText()));
            publication.setCreatedAt(LocalDateTime.now());
            
            Publication savedPublication = pubDao.save(publication);
            
            if (savedPublication != null) {
                showAlert("Éxito", "Publicación guardada correctamente", AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Error", "No se pudo guardar la publicación", AlertType.ERROR);
            }
            
        } catch (Exception ex) {
            logger.error("Error al guardar la publicación", ex);
            showAlert("Error", "Error al guardar la publicación: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Limpia los campos del formulario
     * @param event El evento de acción
     */
    @FXML
    private void onClear(ActionEvent event) {
        clearFields();
    }
    
    /**
     * Método para limpiar los campos del formulario
     */
    private void clearFields() {
        tfTitle.clear();
        tfFee.clear();
        taContent.clear();
        lblStatus.setText("");
        tfTitle.requestFocus();
    }
    
    /**
     * Maneja el evento de volver a la pantalla anterior
     * @param event El evento de acción
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLHome.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino si es necesario
            HomeController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista Home", ex);
            showAlert("Error", "Error al cargar la vista: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de cerrar la aplicación
     * @param event El evento de acción
     */
    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Maneja el evento de cerrar sesión
     * @param event El evento de acción
     */
    @FXML
    private void logoutButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/javafxapplication1/view/FXMLDocument.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("DentalCare - Login");
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de login", ex);
            showAlert("Error", "Error al cargar la vista de login: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Muestra una alerta con el mensaje y tipo especificados
     * @param title El título de la alerta
     * @param message El mensaje de la alerta
     * @param alertType El tipo de alerta
     */
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}