package javafxapplication1;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase principal de la aplicación
 */
public class JavaFXApplication1 extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(JavaFXApplication1.class);
    
    @Override
    public void start(Stage stage) {
        try {
            // Cargar vista de login
            Parent root = FXMLLoader.load(getClass().getResource("/javafxapplication1/view/FXMLDocument.fxml"));
            
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.setTitle("DentalCare - Login");
            stage.show();
            
            logger.info("Aplicación iniciada correctamente");
            
        } catch (IOException e) {
            logger.error("Error al iniciar la aplicación", e);
        }
    }

    /**
     * Método principal
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        launch(args);
    }
}
