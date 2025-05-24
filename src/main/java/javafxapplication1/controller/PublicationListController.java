package javafxapplication1.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxapplication1.dao.PublicationDAO;
import javafxapplication1.dao.impl.PublicationDAOImpl;
import javafxapplication1.model.Publication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la vista de lista de publicaciones
 */
public class PublicationListController {
    
    private static final Logger logger = LoggerFactory.getLogger(PublicationListController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @FXML private TableView<Publication> tablePublications;
    @FXML private TableColumn<Publication, Integer> colId;
    @FXML private TableColumn<Publication, String> colTitle;
    @FXML private TableColumn<Publication, Double> colFee;
    @FXML private TableColumn<Publication, String> colCreatedAt;
    @FXML private TableColumn<Publication, Void> colEdit;
    @FXML private TableColumn<Publication, Void> colDelete;
    @FXML private TextField tfSearch;
    @FXML private Button btnSearch;
    @FXML private Button btnAdd;
    @FXML private Button btnRefresh;
    @FXML private Button btnBack;
    @FXML private Label lblStatus;
    
    private PublicationDAO pubDao = new PublicationDAOImpl();
    private ObservableList<Publication> publicationList = FXCollections.observableArrayList();
    private String currentRole;
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        loadPublications();
    }
    
    /**
     * Establece el rol actual del usuario y configura la interfaz según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        
        // Solo Practicante, Operator y Docente pueden añadir publicaciones
        if (!"Practicante".equals(role) && !"Operator".equals(role) && !"Docente".equals(role)) {
            btnAdd.setDisable(true);
        }
    }
    
    /**
     * Configura las columnas de la tabla
     */
    private void setupTableColumns() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        
        colTitle.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        
        colFee.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getFee()).asObject());
        colFee.setCellFactory(column -> new TableCell<Publication, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        colCreatedAt.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getCreatedAt();
            return new SimpleStringProperty(date != null ? date.format(DATE_FORMATTER) : "");
        });
        
        // Columna de edición con botón
        colEdit.setCellFactory(column -> new TableCell<Publication, Void>() {
            private final Button editButton = new Button("Editar");
            
            {
                editButton.setOnAction(event -> {
                    Publication publication = getTableView().getItems().get(getIndex());
                    openUpdateView(publication);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Deshabilitar el botón si el usuario no tiene permisos
                    if (currentRole != null && 
                        !"Practicante".equals(currentRole) && 
                        !"Operator".equals(currentRole) && 
                        !"Docente".equals(currentRole)) {
                        editButton.setDisable(true);
                    }
                    setGraphic(editButton);
                }
            }
        });
        
        // Columna de eliminación con botón
        colDelete.setCellFactory(column -> new TableCell<Publication, Void>() {
            private final Button deleteButton = new Button("Eliminar");
            
            {
                deleteButton.setOnAction(event -> {
                    Publication publication = getTableView().getItems().get(getIndex());
                    confirmDelete(publication);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Deshabilitar el botón si el usuario no tiene permisos
                    if (currentRole != null && 
                        !"Practicante".equals(currentRole) && 
                        !"Operator".equals(currentRole) && 
                        !"Docente".equals(currentRole)) {
                        deleteButton.setDisable(true);
                    }
                    setGraphic(deleteButton);
                }
            }
        });
    }
    
    /**
     * Carga todas las publicaciones en la tabla
     */
    private void loadPublications() {
        try {
            List<Publication> publications = pubDao.findAll();
            publicationList.clear();
            publicationList.addAll(publications);
            tablePublications.setItems(publicationList);
            
            if (publications.isEmpty()) {
                lblStatus.setText("No hay publicaciones disponibles");
            } else {
                lblStatus.setText("");
            }
            
        } catch (Exception ex) {
            logger.error("Error al cargar las publicaciones", ex);
            showAlert("Error", "Error al cargar las publicaciones: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de búsqueda de publicaciones
     * @param event El evento de acción
     */
    @FXML
    private void onSearch(ActionEvent event) {
        String searchText = tfSearch.getText().trim();
        
        try {
            List<Publication> publications;
            
            if (searchText.isEmpty()) {
                publications = pubDao.findAll();
            } else {
                publications = pubDao.findByTitle(searchText);
            }
            
            publicationList.clear();
            publicationList.addAll(publications);
            
            if (publications.isEmpty()) {
                lblStatus.setText("No se encontraron publicaciones con ese título");
            } else {
                lblStatus.setText("");
            }
            
        } catch (Exception ex) {
            logger.error("Error al buscar publicaciones", ex);
            showAlert("Error", "Error al buscar publicaciones: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de añadir una nueva publicación
     * @param event El evento de acción
     */
    @FXML
    private void onAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLAddPublication.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino
            AddPublicationController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de añadir publicación", ex);
            showAlert("Error", "Error al cargar la vista: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de actualizar la lista de publicaciones
     * @param event El evento de acción
     */
    @FXML
    private void onRefresh(ActionEvent event) {
        tfSearch.clear();
        loadPublications();
    }
    
    /**
     * Abre la vista de actualización de publicación
     * @param publication La publicación a actualizar
     */
    private void openUpdateView(Publication publication) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLUpdatePublication.fxml"));
            Parent root = loader.load();
            
            // Pasar la publicación y el rol al controlador de destino
            UpdatePublicationController controller = loader.getController();
            controller.setPublication(publication);
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) tablePublications.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de actualizar publicación", ex);
            showAlert("Error", "Error al cargar la vista: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Muestra un diálogo de confirmación para eliminar una publicación
     * @param publication La publicación a eliminar
     */
    private void confirmDelete(Publication publication) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea eliminar la publicación \"" + publication.getTitle() + "\"?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deletePublication(publication);
            }
        });
    }
    
    /**
     * Elimina una publicación de la base de datos
     * @param publication La publicación a eliminar
     */
    private void deletePublication(Publication publication) {
        try {
            pubDao.delete(publication.getId());
            publicationList.remove(publication);
            showAlert("Éxito", "Publicación eliminada correctamente", AlertType.INFORMATION);
            
        } catch (Exception ex) {
            logger.error("Error al eliminar la publicación", ex);
            showAlert("Error", "Error al eliminar la publicación: " + ex.getMessage(), AlertType.ERROR);
        }
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
}