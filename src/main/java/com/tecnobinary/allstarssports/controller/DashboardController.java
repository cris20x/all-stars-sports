package main.java.com.tecnobinary.allstarssports.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
