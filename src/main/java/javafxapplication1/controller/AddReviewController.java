package javafxapplication1.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafxapplication1.dao.ReviewDAO;
import javafxapplication1.dao.impl.ReviewDAOImpl;
import javafxapplication1.model.Review;
import javafxapplication1.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la vista de añadir reseñas
 */
public class AddReviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(AddReviewController.class);
    
    @FXML private ComboBox<AppointmentInfo> cbAppointment;
    @FXML private RadioButton rb1;
    @FXML private RadioButton rb2;
    @FXML private RadioButton rb3;
    @FXML private RadioButton rb4;
    @FXML private RadioButton rb5;
    @FXML private ToggleGroup ratingGroup;
    @FXML private TextArea taComment;
    @FXML private Button btnSave;
    @FXML private Button btnClear;
    @FXML private Button btnBack;
    @FXML private Label lblStatus;
    
    private ReviewDAO reviewDao = new ReviewDAOImpl();
    private String currentRole;
    private Map<Integer, RadioButton> ratingButtons = new HashMap<>();
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        // Inicializar el mapa de botones de calificación
        ratingButtons.put(1, rb1);
        ratingButtons.put(2, rb2);
        ratingButtons.put(3, rb3);
        ratingButtons.put(4, rb4);
        ratingButtons.put(5, rb5);
        
        // Configurar el ComboBox de citas
        setupAppointmentComboBox();
        
        // Cargar las citas disponibles
        loadAppointments();
    }
    
    /**
     * Establece el rol actual del usuario y configura la interfaz según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        
        // Solo Paciente puede crear reseñas
        if (!"Paciente".equals(role)) {
            btnSave.setDisable(true);
            showAlert("Acceso restringido", "Solo los pacientes pueden crear reseñas", AlertType.WARNING);
        }
    }
    
    /**
     * Configura el ComboBox de citas
     */
    private void setupAppointmentComboBox() {
        // Configurar el convertidor para mostrar información de la cita
        cbAppointment.setConverter(new StringConverter<AppointmentInfo>() {
            @Override
            public String toString(AppointmentInfo appointment) {
                if (appointment == null) {
                    return null;
                }
                return String.format("Cita #%d - %s - %s", 
                        appointment.getId(), 
                        appointment.getDate(), 
                        appointment.getDentistName());
            }

            @Override
            public AppointmentInfo fromString(String string) {
                return null; // No es necesario para este caso
            }
        });
    }
    
    /**
     * Carga las citas disponibles para el paciente actual
     */
    private void loadAppointments() {
        ObservableList<AppointmentInfo> appointments = FXCollections.observableArrayList();
        
        // Consulta para obtener las citas del paciente actual que no tienen reseña
        String sql = "SELECT a.id, a.appointment_date, d.name AS dentist_name " +
                     "FROM appointment a " +
                     "JOIN dentist d ON a.dentist_id = d.id " +
                     "LEFT JOIN review r ON a.id = r.appointment_id " +
                     "WHERE a.patient_id = (SELECT id FROM patient WHERE username = ?) " +
                     "AND r.id IS NULL " +
                     "ORDER BY a.appointment_date DESC";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Usar el nombre de usuario actual (del rol)
            ps.setString(1, currentRole);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AppointmentInfo appointment = new AppointmentInfo(
                            rs.getInt("id"),
                            rs.getDate("appointment_date").toString(),
                            rs.getString("dentist_name")
                    );
                    appointments.add(appointment);
                }
            }
            
            cbAppointment.setItems(appointments);
            
            if (appointments.isEmpty()) {
                lblStatus.setText("No hay citas disponibles para reseñar");
                btnSave.setDisable(true);
            }
            
        } catch (SQLException e) {
            logger.error("Error al cargar las citas", e);
            showAlert("Error", "Error al cargar las citas: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de guardar una nueva reseña
     * @param event El evento de acción
     */
    @FXML
    private void onSave(ActionEvent event) {
        // Validar que se haya seleccionado una cita
        if (cbAppointment.getValue() == null) {
            showAlert("Error", "Debe seleccionar una cita", AlertType.WARNING);
            cbAppointment.requestFocus();
            return;
        }
        
        // Validar que se haya seleccionado una calificación
        if (ratingGroup.getSelectedToggle() == null) {
            showAlert("Error", "Debe seleccionar una calificación", AlertType.WARNING);
            return;
        }
        
        try {
            // Obtener la calificación seleccionada
            int rating = 0;
            for (Map.Entry<Integer, RadioButton> entry : ratingButtons.entrySet()) {
                if (entry.getValue().equals(ratingGroup.getSelectedToggle())) {
                    rating = entry.getKey();
                    break;
                }
            }
            
            // Crear y guardar la reseña
            Review review = new Review();
            review.setAppointmentId(cbAppointment.getValue().getId());
            review.setRating(rating);
            review.setComment(taComment.getText());
            review.setCreatedAt(LocalDateTime.now());
            
            Review savedReview = reviewDao.save(review);
            
            if (savedReview != null) {
                showAlert("Éxito", "Reseña guardada correctamente", AlertType.INFORMATION);
                clearFields();
                loadAppointments(); // Recargar las citas disponibles
            } else {
                showAlert("Error", "No se pudo guardar la reseña", AlertType.ERROR);
            }
            
        } catch (Exception ex) {
            logger.error("Error al guardar la reseña", ex);
            showAlert("Error", "Error al guardar la reseña: " + ex.getMessage(), AlertType.ERROR);
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
        cbAppointment.getSelectionModel().clearSelection();
        rb5.setSelected(true); // Seleccionar 5 estrellas por defecto
        taComment.clear();
        lblStatus.setText("");
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
            
            // Pasar el rol actual al controlador de destino
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
    
    /**
     * Clase interna para almacenar información de citas
     */
    public static class AppointmentInfo {
        private final int id;
        private final String date;
        private final String dentistName;
        
        public AppointmentInfo(int id, String date, String dentistName) {
            this.id = id;
            this.date = date;
            this.dentistName = dentistName;
        }
        
        public int getId() {
            return id;
        }
        
        public String getDate() {
            return date;
        }
        
        public String getDentistName() {
            return dentistName;
        }
    }
}