package javafxapplication1.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafxapplication1.dao.ReviewDAO;
import javafxapplication1.dao.impl.ReviewDAOImpl;
import javafxapplication1.model.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la vista de actualización de reseñas
 */
public class UpdateReviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(UpdateReviewController.class);
    
    @FXML private Label lblAppointmentId;
    @FXML private RadioButton rbRating1;
    @FXML private RadioButton rbRating2;
    @FXML private RadioButton rbRating3;
    @FXML private RadioButton rbRating4;
    @FXML private RadioButton rbRating5;
    @FXML private ToggleGroup ratingGroup;
    @FXML private TextArea taComment;
    @FXML private Button btnUpdate;
    @FXML private Button btnClear;
    @FXML private Button btnBack;
    
    private ReviewDAO reviewDao = new ReviewDAOImpl();
    private Review currentReview;
    private String currentRole;
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        // Inicializar el grupo de botones de radio para la calificación
        if (ratingGroup == null) {
            ratingGroup = new ToggleGroup();
        }
        rbRating1.setToggleGroup(ratingGroup);
        rbRating2.setToggleGroup(ratingGroup);
        rbRating3.setToggleGroup(ratingGroup);
        rbRating4.setToggleGroup(ratingGroup);
        rbRating5.setToggleGroup(ratingGroup);
    }
    
    /**
     * Establece el rol actual del usuario y configura la interfaz según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        
        // Solo Paciente puede actualizar sus propias reseñas
        if (!"Paciente".equals(role)) {
            btnUpdate.setDisable(true);
            showAlert("Acceso restringido", "Solo los pacientes pueden actualizar sus reseñas", AlertType.WARNING);
        }
    }
    
    /**
     * Establece la reseña actual y carga sus datos en la interfaz
     * @param review La reseña a actualizar
     */
    public void setReview(Review review) {
        this.currentReview = review;
        populateFields();
    }
    
    /**
     * Carga los datos de la reseña actual en los campos de la interfaz
     */
    private void populateFields() {
        if (currentReview != null) {
            lblAppointmentId.setText(String.valueOf(currentReview.getAppointmentId()));
            
            // Seleccionar el botón de radio correspondiente a la calificación
            switch (currentReview.getRating()) {
                case 1:
                    rbRating1.setSelected(true);
                    break;
                case 2:
                    rbRating2.setSelected(true);
                    break;
                case 3:
                    rbRating3.setSelected(true);
                    break;
                case 4:
                    rbRating4.setSelected(true);
                    break;
                case 5:
                    rbRating5.setSelected(true);
                    break;
                default:
                    // No seleccionar ninguno si el valor no es válido
                    break;
            }
            
            taComment.setText(currentReview.getComment());
        }
    }
    
    /**
     * Maneja el evento de actualizar la reseña
     * @param event El evento de acción
     */
    @FXML
    private void onUpdate(ActionEvent event) {
        if (!validateInput()) {
            return;
        }
        
        try {
            // Actualizar los datos de la reseña con los valores de la interfaz
            currentReview.setRating(getSelectedRating());
            currentReview.setComment(taComment.getText().trim());
            
            // Guardar la reseña actualizada
            reviewDao.update(currentReview);
            
            showAlert("Éxito", "Reseña actualizada correctamente", AlertType.INFORMATION);
            navigateToReviewList();
            
        } catch (SQLException ex) {
            logger.error("Error al actualizar la reseña", ex);
            showAlert("Error", "Error al actualizar la reseña: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Valida los datos de entrada
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean validateInput() {
        // Validar que se haya seleccionado una calificación
        if (ratingGroup.getSelectedToggle() == null) {
            showAlert("Error de validación", "Debe seleccionar una calificación", AlertType.WARNING);
            return false;
        }
        
        // Validar que el comentario no esté vacío
        if (taComment.getText().trim().isEmpty()) {
            showAlert("Error de validación", "El comentario no puede estar vacío", AlertType.WARNING);
            return false;
        }
        
        return true;
    }
    
    /**
     * Obtiene la calificación seleccionada
     * @return El valor de la calificación (1-5)
     */
    private int getSelectedRating() {
        RadioButton selectedRating = (RadioButton) ratingGroup.getSelectedToggle();
        
        if (selectedRating == rbRating1) return 1;
        if (selectedRating == rbRating2) return 2;
        if (selectedRating == rbRating3) return 3;
        if (selectedRating == rbRating4) return 4;
        if (selectedRating == rbRating5) return 5;
        
        // Valor por defecto si no se ha seleccionado ninguno (no debería ocurrir debido a la validación)
        return 0;
    }
    
    /**
     * Maneja el evento de limpiar los campos
     * @param event El evento de acción
     */
    @FXML
    private void onClear(ActionEvent event) {
        // Restaurar los valores originales de la reseña
        populateFields();
    }
    
    /**
     * Maneja el evento de volver a la lista de reseñas
     * @param event El evento de acción
     */
    @FXML
    private void handleBack(ActionEvent event) {
        navigateToReviewList();
    }
    
    /**
     * Navega a la vista de lista de reseñas
     */
    private void navigateToReviewList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLReviewList.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino
            ReviewListController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) btnBack.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de lista de reseñas", ex);
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