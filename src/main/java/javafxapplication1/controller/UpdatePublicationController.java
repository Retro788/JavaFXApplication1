package javafxapplication1.controller;

import java.io.IOException;
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
 * Controlador para la vista de actualizar publicaciones
 */
public class UpdatePublicationController {
    
    private static final Logger logger = LoggerFactory.getLogger(UpdatePublicationController.class);
    
    @FXML private TextField tfTitle;
    @FXML private TextField tfFee;
    @FXML private TextArea taContent;
    @FXML private Button btnUpdate;
    @FXML private Button btnClear;
    @FXML private Button btnBack;
    @FXML private Label lblStatus;
    
    private PublicationDAO pubDao = new PublicationDAOImpl();
    private Publication publication;
    private String currentRole;
    
    /**
     * Establece el rol actual del usuario y configura la interfaz según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        
        // Solo Practicante, Operator y Docente pueden actualizar publicaciones
        if (!"Practicante".equals(role) && !"Operator".equals(role) && !"Docente".equals(role)) {
            btnUpdate.setDisable(true);
            showAlert("Acceso restringido", "No tiene permisos para actualizar publicaciones", AlertType.WARNING);
        }
    }
    
    /**
     * Establece la publicación a actualizar y rellena los campos
     * @param publication La publicación a actualizar
     */
    public void setPublication(Publication publication) {
        this.publication = publication;
        populateFields();
    }
    
    /**
     * Rellena los campos con los datos de la publicación
     */
    private void populateFields() {
        if (publication != null) {
            tfTitle.setText(publication.getTitle());
            tfFee.setText(String.format("%.2f", publication.getFee()));
            taContent.setText(publication.getContent());
        }
    }
    
    /**
     * Maneja el evento de actualizar la publicación
     * @param event El evento de acción
     */
    @FXML
    private void onUpdate(ActionEvent event) {
        // Validar que la publicación no sea nula
        if (publication == null) {
            showAlert("Error", "No hay publicación para actualizar", AlertType.ERROR);
            return;
        }
        
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
            // Actualizar los datos de la publicación
            publication.setTitle(tfTitle.getText());
            publication.setContent(taContent.getText());
            publication.setFee(Double.parseDouble(tfFee.getText()));
            
            Publication updatedPublication = pubDao.update(publication);
            
            if (updatedPublication != null) {
                showAlert("Éxito", "Publicación actualizada correctamente", AlertType.INFORMATION);
                navigateToPublicationList();
            } else {
                showAlert("Error", "No se pudo actualizar la publicación", AlertType.ERROR);
            }
            
        } catch (Exception ex) {
            logger.error("Error al actualizar la publicación", ex);
            showAlert("Error", "Error al actualizar la publicación: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de restaurar los campos a los valores originales
     * @param event El evento de acción
     */
    @FXML
    private void onClear(ActionEvent event) {
        populateFields();
    }
    
    /**
     * Maneja el evento de volver a la lista de publicaciones
     * @param event El evento de acción
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateToPublicationList();
    }
    
    /**
     * Navega a la vista de lista de publicaciones
     */
    private void navigateToPublicationList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLPublicationList.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino
            PublicationListController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de lista de publicaciones", ex);
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