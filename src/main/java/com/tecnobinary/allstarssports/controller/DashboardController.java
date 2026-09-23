package main.java.com.tecnobinary.allstarssports.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.StringConverter;
import main.java.com.tecnobinary.allstarssports.model.Equipo;
import main.java.com.tecnobinary.allstarssports.model.SesionUsuario;
import main.java.com.tecnobinary.allstarssports.service.DashboardService;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class DashboardController implements Initializable {

    private final DashboardService dashboardService;
    private final SceneManager sceneManager;

    @FXML
    private Label lblBienvenida;
    @FXML
    private Label lblRol;
    @FXML
    private TableView<Equipo> tableEquipos;
    @FXML
    private TableColumn<Equipo, Integer> colIdEquipo;
    @FXML
    private TableColumn<Equipo, String> colNombreEquipo;
    @FXML
    private TableColumn<Equipo, String> colNombreLiga;
    @FXML
    private Button btnGestionarLigas;
    @FXML
    private Button btnGestionarMiembros;
    @FXML
    private Button btnCerrarSesion;
    @FXML
    private Button btnAgregarEquipo;
    @FXML
    private Button btnEditarEquipo;
    @FXML
    private Button btnEliminarEquipo;

    public DashboardController() {
        this.dashboardService = null;
        this.sceneManager = null;
    }

    public DashboardController(DashboardService dashboardService, SceneManager sceneManager) {
        this.dashboardService = dashboardService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        mostrarDatosSesion();
        cargarEquipos();
    }

    private void configurarColumnas() {
        colIdEquipo.setCellValueFactory(new PropertyValueFactory<>("idEquipo"));
        colNombreEquipo.setCellValueFactory(new PropertyValueFactory<>("nombreEquipo"));
        colNombreLiga.setCellValueFactory(new PropertyValueFactory<>("nombreLiga"));
    }

    private void mostrarDatosSesion() {
        SesionUsuario sesion = SesionUsuario.getInstance();
        if (sesion.getNombre() == null) {
            return;
        }
        lblBienvenida.setText("Bienvenido, " + sesion.getNombre() + " " + sesion.getApellido());
        lblRol.setText(sesion.getNombreRol());
    }

    private void cargarEquipos() {
        if (dashboardService == null) {
            return;
        }
        try {
            ObservableList<Equipo> equipos = dashboardService.findEquipos();
            tableEquipos.setItems(equipos);
        } catch (RuntimeException e) {
            if (sceneManager != null) {
                sceneManager.showAlert("Error al cargar equipos", "No se pudieron obtener los equipos", e.getMessage(), AlertType.ERROR);
            }
        }
    }

    @FXML
    public void handleGestionarLigas() {
    }

    @FXML
    public void handleGestionarMiembros() {
    }

    @FXML
    public void handleAgregarEquipo() {
        if (dashboardService == null) {
            return;
        }
        mostrarFormularioEquipo(null);
    }

    @FXML
    public void handleEditarEquipo() {
        if (dashboardService == null) {
            return;
        }
        Equipo seleccionado = tableEquipos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            if (sceneManager != null) {
                sceneManager.showAlert("Editar equipo", "Ningún equipo seleccionado",
                        "Selecciona un equipo de la tabla para editarlo.", AlertType.WARNING);
            }
            return;
        }
        mostrarFormularioEquipo(seleccionado);
    }

    @FXML
    public void handleEliminarEquipo() {
        if (dashboardService == null) {
            return;
        }
        Equipo seleccionado = tableEquipos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            if (sceneManager != null) {
                sceneManager.showAlert("Eliminar equipo", "Ningún equipo seleccionado",
                        "Selecciona un equipo de la tabla para eliminarlo.", AlertType.WARNING);
            }
            return;
        }

        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar equipo");
        confirmacion.setHeaderText("¿Eliminar el equipo \"" + seleccionado.getNombreEquipo() + "\"?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        confirmacion.showAndWait().ifPresent(boton -> {
            if (boton == ButtonType.OK) {
                try {
                    boolean eliminado = dashboardService.eliminarEquipo(seleccionado.getIdEquipo());
                    if (eliminado) {
                        cargarEquipos();
                    } else if (sceneManager != null) {
                        sceneManager.showAlert("Error al eliminar", "No se pudo eliminar el equipo",
                                "Intenta nuevamente.", AlertType.ERROR);
                    }
                } catch (RuntimeException e) {
                    if (sceneManager != null) {
                        sceneManager.showAlert("Error al eliminar", "No se pudo eliminar el equipo",
                                e.getMessage(), AlertType.ERROR);
                    }
                }
            }
        });
    }

    private void mostrarFormularioEquipo(Equipo equipoExistente) {
        boolean esEdicion = equipoExistente != null;

        Dialog<Equipo> dialog = new Dialog<>();
        dialog.setTitle(esEdicion ? "Editar equipo" : "Agregar equipo");
        dialog.setHeaderText(esEdicion ? "Modifica los datos del equipo" : "Ingresa los datos del nuevo equipo");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre del equipo");

        ComboBox<Pair<Integer, String>> comboLigas = new ComboBox<>();
        comboLigas.setItems(dashboardService.findLigas());
        comboLigas.setPromptText("Selecciona una liga");
        comboLigas.setConverter(new StringConverter<Pair<Integer, String>>() {
            @Override
            public String toString(Pair<Integer, String> liga) {
                return liga == null ? "" : liga.getValue();
            }

            @Override
            public Pair<Integer, String> fromString(String string) {
                return null;
            }
        });

        if (esEdicion) {
            txtNombre.setText(equipoExistente.getNombreEquipo());
            for (Pair<Integer, String> liga : comboLigas.getItems()) {
                if (liga.getKey() == equipoExistente.getIdLiga()) {
                    comboLigas.setValue(liga);
                    break;
                }
            }
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20, 24, 10, 24));
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Liga:"), 0, 1);
        grid.add(comboLigas, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Node botonGuardar = dialog.getDialogPane().lookupButton(btnGuardar);
        botonGuardar.setDisable(true);

        Runnable validar = () -> botonGuardar.setDisable(
                txtNombre.getText().isBlank() || comboLigas.getValue() == null
        );
        txtNombre.textProperty().addListener((obs, viejo, nuevo) -> validar.run());
        comboLigas.valueProperty().addListener((obs, viejo, nuevo) -> validar.run());
        validar.run();

        dialog.setResultConverter(boton -> {
            if (boton == btnGuardar) {
                Equipo equipo = esEdicion ? equipoExistente : new Equipo();
                equipo.setNombreEquipo(txtNombre.getText().trim());
                equipo.setIdLiga(comboLigas.getValue().getKey());
                return equipo;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(equipo -> {
            try {
                boolean exito = esEdicion
                        ? dashboardService.actualizarEquipo(equipo)
                        : dashboardService.crearEquipo(equipo);
                if (exito) {
                    cargarEquipos();
                } else if (sceneManager != null) {
                    sceneManager.showAlert("Error", "No se pudo guardar el equipo",
                            "Intenta nuevamente.", AlertType.ERROR);
                }
            } catch (RuntimeException e) {
                if (sceneManager != null) {
                    sceneManager.showAlert("Error", "No se pudo guardar el equipo",
                            e.getMessage(), AlertType.ERROR);
                }
            }
        });
    }

    @FXML
    public void handleCerrarSesion() {
        if (sceneManager == null) {
            return;
        }
        SesionUsuario.getInstance().cerrarSesion();
        try {
            sceneManager.showLoginView();
        } catch (Exception e) {
            sceneManager.showAlert("Error", "No se pudo cerrar sesión", e.getMessage(), AlertType.ERROR);
        }
    }

}
