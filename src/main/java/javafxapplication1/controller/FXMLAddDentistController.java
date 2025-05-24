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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rohit
 */
public class FXMLAddDentistController implements Initializable {
    
    private static final Logger logger = LoggerFactory.getLogger(FXMLAddDentistController.class);
    
    private String currentRole;
    
    public void setCurrentRole(String role) {
        this.currentRole = role;
        logger.info("Rol establecido en AddDentistController: {}", role);
        if (!List.of("Practicante", "Operator", "Docente").contains(role)) {
            // Deshabilitar el botón de guardar si el rol no tiene permisos
            if (saveButton != null) {
                saveButton.setDisable(true);
                showAlert(AlertType.WARNING, "Acceso restringido", 
                          "No tiene permisos para añadir dentistas con el rol: " + role);
            }
        }
    }

    @FXML MenuBar myMenuBar;
    
    @FXML private TextField dentistid;
    @FXML private TextField dentistname;
    @FXML private TextField dentistage;
    @FXML private ChoiceBox dentistgender;
    @FXML private TextArea dentistaddress;
    @FXML private TextField dentistphone;
    @FXML private ChoiceBox dentistbloodgroup;
    @FXML private TextField dentistspeciality;
    @FXML private Button saveButton; // Botón para guardar dentista

    
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    public FXMLAddDentistController(){
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
    private void saveDentist(ActionEvent event){
        // Validar que el usuario tenga permisos para guardar
        if (currentRole != null && !List.of("Practicante", "Operator", "Docente").contains(currentRole)) {
            showAlert(AlertType.WARNING, "Acceso restringido", 
                      "No tiene permisos para añadir dentistas con el rol: " + currentRole);
            return;
        }
        
        // Validar campos obligatorios
        if (dentistid.getText().isEmpty() || dentistname.getText().isEmpty() || 
            dentistage.getText().isEmpty() || dentistphone.getText().isEmpty()) {
            showAlert(AlertType.WARNING, "Datos incompletos", 
                      "Por favor complete todos los campos obligatorios");
            return;
        }
        
        // Validar que la edad sea numérica
        if (!isNumeric(dentistage.getText())) {
            showAlert(AlertType.WARNING, "Error de formato", 
                      "La edad debe ser un valor numérico");
            return;
        }
        
        // Validar que el teléfono sea numérico
        if (!isNumeric(dentistphone.getText())) {
            showAlert(AlertType.WARNING, "Error de formato", 
                      "El teléfono debe contener solo números");
            return;
        }

        try{
            preparedStatement = con.prepareStatement("INSERT INTO dentist values(?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1,Integer.parseInt(dentistid.getText().toString()));
            preparedStatement.setString(2,dentistname.getText().toString());
            preparedStatement.setString(3,dentistage.getText().toString());
            preparedStatement.setString(4,dentistgender.getValue().toString());
            preparedStatement.setString(5,dentistaddress.getText().toString());
            preparedStatement.setString(6,dentistphone.getText().toString());
            preparedStatement.setString(7,dentistbloodgroup.getValue().toString());
            preparedStatement.setString(8,dentistspeciality.getText().toString());
            preparedStatement.executeUpdate();
            
            preparedStatement = con.prepareStatement("SELECT * FROM dentist WHERE dentistid = ?");
            preparedStatement.setInt(1,Integer.parseInt(dentistid.getText().toString()));
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar componentes
        dentistgender.getItems().addAll("Male", "Female", "Other");
        dentistbloodgroup.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        
        // Si ya se estableció el rol antes de la inicialización, aplicar restricciones
        if (currentRole != null) {
            setCurrentRole(currentRole);
        }
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
}
