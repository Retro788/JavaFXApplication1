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
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rohit
 */
public class FXMLAddTreatmentController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(FXMLAddTreatmentController.class);
    
    private String currentRole;
    
    public void setCurrentRole(String role) {
        this.currentRole = role;
        logger.info("Rol establecido en AddTreatmentController: {}", role);
        if (!List.of("Practicante", "Operator", "Docente").contains(role)) {
            // Deshabilitar el botón de guardar si el rol no tiene permisos
            if (saveButton != null) {
                saveButton.setDisable(true);
                showAlert(AlertType.WARNING, "Acceso restringido", 
                          "No tiene permisos para añadir tratamientos con el rol: " + role);
            }
        }
    }

    @FXML MenuBar myMenuBar;
    
    @FXML private TextField treatmentid;
    @FXML private TextField treatmentname;
    @FXML private TextField treatmentamount;
    @FXML private Button saveButton; // Botón para guardar tratamiento
    
    
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    public FXMLAddTreatmentController(){
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
    private void saveTreatment(ActionEvent event){
        // Validar que el usuario tenga permisos para guardar
        if (currentRole != null && !List.of("Practicante", "Operator", "Docente").contains(currentRole)) {
            showAlert(AlertType.WARNING, "Acceso restringido", 
                      "No tiene permisos para añadir tratamientos con el rol: " + currentRole);
            return;
        }
        
        // Validar campos obligatorios
        if (treatmentid.getText().isEmpty() || treatmentname.getText().isEmpty() || 
            treatmentamount.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Datos incompletos", 
                      "Por favor complete todos los campos obligatorios");
            return;
        }
        
        // Validar que el ID sea numérico
        if (!isNumeric(treatmentid.getText())) {
            showAlert(AlertType.WARNING, "Error de formato", 
                      "El ID debe ser un valor numérico");
            return;
        }
        
        // Validar que el monto sea decimal
        if (!isDecimal(treatmentamount.getText())) {
            showAlert(AlertType.WARNING, "Error de formato", 
                      "El monto debe ser un valor decimal válido");
            return;
        }

        try{
            preparedStatement = con.prepareStatement("INSERT INTO treatment values(?,?,?)");
            preparedStatement.setInt(1,Integer.parseInt(treatmentid.getText().toString()));
            preparedStatement.setString(2,treatmentname.getText().toString());
            preparedStatement.setString(3,treatmentamount.getText().toString());
            preparedStatement.executeUpdate();
            
            preparedStatement = con.prepareStatement("SELECT * FROM treatment WHERE treatmentid = ?");
            preparedStatement.setInt(1,Integer.parseInt(treatmentid.getText().toString()));
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Si ya se estableció el rol antes de la inicialización, aplicar restricciones
        if (currentRole != null) {
            setCurrentRole(currentRole);
        }
    }
    
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
    
    private boolean isDecimal(String str) {
        return str != null && str.matches("\\d+(\\.\\d+)?");
    }
    
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
