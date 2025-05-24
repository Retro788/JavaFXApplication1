package javafxapplication1.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafxapplication1.dao.ReviewDAO;
import javafxapplication1.dao.impl.ReviewDAOImpl;
import javafxapplication1.model.Review;
import javafxapplication1.util.ConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para la vista de lista de reseñas
 */
public class ReviewListController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewListController.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    @FXML private TableView<Review> tableReviews;
    @FXML private TableColumn<Review, Integer> colId;
    @FXML private TableColumn<Review, Integer> colAppointmentId;
    @FXML private TableColumn<Review, Integer> colRating;
    @FXML private TableColumn<Review, String> colComment;
    @FXML private TableColumn<Review, String> colCreatedAt;
    @FXML private TableColumn<Review, Void> colEdit;
    @FXML private TableColumn<Review, Void> colDelete;
    @FXML private ComboBox<String> cbRatingFilter;
    @FXML private Button btnFilter;
    @FXML private Button btnAdd;
    @FXML private Button btnRefresh;
    @FXML private Button btnBack;
    @FXML private Label lblStatus;
    
    private ReviewDAO reviewDao = new ReviewDAOImpl();
    private ObservableList<Review> reviewList = FXCollections.observableArrayList();
    private String currentRole;
    private ObservableList<String> ratingFilterOptions = FXCollections.observableArrayList(
            "Todas", "5 estrellas", "4+ estrellas", "3+ estrellas", "2+ estrellas", "1+ estrellas");
    
    /**
     * Inicializa el controlador
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        setupRatingFilter();
        loadReviews();
    }
    
    /**
     * Establece el rol actual del usuario y configura la interfaz según el rol
     * @param role El rol del usuario
     */
    public void setCurrentRole(String role) {
        this.currentRole = role;
        
        // Solo Paciente puede añadir reseñas
        if (!"Paciente".equals(role)) {
            btnAdd.setDisable(true);
        }
        
        // Recargar las reseñas según el rol
        loadReviewsByRole();
    }
    
    /**
     * Configura las opciones del filtro de calificación
     */
    private void setupRatingFilter() {
        cbRatingFilter.setItems(ratingFilterOptions);
        cbRatingFilter.getSelectionModel().selectFirst();
    }
    
    /**
     * Configura las columnas de la tabla
     */
    private void setupTableColumns() {
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        
        colAppointmentId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAppointmentId()).asObject());
        
        colRating.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRating()).asObject());
        colRating.setCellFactory(column -> new TableCell<Review, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " ★");
                }
            }
        });
        
        colComment.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getComment()));
        
        colCreatedAt.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().getCreatedAt();
            return new SimpleStringProperty(date != null ? date.format(DATE_FORMATTER) : "");
        });
        
        // Columna de edición con botón
        colEdit.setCellFactory(column -> new TableCell<Review, Void>() {
            private final Button editButton = new Button("Editar");
            
            {
                editButton.setOnAction(event -> {
                    Review review = getTableView().getItems().get(getIndex());
                    openUpdateView(review);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Deshabilitar el botón si el usuario no tiene permisos
                    if (currentRole != null) {
                        // Solo el paciente que creó la reseña puede editarla
                        Review review = getTableView().getItems().get(getIndex());
                        boolean canEdit = canUserEditReview(review);
                        editButton.setDisable(!canEdit);
                    }
                    setGraphic(editButton);
                }
            }
        });
        
        // Columna de eliminación con botón
        colDelete.setCellFactory(column -> new TableCell<Review, Void>() {
            private final Button deleteButton = new Button("Eliminar");
            
            {
                deleteButton.setOnAction(event -> {
                    Review review = getTableView().getItems().get(getIndex());
                    confirmDelete(review);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Deshabilitar el botón si el usuario no tiene permisos
                    if (currentRole != null) {
                        // Solo el paciente que creó la reseña o un Docente/Operator puede eliminarla
                        Review review = getTableView().getItems().get(getIndex());
                        boolean canDelete = canUserDeleteReview(review);
                        deleteButton.setDisable(!canDelete);
                    }
                    setGraphic(deleteButton);
                }
            }
        });
    }
    
    /**
     * Verifica si el usuario actual puede editar una reseña
     * @param review La reseña a verificar
     * @return true si puede editar, false en caso contrario
     */
    private boolean canUserEditReview(Review review) {
        if ("Paciente".equals(currentRole)) {
            // Verificar si el paciente es el creador de la reseña
            return isReviewCreatedByCurrentUser(review);
        }
        return false;
    }
    
    /**
     * Verifica si el usuario actual puede eliminar una reseña
     * @param review La reseña a verificar
     * @return true si puede eliminar, false en caso contrario
     */
    private boolean canUserDeleteReview(Review review) {
        if ("Paciente".equals(currentRole)) {
            // Verificar si el paciente es el creador de la reseña
            return isReviewCreatedByCurrentUser(review);
        } else if ("Docente".equals(currentRole) || "Operator".equals(currentRole)) {
            // Docentes y Operators pueden eliminar cualquier reseña
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si la reseña fue creada por el usuario actual
     * @param review La reseña a verificar
     * @return true si fue creada por el usuario actual, false en caso contrario
     */
    private boolean isReviewCreatedByCurrentUser(Review review) {
        String sql = "SELECT COUNT(*) FROM appointment a " +
                     "JOIN patient p ON a.patient_id = p.id " +
                     "WHERE a.id = ? AND p.username = ?";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, review.getAppointmentId());
            ps.setString(2, currentRole);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al verificar el creador de la reseña", e);
        }
        
        return false;
    }
    
    /**
     * Carga las reseñas según el rol del usuario
     */
    private void loadReviewsByRole() {
        if ("Paciente".equals(currentRole)) {
            // Cargar solo las reseñas del paciente actual
            loadPatientReviews();
        } else {
            // Cargar todas las reseñas para otros roles
            loadReviews();
        }
    }
    
    /**
     * Carga todas las reseñas en la tabla
     */
    private void loadReviews() {
        try {
            List<Review> reviews = reviewDao.findAll();
            reviewList.clear();
            reviewList.addAll(reviews);
            tableReviews.setItems(reviewList);
            
            if (reviews.isEmpty()) {
                lblStatus.setText("No hay reseñas disponibles");
            } else {
                lblStatus.setText("");
            }
            
        } catch (Exception ex) {
            logger.error("Error al cargar las reseñas", ex);
            showAlert("Error", "Error al cargar las reseñas: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Carga las reseñas del paciente actual
     */
    private void loadPatientReviews() {
        String sql = "SELECT r.* FROM review r " +
                     "JOIN appointment a ON r.appointment_id = a.id " +
                     "JOIN patient p ON a.patient_id = p.id " +
                     "WHERE p.username = ? " +
                     "ORDER BY r.created_at DESC";
        
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, currentRole);
            
            try (ResultSet rs = ps.executeQuery()) {
                reviewList.clear();
                
                while (rs.next()) {
                    Review review = new Review();
                    review.setId(rs.getInt("id"));
                    review.setAppointmentId(rs.getInt("appointment_id"));
                    review.setRating(rs.getInt("rating"));
                    review.setComment(rs.getString("comment"));
                    review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    reviewList.add(review);
                }
                
                tableReviews.setItems(reviewList);
                
                if (reviewList.isEmpty()) {
                    lblStatus.setText("No tiene reseñas registradas");
                } else {
                    lblStatus.setText("");
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error al cargar las reseñas del paciente", e);
            showAlert("Error", "Error al cargar las reseñas: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de filtrar reseñas por calificación
     * @param event El evento de acción
     */
    @FXML
    private void onFilter(ActionEvent event) {
        String filterOption = cbRatingFilter.getValue();
        
        try {
            List<Review> reviews;
            
            if ("Todas".equals(filterOption)) {
                // Cargar todas las reseñas según el rol
                loadReviewsByRole();
                return;
            } else {
                // Extraer el valor numérico de la opción seleccionada
                int minRating = Character.getNumericValue(filterOption.charAt(0));
                reviews = reviewDao.findByMinRating(minRating);
                
                // Filtrar por paciente si es necesario
                if ("Paciente".equals(currentRole)) {
                    reviews.removeIf(review -> !isReviewCreatedByCurrentUser(review));
                }
            }
            
            reviewList.clear();
            reviewList.addAll(reviews);
            
            if (reviews.isEmpty()) {
                lblStatus.setText("No se encontraron reseñas con esa calificación");
            } else {
                lblStatus.setText("");
            }
            
        } catch (Exception ex) {
            logger.error("Error al filtrar reseñas", ex);
            showAlert("Error", "Error al filtrar reseñas: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de añadir una nueva reseña
     * @param event El evento de acción
     */
    @FXML
    private void onAdd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLAddReview.fxml"));
            Parent root = loader.load();
            
            // Pasar el rol actual al controlador de destino
            AddReviewController controller = loader.getController();
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de añadir reseña", ex);
            showAlert("Error", "Error al cargar la vista: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Maneja el evento de actualizar la lista de reseñas
     * @param event El evento de acción
     */
    @FXML
    private void onRefresh(ActionEvent event) {
        cbRatingFilter.getSelectionModel().selectFirst();
        loadReviewsByRole();
    }
    
    /**
     * Abre la vista de actualización de reseña
     * @param review La reseña a actualizar
     */
    private void openUpdateView(Review review) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxapplication1/view/FXMLUpdateReview.fxml"));
            Parent root = loader.load();
            
            // Pasar la reseña y el rol al controlador de destino
            UpdateReviewController controller = loader.getController();
            controller.setReview(review);
            if (currentRole != null) {
                controller.setCurrentRole(currentRole);
            }
            
            Stage stage = (Stage) tableReviews.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (IOException ex) {
            logger.error("Error al cargar la vista de actualizar reseña", ex);
            showAlert("Error", "Error al cargar la vista: " + ex.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Muestra un diálogo de confirmación para eliminar una reseña
     * @param review La reseña a eliminar
     */
    private void confirmDelete(Review review) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmar eliminación");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea eliminar esta reseña?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteReview(review);
            }
        });
    }
    
    /**
     * Elimina una reseña de la base de datos
     * @param review La reseña a eliminar
     */
    private void deleteReview(Review review) {
        try {
            reviewDao.delete(review.getId());
            reviewList.remove(review);
            showAlert("Éxito", "Reseña eliminada correctamente", AlertType.INFORMATION);
            
        } catch (Exception ex) {
            logger.error("Error al eliminar la reseña", ex);
            showAlert("Error", "Error al eliminar la reseña: " + ex.getMessage(), AlertType.ERROR);
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