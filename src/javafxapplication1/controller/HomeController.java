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
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @FXML private MenuBar myMenuBar;
    @FXML private Button addPatientBtn;
    @FXML private Button addDentistBtn;
    @FXML private Button addAppointmentBtn;
    @FXML private Button addTreatmentBtn;
    @FXML private Button addBillBtn;
    @FXML private Button btnReports;
    @FXML private Button btnManageUsers;
    @FXML private Button btnReviews;
    @FXML private Button btnPublications;
    
    private String currentRole;
    
    public void setCurrentRole(String role) {
        this.currentRole = role;
        configureUIByRole();
    }
    
    private void configureUIByRole() {
        // Configurar visibilidad de elementos según el rol
        // Primero ocultar todos los botones de gestión avanzada
        btnReports.setVisible(false);
        btnManageUsers.setVisible(false);
        btnReviews.setVisible(false);
        btnPublications.setVisible(false);
        
        logger.info("Configurando UI para rol: {}", currentRole);
        
        switch(currentRole) {
            case "Practicante":
                addPatientBtn.setVisible(true);
                addDentistBtn.setVisible(false);
                addAppointmentBtn.setVisible(true);
                addTreatmentBtn.setVisible(true);
                addBillBtn.setVisible(false);
                btnReviews.setVisible(true);
                break;
            case "Paciente":
                addPatientBtn.setVisible(false);
                addDentistBtn.setVisible(false);
                addAppointmentBtn.setVisible(true);
                addTreatmentBtn.setVisible(false);
                addBillBtn.setVisible(false);
                btnManageUsers.setVisible(false);
                btnReports.setVisible(false);
                break;
            case "Docente":
                addPatientBtn.setVisible(true);
                addDentistBtn.setVisible(true);
                addAppointmentBtn.setVisible(true);
                addTreatmentBtn.setVisible(true);
                addBillBtn.setVisible(false); // Docente no puede facturar
                btnReports.setVisible(true);
                btnManageUsers.setVisible(true);
                btnReviews.setVisible(true);
                btnPublications.setVisible(true);
                break;
            case "Operator":
                addPatientBtn.setVisible(true);
                addDentistBtn.setVisible(false);
                addAppointmentBtn.setVisible(true);
                addTreatmentBtn.setVisible(false);
                addBillBtn.setVisible(true);
                btnManageUsers.setVisible(true);
                btnPublications.setVisible(true);
                break;
            case "Dentist":
                addPatientBtn.setVisible(true);
                addDentistBtn.setVisible(false);
                addAppointmentBtn.setVisible(true);
                addTreatmentBtn.setVisible(true);
                addBillBtn.setVisible(true);
                btnReports.setVisible(true);
                break;
            default:
                // Configuración por defecto
                logger.warn("Rol no reconocido: {}", currentRole);
                break;
        }
    }
    
    @FXML
    private void patientlistHyperlinkAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLPatientList.fxml", event);
    }
    
    @FXML
    private void dentistlistHyperlinkAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLDentistList.fxml", event);
    }
    
    @FXML
    private void appointmentlistHyperlinkAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAppointmentList.fxml", event);
    }
    
    @FXML
    private void treatmentlistHyperlinkAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLTreatmentList.fxml", event);
    }
    
    @FXML
    private void billlistHyperlinkAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLBillList.fxml", event);
    }

    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLDocument.fxml", event);
    }
    
    @FXML
    private void addpatientButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddPatient.fxml", event);
    }

    @FXML
    private void updatepatientButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLUpdatePatient.fxml", event);
    }
    
    @FXML
    private void adddentistButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddDentist.fxml", event);
    }
    
    @FXML
    private void updatedentistButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLUpdateDentist.fxml", event);
    }
    
    @FXML
    private void addappointmentButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddAppointment.fxml", event);
    }
    
    @FXML
    private void updateappointmentButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLUpdateAppointment.fxml", event);
    }
    
    @FXML
    private void addtreatmentButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddTreatment.fxml", event);
    }
    
    @FXML
    private void updatetreatmentButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLUpdateTreatment.fxml", event);
    }
    
    @FXML
    private void addbillButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddBill.fxml", event);
    }
    
    @FXML
    private void updatebillButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLUpdateBill.fxml", event);
    }

    @FXML
    private void handleClose(ActionEvent event) {
        System.exit(0);
    }
    
    private void loadView(String fxmlPath, ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent viewParent = loader.load();
            
            // Si el controlador tiene un método setCurrentRole, establecer el rol actual
            Object controller = loader.getController();
            if (controller != null && currentRole != null) {
                try {
                    java.lang.reflect.Method setRoleMethod = controller.getClass().getMethod("setCurrentRole", String.class);
                    setRoleMethod.invoke(controller, currentRole);
                    logger.debug("Rol establecido en controlador: {}", fxmlPath);
                } catch (NoSuchMethodException e) {
                    // El controlador no tiene el método setCurrentRole, es normal
                    logger.debug("El controlador no tiene método setCurrentRole: {}", fxmlPath);
                } catch (Exception e) {
                    logger.error("Error al establecer rol en controlador: {}", fxmlPath, e);
                }
            }
            
            Scene viewScene = new Scene(viewParent);
            Stage stage = (Stage)((Node)myMenuBar).getScene().getWindow();
            stage.setScene(viewScene);
            stage.show();
        } catch (IOException e) {
            logger.error("Error al cargar la vista: {}", fxmlPath, e);
            showAlert(AlertType.ERROR, "Error", "No se pudo cargar la vista: " + fxmlPath);
            throw e;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización por defecto, se configurará cuando se establezca el rol
        logger.debug("Inicializando HomeController");
    }
    
    @FXML
    private void publicationsButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLPublicationList.fxml", event);
    }
    
    @FXML
    private void addPublicationButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddPublication.fxml", event);
    }
    
    @FXML
    private void reviewsButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLReviewList.fxml", event);
    }
    
    @FXML
    private void addReviewButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLAddReview.fxml", event);
    }
    
    @FXML
    private void reportsButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLReports.fxml", event);
    }
    
    @FXML
    private void manageUsersButtonAction(ActionEvent event) throws IOException {
        loadView("/javafxapplication1/view/FXMLUserList.fxml", event);
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}