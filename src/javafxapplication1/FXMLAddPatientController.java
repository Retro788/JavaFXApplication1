/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author rohit
 */
public class FXMLAddPatientController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(FXMLAddPatientController.class);
    
    private String currentRole;
    
    public void setCurrentRole(String role) {
        this.currentRole = role;
        logger.info("Rol establecido en AddPatientController: {}", role);
        if (!List.of("Practicante", "Operator", "Docente", "Dentist").contains(role)) {
            // Deshabilitar el botón de guardar si el rol no tiene permisos
            if (saveButton != null) {
                saveButton.setDisable(true);
                showAlert(AlertType.WARNING, "Acceso restringido", 
                          "No tiene permisos para añadir pacientes con el rol: " + role);
            }
        }
    }

    @FXML private AnchorPane rootpane;
    @FXML private MenuBar myMenuBar;
    
    @FXML private TextField patientid;
    @FXML private TextField patientname;
    @FXML private TextField patientage;
    @FXML private ChoiceBox patientgender;
    @FXML private TextArea patientaddress;
    @FXML private TextField patientphone;
    @FXML private ChoiceBox patientbloodgroup;
    @FXML private TextArea patienthealthproblems;
    @FXML private Button saveButton; // Botón para guardar paciente

    
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    public FXMLAddPatientController(){
        con = ConnectionUtil.connectdb();
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
    private void updatepatientButtonAction(ActionEvent event) throws IOException {
        Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLUpdatePatient.fxml"));   
        Scene add_patient_scene = new Scene(add_patient_parent);
        Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
        app_stage.setScene(add_patient_scene);
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
    private void savePatient(ActionEvent event){
        // Validar que el usuario tenga permisos para guardar
        if (currentRole != null && !List.of("Practicante", "Operator", "Docente", "Dentist").contains(currentRole)) {
            showAlert(AlertType.WARNING, "Acceso restringido", 
                      "No tiene permisos para añadir pacientes con el rol: " + currentRole);
            return;
        }
        
        // Validar campos obligatorios
        if (patientid.getText().isEmpty() || patientname.getText().isEmpty() || 
            patientage.getText().isEmpty() || patientphone.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Datos incompletos", 
                      "Por favor complete todos los campos obligatorios");
            return;
        }
        
        // Validar que la edad sea numérica
        if (!isNumeric(patientage.getText())) {
            showAlert(AlertType.WARNING, "Error de formato", 
                      "La edad debe ser un valor numérico");
            return;
        }
        
        // Validar que el teléfono sea numérico
        if (!isNumeric(patientphone.getText())) {
            showAlert(AlertType.WARNING, "Error de formato", 
                      "El teléfono debe contener solo números");
            return;
        }
        try{
            preparedStatement = con.prepareStatement("INSERT INTO patient values(?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1,Integer.parseInt(patientid.getText().toString()));
            preparedStatement.setString(2,patientname.getText().toString());
            preparedStatement.setString(3,patientage.getText().toString());
            preparedStatement.setString(4,patientgender.getValue().toString());
            preparedStatement.setString(5,patientaddress.getText().toString());
            preparedStatement.setString(6,patientphone.getText().toString());
            preparedStatement.setString(7,patientbloodgroup.getValue().toString());
            preparedStatement.setString(8,patienthealthproblems.getText().toString());
            preparedStatement.executeUpdate();
            
            preparedStatement = con.prepareStatement("SELECT * FROM patient WHERE patientid = ?");
            preparedStatement.setInt(1,Integer.parseInt(patientid.getText().toString()));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                System.out.print("Saved");
                Parent add_patient_parent = FXMLLoader.load(getClass().getResource("FXMLHome.fxml"));   
                Scene add_patient_scene = new Scene(add_patient_parent);
                Stage app_stage = (Stage)((Node)myMenuBar).getScene().getWindow();
                app_stage.setScene(add_patient_scene);
                app_stage.show();

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @FXML
    private void handleClose(ActionEvent event) {
        System.exit(0);
    }
    
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar componentes
        patientgender.getItems().addAll("Male", "Female", "Other");
        patientbloodgroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        
        // Si ya se estableció el rol antes de la inicialización, aplicar restricciones
        if (currentRole != null) {
            setCurrentRole(currentRole);
        }
    }    
    
}
