/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

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
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author rohit
 */
public class FXMLHomeController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(FXMLHomeController.class);
    
    @FXML private MenuBar myMenuBar;
    @FXML private Button btnAddPatient;
    @FXML private Button btnUpdatePatient;
    @FXML private Button btnAddDentist;
    @FXML private Button btnUpdateDentist;
    @FXML private Button btnAddAppointment;
    @FXML private Button btnUpdateAppointment;
    @FXML private Button btnAddTreatment;
    @FXML private Button btnUpdateTreatment;
    @FXML private Button btnAddBill;
    @FXML private Button btnUpdateBill;
    @FXML private Button btnReports;
    @FXML private Button btnManageUsers;
    @FXML private Button btnPublications;
    @FXML private Button btnReviews;
    
    private String currentRole;
    
    /**
     * Establece el rol actual y configura la visibilidad de los botones según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        configureButtonsByRole();
    }
    
    /**
     * Configura la visibilidad de los botones según el rol del usuario
     */
    private void configureButtonsByRole() {
        if (currentRole == null) return;
        
        switch (currentRole) {
            case "Paciente":
                // Paciente solo puede ver sus citas y reseñas
                btnAddPatient.setVisible(false);
                btnUpdatePatient.setVisible(false);
                btnAddDentist.setVisible(false);
                btnUpdateDentist.setVisible(false);
                btnAddTreatment.setVisible(false);
                btnUpdateTreatment.setVisible(false);
                btnAddBill.setVisible(false);
                btnUpdateBill.setVisible(false);
                btnReports.setVisible(false);
                btnManageUsers.setVisible(false);
                btnPublications.setVisible(false);
                break;
                
            case "Docente":
                // Docente no puede facturar
                btnAddBill.setVisible(false);
                btnUpdateBill.setVisible(false);
                break;
                
            case "Practicante":
            case "Operator":
                // Tienen acceso completo
                break;
                
            default:
                // Para roles no reconocidos, restringir acceso
                btnAddPatient.setVisible(false);
                btnUpdatePatient.setVisible(false);
                btnAddDentist.setVisible(false);
                btnUpdateDentist.setVisible(false);
                btnAddAppointment.setVisible(false);
                btnUpdateAppointment.setVisible(false);
                btnAddTreatment.setVisible(false);
                btnUpdateTreatment.setVisible(false);
                btnAddBill.setVisible(false);
                btnUpdateBill.setVisible(false);
                btnReports.setVisible(false);
                btnManageUsers.setVisible(false);
                btnPublications.setVisible(false);
                btnReviews.setVisible(false);
                break;
        }
    }
    
    @FXML
    private void patientlistHyperlinkAction(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("FXMLPatientList.fxml"));   
        Scene login_scene = new Scene(login_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(login_scene);
        app_stage.show();
    }
    @FXML
    private void dentistlistHyperlinkAction(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("FXMLDentistList.fxml"));   
        Scene login_scene = new Scene(login_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(login_scene);
        app_stage.show();
    }
    @FXML
    private void appointmentlistHyperlinkAction(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("FXMLAppointmentList.fxml"));   
        Scene login_scene = new Scene(login_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(login_scene);
        app_stage.show();
    }
    @FXML
    private void treatmentlistHyperlinkAction(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("FXMLTreatmentList.fxml"));   
        Scene login_scene = new Scene(login_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(login_scene);
        app_stage.show();
    }
    @FXML
    private void billlistHyperlinkAction(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("FXMLBillList.fxml"));   
        Scene login_scene = new Scene(login_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(login_scene);
        app_stage.show();
    }

    @FXML
    private void logoutButtonAction(ActionEvent event) throws IOException {
        Parent login_parent = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));   
        Scene login_scene = new Scene(login_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(login_scene);
        app_stage.show();

    }
    
    @FXML
    private void addpatientButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLAddPatient.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();

    }

    @FXML
    private void updatepatientButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLUpdatePatient.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();

    }
    @FXML
    private void adddentistButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLAddDentist.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();

    }
    @FXML
    private void updatedentistButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLUpdateDentist.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }
    @FXML
    private void addappointmentButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLAddAppointment.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }
    @FXML
    private void updateappointmentButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLUpdateAppointment.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }
    @FXML
    private void addtreatmentButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLAddTreatment.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }
    @FXML
    private void updatetreatmentButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLUpdateTreatment.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }
    @FXML
    private void addbillButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLAddBill.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }
    @FXML
    private void updatebillButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLUpdateBill.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
        app_stage.show();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void publicationsButtonAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/FXMLPublicationList.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino
            PublicationListController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de publicaciones", ex);
        }
    }
    
    @FXML
    private void reviewsButtonAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/FXMLReviewList.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino
            ReviewListController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de reseñas", ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // La configuración de botones se realizará cuando se establezca el rol
    }    
    
}
