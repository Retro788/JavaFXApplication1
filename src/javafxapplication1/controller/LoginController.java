package javafxapplication1.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Locale;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapplication1.util.ConfigLoader;
import javafxapplication1.util.ConnectionUtil;
import javafxapplication1.util.HashingUtil;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleCombo;

    @FXML public void initialize(URL url, ResourceBundle rb) {
        roleCombo.getItems().addAll(ConfigLoader.get("app.roles").split(","));
    }
    
    @FXML
    private void onLogin(ActionEvent e) {
        String u = usernameField.getText(), p = passwordField.getText(), r = roleCombo.getValue();
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection c = ConnectionUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && HashingUtil.verify(p, rs.getString("password_hash"))
                    && rs.getString("role").equals(r)) {
                    // Cargar menú principal, pasando rol
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLHome.fxml"));
                    loader.setResources(ResourceBundle.getBundle("messages", Locale.getDefault()));
                    Parent root = loader.load();
                    HomeController hc = loader.getController();
                    hc.setCurrentRole(r);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                } else {
                    showAlert("Login fallido", "Usuario/contraseña/rol incorrecto", AlertType.ERROR);
                }
            }
        } catch (Exception ex) {
            // loguear con SLF4J
            LoggerFactory.getLogger(LoginController.class).error("Error en login", ex);
        }
    }
    
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void registerNewAccount(ActionEvent event) throws IOException {
        Parent register_parent = FXMLLoader.load(getClass().getResource("/javafxapplication1/view/FXMLRegister.fxml"));
        Scene register_scene = new Scene(register_parent);
        Stage app_stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        app_stage.setScene(register_scene);
        app_stage.show();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        System.exit(0);
    }
}