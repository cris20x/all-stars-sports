package main.java.com.tecnobinary.allstarssports.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import javafx.util.StringConverter;
import main.java.com.tecnobinary.allstarssports.model.Equipo;
import main.java.com.tecnobinary.allstarssports.model.Liga;
import main.java.com.tecnobinary.allstarssports.model.SesionUsuario;
import main.java.com.tecnobinary.allstarssports.model.Torneo;
import main.java.com.tecnobinary.allstarssports.repository.GestionRepository;
import main.java.com.tecnobinary.allstarssports.service.DashboardService;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class DashboardController implements Initializable {

    private final DashboardService dashboardService;
    private final SceneManager sceneManager;
    private final GestionRepository gestionRepository;

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
    private TableColumn<Equipo, String> colDeporte;

    @FXML
    private TableColumn<Equipo, Void> colAccionesEquipo;

    @FXML
    private Button btnGestionarLigas;

    @FXML
    private Button btnGestionarEquipos;

    @FXML
    private Button btnCerrarSesion;

    @FXML
    private Button btnUnirseEquipo;

    @FXML
    private Button btnMisEquipos;

    @FXML
    private Button btnVistaLista;

    @FXML
    private Button btnVistaCuadricula;

    @FXML
    private FlowPane gridEquipos;

    public DashboardController() {
        this.dashboardService = null;
        this.sceneManager = null;
        this.gestionRepository = new GestionRepository();
    }

    public DashboardController(
            DashboardService dashboardService,
            SceneManager sceneManager) {

        this.dashboardService = dashboardService;
        this.sceneManager = sceneManager;
        this.gestionRepository = new GestionRepository();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        mostrarDatosSesion();
        cargarEquipos();
    }

    private void configurarColumnas() {

        colIdEquipo.setCellValueFactory(
                new PropertyValueFactory<>("idEquipo")
        );

        colNombreEquipo.setCellValueFactory(
                new PropertyValueFactory<>("nombreEquipo")
        );

        colNombreLiga.setCellValueFactory(
                new PropertyValueFactory<>("nombreLiga")
        );

        colDeporte.setCellValueFactory(
                new PropertyValueFactory<>("deporte")
        );

        colAccionesEquipo.setCellFactory(columna -> new javafx.scene.control.TableCell<Equipo, Void>() {

            private final Button editar = new Button("Editar");
            private final Button eliminar = new Button("Eliminar");
            private final HBox caja = new HBox(6, editar, eliminar);

            {
                editar.getStyleClass().add("boton-editar");
                eliminar.getStyleClass().add("boton-eliminar");
                caja.setAlignment(javafx.geometry.Pos.CENTER);

                editar.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    mostrarFormularioEquipo(equipo);
                });

                eliminar.setOnAction(event -> {
                    Equipo equipo = getTableView().getItems().get(getIndex());
                    confirmarEliminarEquipo(equipo);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                boolean esManager = SesionUsuario.getInstance().isManager();

                if (empty || !esManager) {
                    setGraphic(null);
                } else {
                    setGraphic(caja);
                }
            }
        });
    }

    private void mostrarDatosSesion() {

        SesionUsuario sesion = SesionUsuario.getInstance();

        if (sesion.getNombre() == null) {
            return;
        }

        lblBienvenida.setText(
                "Bienvenido, "
                + sesion.getNombre()
                + " "
                + sesion.getApellido()
        );

        lblRol.setText(sesion.getNombreRol());

        aplicarPermisos();
    }

    private void aplicarPermisos() {

        boolean esManager =
                SesionUsuario.getInstance().isManager();

        btnGestionarLigas.setVisible(esManager);
        btnGestionarLigas.setManaged(esManager);

        btnGestionarEquipos.setVisible(esManager);
        btnGestionarEquipos.setManaged(esManager);

        tableEquipos.refresh();
    }

    private void cargarEquipos() {

        if (dashboardService == null) {
            return;
        }

        try {

            ObservableList<Equipo> equipos =
                    dashboardService.findEquipos();

            tableEquipos.setItems(equipos);

            if (gridEquipos != null
                    && gridEquipos.isVisible()) {

                cargarCuadricula();
            }

        } catch (RuntimeException e) {

            mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los equipos",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }
    }

    @FXML
    public void handleVistaLista() {

        tableEquipos.setVisible(true);
        tableEquipos.setManaged(true);

        gridEquipos.setVisible(false);
        gridEquipos.setManaged(false);

        activarBotonVista(
                btnVistaLista,
                btnVistaCuadricula
        );
    }

    @FXML
    public void handleVistaCuadricula() {

        tableEquipos.setVisible(false);
        tableEquipos.setManaged(false);

        gridEquipos.setVisible(true);
        gridEquipos.setManaged(true);

        activarBotonVista(
                btnVistaCuadricula,
                btnVistaLista
        );

        cargarCuadricula();
    }

    private void activarBotonVista(
            Button activo,
            Button inactivo) {

        if (!activo.getStyleClass()
                .contains("boton-vista-activo")) {

            activo.getStyleClass()
                    .add("boton-vista-activo");
        }

        inactivo.getStyleClass()
                .remove("boton-vista-activo");
    }

    private void cargarCuadricula() {

        gridEquipos.getChildren().clear();

        boolean esManager =
                SesionUsuario.getInstance().isManager();

        for (Equipo equipo : tableEquipos.getItems()) {

            VBox tarjeta = new VBox(8);

            tarjeta.getStyleClass()
                    .add("tarjeta-equipo");

            if (equipo.getImagenUrl() != null
                    && !equipo.getImagenUrl().isBlank()) {

                try {

                    File archivo =
                            new File(equipo.getImagenUrl());

                    if (archivo.exists()) {

                        ImageView imagen =
                                new ImageView(
                                        new Image(
                                                archivo.toURI()
                                                        .toString(),
                                                196,
                                                88,
                                                true,
                                                true
                                        )
                                );

                        imagen.setFitWidth(196);
                        imagen.setFitHeight(88);
                        imagen.setPreserveRatio(true);
                        imagen.setSmooth(true);

                        javafx.scene.shape.Rectangle recorte =
                                new javafx.scene.shape.Rectangle(196, 88);

                        recorte.setArcWidth(12);
                        recorte.setArcHeight(12);

                        imagen.setClip(recorte);

                        tarjeta.getChildren()
                                .add(imagen);
                    }

                } catch (Exception ignored) {
                }
            }

            Label nombre =
                    new Label(
                            equipo.getNombreEquipo()
                    );

            nombre.getStyleClass()
                    .add("nombre-equipo");

            nombre.setWrapText(true);
            nombre.setMaxWidth(196);

            Label liga =
                    new Label(
                            "Liga: "
                            + equipo.getNombreLiga()
                    );

            liga.getStyleClass()
                    .add("liga-equipo");

            liga.setWrapText(true);
            liga.setMaxWidth(196);

            Label deporte =
                    new Label(
                            "Deporte: "
                            + equipo.getDeporte()
                    );

            deporte.getStyleClass()
                    .add("liga-equipo");

            deporte.setWrapText(true);
            deporte.setMaxWidth(196);

            Label id =
                    new Label(
                            "ID: "
                            + equipo.getIdEquipo()
                    );

            id.getStyleClass()
                    .add("id-equipo");

            tarjeta.getChildren().addAll(
                    nombre,
                    liga,
                    deporte,
                    id
            );

            if (esManager) {

                HBox acciones =
                        new HBox(8);

                Button editar =
                        new Button("Editar");

                editar.getStyleClass()
                        .add("boton-editar");

                Button eliminar =
                        new Button("Eliminar");

                eliminar.getStyleClass()
                        .add("boton-eliminar");

                editar.setOnAction(event ->
                        mostrarFormularioEquipo(equipo)
                );

                eliminar.setOnAction(event ->
                        confirmarEliminarEquipo(equipo)
                );

                acciones.getChildren().addAll(
                        editar,
                        eliminar
                );

                tarjeta.getChildren()
                        .add(acciones);
            }

            gridEquipos.getChildren()
                    .add(tarjeta);
        }
    }

    @FXML
    public void handleGestionarLigas() {

        if (!SesionUsuario.getInstance().isManager()) {

            mostrarAccesoDenegado();
            return;
        }

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle("Gestionar Ligas");
        dialog.setHeaderText("Administración de ligas");

        ButtonType cerrar =
                new ButtonType(
                        "Cerrar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .add(cerrar);

        VBox contenido =
                new VBox(12);

        contenido.setPadding(
                new Insets(20)
        );

        ComboBox<Liga> combo =
                new ComboBox<>();

        Button agregar =
                new Button("Agregar");

        Button editar =
                new Button("Editar");

        Button eliminar =
                new Button("Eliminar");

        Runnable cargar = () -> {

            try {

                combo.getItems().setAll(
                        gestionRepository.listarLigas()
                );

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudieron cargar las ligas",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        };

        agregar.setOnAction(event ->
                mostrarFormularioLiga(
                        null,
                        cargar
                )
        );

        editar.setOnAction(event -> {

            Liga liga =
                    combo.getValue();

            if (liga == null) {

                mostrarAlerta(
                        "Editar liga",
                        "Selecciona una liga",
                        "Debes seleccionar una liga.",
                        AlertType.WARNING
                );

                return;
            }

            mostrarFormularioLiga(
                    liga,
                    cargar
            );
        });

        eliminar.setOnAction(event -> {

            Liga liga =
                    combo.getValue();

            if (liga == null) {

                mostrarAlerta(
                        "Eliminar liga",
                        "Selecciona una liga",
                        "Debes seleccionar una liga.",
                        AlertType.WARNING
                );

                return;
            }

            if (!SesionUsuario.getInstance().isManager()) {

                mostrarAccesoDenegado();
                return;
            }

            Alert confirmacion =
                    new Alert(AlertType.CONFIRMATION);

            confirmacion.setTitle("Eliminar liga");
            confirmacion.setHeaderText(
                    "¿Eliminar " + liga.getNombreLiga() + "?"
            );
            confirmacion.setContentText(
                    "La eliminación puede fallar si la liga tiene equipos o torneos relacionados."
            );

            confirmacion.showAndWait()
                    .ifPresent(boton -> {

                        if (boton == ButtonType.OK) {

                            try {

                                if (gestionRepository.eliminarLiga(
                                        liga.getIdLiga())) {

                                    cargar.run();

                                } else {

                                    mostrarAlerta(
                                            "Error",
                                            "No se eliminó la liga",
                                            "Intenta nuevamente.",
                                            AlertType.ERROR
                                    );
                                }

                            } catch (Exception e) {

                                mostrarAlerta(
                                        "Error",
                                        "No se pudo eliminar",
                                        e.getMessage(),
                                        AlertType.ERROR
                                );
                            }
                        }
                    });
        });

        HBox acciones =
                new HBox(
                        10,
                        agregar,
                        editar,
                        eliminar
                );

        contenido.getChildren()
                .addAll(
                        new Label("Liga:"),
                        combo,
                        acciones
                );

        dialog.getDialogPane()
                .setContent(contenido);

        cargar.run();

        dialog.showAndWait();
    }

    private void mostrarFormularioLiga(
            Liga ligaExistente,
            Runnable actualizar) {

        if (!SesionUsuario.getInstance().isManager()) {

            mostrarAccesoDenegado();
            return;
        }

        boolean edicion =
                ligaExistente != null;

        Dialog<Liga> dialog =
                new Dialog<>();

        dialog.setTitle(
                edicion
                ? "Editar liga"
                : "Agregar liga"
        );

        ButtonType guardar =
                new ButtonType(
                        "Guardar",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        guardar,
                        ButtonType.CANCEL
                );

        TextField nombre =
                new TextField();

        TextField deporte =
                new TextField();

        nombre.setPromptText(
                "Nombre de la liga"
        );

        deporte.setPromptText(
                "Deporte"
        );

        if (edicion) {

            nombre.setText(
                    ligaExistente.getNombreLiga()
            );

            deporte.setText(
                    ligaExistente.getDeporte()
            );
        }

        GridPane grid =
                new GridPane();

        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(
                new Insets(20)
        );

        grid.add(
                new Label("Nombre:"),
                0,
                0
        );

        grid.add(
                nombre,
                1,
                0
        );

        grid.add(
                new Label("Deporte:"),
                0,
                1
        );

        grid.add(
                deporte,
                1,
                1
        );

        dialog.getDialogPane()
                .setContent(grid);

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {

                Liga liga =
                        edicion
                        ? ligaExistente
                        : new Liga();

                liga.setNombreLiga(
                        nombre.getText().trim()
                );

                liga.setDeporte(
                        deporte.getText().trim()
                );

                return liga;
            }

            return null;
        });

        dialog.showAndWait()
                .ifPresent(liga -> {

                    try {

                        boolean resultado =
                                edicion
                                ? gestionRepository.actualizarLiga(liga)
                                : gestionRepository.crearLiga(
                                        liga.getNombreLiga(),
                                        liga.getDeporte()
                                );

                        if (resultado) {
                            actualizar.run();
                        } else {
                            mostrarAlerta(
                                    "Error",
                                    "No se pudo guardar",
                                    "Intenta nuevamente.",
                                    AlertType.ERROR
                            );
                        }

                    } catch (Exception e) {

                        mostrarAlerta(
                                "Error",
                                "No se pudo guardar la liga",
                                e.getMessage(),
                                AlertType.ERROR
                        );
                    }
                });
    }

    @FXML
    public void handleUnirseEquipo() {

        SesionUsuario sesion =
                SesionUsuario.getInstance();

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle("Unirse a un Equipo");
        dialog.setHeaderText("Equipos disponibles");

        ButtonType cerrar =
                new ButtonType(
                        "Cerrar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .add(cerrar);

        VBox contenido =
                new VBox(12);

        contenido.setPadding(
                new Insets(20)
        );

        ComboBox<Pair<Integer, String>> equipos =
                new ComboBox<>();

        equipos.setConverter(
                new StringConverter<Pair<Integer, String>>() {

                    @Override
                    public String toString(
                            Pair<Integer, String> item) {

                        return item == null
                                ? ""
                                : item.getValue();
                    }

                    @Override
                    public Pair<Integer, String> fromString(
                            String string) {

                        return null;
                    }
                }
        );

        Button unirse =
                new Button("Unirme al equipo");

        Button torneos =
                new Button("Ver torneos disponibles");

        try {

            equipos.getItems().setAll(
                    gestionRepository.listarEquipos()
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los equipos",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }

        unirse.setOnAction(event -> {

            Pair<Integer, String> equipo =
                    equipos.getValue();

            if (equipo == null) {

                mostrarAlerta(
                        "Equipo",
                        "Selecciona un equipo",
                        "Selecciona el equipo al que deseas unirte.",
                        AlertType.WARNING
                );

                return;
            }

            try {

                if (gestionRepository.estaEnEquipo(
                        sesion.getIdUsuario(),
                        equipo.getKey())) {

                    mostrarAlerta(
                            "Equipo",
                            "Ya eres miembro",
                            "Ya perteneces a este equipo.",
                            AlertType.INFORMATION
                    );

                    return;
                }

                if (gestionRepository.unirseEquipo(
                        sesion.getIdUsuario(),
                        equipo.getKey())) {

                    mostrarAlerta(
                            "Equipo",
                            "Te has unido",
                            "Ahora perteneces a " + equipo.getValue() + ".",
                            AlertType.INFORMATION
                    );
                }

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudo unir al equipo",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        });

        torneos.setOnAction(event ->
                mostrarTorneosParticipante()
        );

        contenido.getChildren().addAll(
                new Label(
                        "Usuario: "
                        + sesion.getNombre()
                ),
                new Label("Selecciona un equipo:"),
                equipos,
                unirse,
                torneos
        );

        dialog.getDialogPane()
                .setContent(contenido);

        dialog.showAndWait();
    }

    @FXML
    public void handleMisEquipos() {

        SesionUsuario sesion =
                SesionUsuario.getInstance();

        boolean esManager =
                SesionUsuario.getInstance().isManager();

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle("Mis Equipos");
        dialog.setHeaderText(
                "Equipos y torneos a los que perteneces"
        );

        ButtonType cerrar =
                new ButtonType(
                        "Cerrar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .add(cerrar);

        Label tituloEquipos = new Label("Equipos");
        tituloEquipos.getStyleClass().add("seccion-titulo");

        VBox listaEquipos = new VBox(8);

        javafx.scene.control.ScrollPane scrollEquipos =
                new javafx.scene.control.ScrollPane(listaEquipos);

        scrollEquipos.setFitToWidth(true);
        scrollEquipos.setPrefViewportHeight(200);
        scrollEquipos.getStyleClass().add("mis-equipos-scroll");

        Label tituloTorneos = new Label("Torneos");
        tituloTorneos.getStyleClass().add("seccion-titulo");

        VBox listaTorneos = new VBox(8);

        javafx.scene.control.ScrollPane scrollTorneos =
                new javafx.scene.control.ScrollPane(listaTorneos);

        scrollTorneos.setFitToWidth(true);
        scrollTorneos.setPrefViewportHeight(200);
        scrollTorneos.getStyleClass().add("mis-equipos-scroll");

        VBox contenido = new VBox(
                10,
                tituloEquipos,
                scrollEquipos,
                tituloTorneos,
                scrollTorneos
        );

        contenido.setPadding(new Insets(20));
        contenido.setPrefWidth(420);

        Runnable[] recargarRef = new Runnable[1];
        Runnable[] recargarTorneosRef = new Runnable[1];

        if (esManager) {

            Button agregarTorneo =
                    new Button("+ Agregar Torneo");

            agregarTorneo.getStyleClass()
                    .add("boton-participante");

            agregarTorneo.setOnAction(event ->
                    mostrarFormularioTorneo(
                            null,
                            () -> recargarTorneosRef[0].run()
                    )
            );

            contenido.getChildren().add(0, agregarTorneo);
        }

        recargarRef[0] = () -> {

            listaEquipos.getChildren().clear();

            try {

                List<Pair<Integer, String>> misEquipos =
                        gestionRepository.listarMisEquipos(
                                sesion.getIdUsuario()
                        );

                if (misEquipos.isEmpty()) {

                    listaEquipos.getChildren().add(
                            new Label(
                                    "Todavía no perteneces a ningún equipo."
                            )
                    );

                } else {

                    for (Pair<Integer, String> equipo : misEquipos) {

                        Label item =
                                new Label(equipo.getValue());

                        item.getStyleClass()
                                .add("mi-equipo-item");

                        item.setWrapText(true);
                        HBox.setHgrow(item, javafx.scene.layout.Priority.ALWAYS);

                        Button salir = new Button("Salir");
                        salir.getStyleClass().add("boton-eliminar");

                        salir.setOnAction(event -> {

                            Alert confirmacion =
                                    new Alert(AlertType.CONFIRMATION);

                            confirmacion.setTitle("Salir del equipo");
                            confirmacion.setHeaderText(
                                    "¿Salir de este equipo?"
                            );

                            confirmacion.showAndWait()
                                    .ifPresent(boton -> {

                                        if (boton == ButtonType.OK) {

                                            try {

                                                gestionRepository.salirEquipo(
                                                        sesion.getIdUsuario(),
                                                        equipo.getKey()
                                                );

                                                recargarRef[0].run();

                                            } catch (Exception e) {

                                                mostrarAlerta(
                                                        "Error",
                                                        "No se pudo salir del equipo",
                                                        e.getMessage(),
                                                        AlertType.ERROR
                                                );
                                            }
                                        }
                                    });
                        });

                        HBox fila = new HBox(10, item, salir);
                        fila.getStyleClass().add("mi-equipo-fila");
                        fila.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        listaEquipos.getChildren().add(fila);
                    }
                }

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudieron cargar tus equipos",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        };

        recargarTorneosRef[0] = () -> {

            listaTorneos.getChildren().clear();

            try {

                List<Torneo> misTorneos =
                        gestionRepository.listarTorneosInscritos(
                                sesion.getIdUsuario()
                        );

                if (misTorneos.isEmpty()) {

                    listaTorneos.getChildren().add(
                            new Label(
                                    "Todavía no estás inscrito en ningún torneo."
                            )
                    );

                } else {

                    for (Torneo torneo : misTorneos) {

                        VBox info = new VBox(2);

                        Label nombre =
                                new Label(torneo.getNombreTorneo());

                        nombre.getStyleClass()
                                .add("mi-equipo-item");

                        nombre.setWrapText(true);

                        Label detalle =
                                new Label(
                                        "Liga: " + torneo.getNombreLiga()
                                        + "  •  Estado: " + torneo.getEstado()
                                        + "\nInicio: " + torneo.getFechaInicio()
                                        + "  •  Fin: " + torneo.getFechaFin()
                                );

                        detalle.getStyleClass().add("liga-equipo");
                        detalle.setWrapText(true);

                        info.getChildren().addAll(nombre, detalle);
                        HBox.setHgrow(info, javafx.scene.layout.Priority.ALWAYS);

                        HBox botones = new HBox(6);
                        botones.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

                        if (esManager) {

                            Button editar = new Button("Editar");
                            editar.getStyleClass().add("boton-editar");

                            editar.setOnAction(event ->
                                    mostrarFormularioTorneo(
                                            torneo,
                                            () -> recargarTorneosRef[0].run()
                                    )
                            );

                            botones.getChildren().add(editar);
                        }

                        Button salir = new Button("Salir");
                        salir.getStyleClass().add("boton-eliminar");

                        salir.setOnAction(event -> {

                            Alert confirmacion =
                                    new Alert(AlertType.CONFIRMATION);

                            confirmacion.setTitle("Cancelar inscripción");
                            confirmacion.setHeaderText(
                                    "¿Salir de este torneo?"
                            );

                            confirmacion.showAndWait()
                                    .ifPresent(boton -> {

                                        if (boton == ButtonType.OK) {

                                            try {

                                                gestionRepository.cancelarInscripcion(
                                                        sesion.getIdUsuario(),
                                                        torneo.getIdTorneo()
                                                );

                                                recargarTorneosRef[0].run();

                                            } catch (Exception e) {

                                                mostrarAlerta(
                                                        "Error",
                                                        "No se pudo salir del torneo",
                                                        e.getMessage(),
                                                        AlertType.ERROR
                                                );
                                            }
                                        }
                                    });
                        });

                        botones.getChildren().add(salir);

                        HBox fila = new HBox(10, info, botones);
                        fila.getStyleClass().add("mi-equipo-fila");
                        fila.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                        listaTorneos.getChildren().add(fila);
                    }
                }

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudieron cargar tus torneos",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        };

        recargarRef[0].run();
        recargarTorneosRef[0].run();

        dialog.getDialogPane()
                .setContent(contenido);

        dialog.showAndWait();
    }

    private void mostrarFormularioTorneo(
            Torneo torneoExistente,
            Runnable actualizar) {

        if (!SesionUsuario.getInstance().isManager()) {

            mostrarAccesoDenegado();
            return;
        }

        boolean edicion = torneoExistente != null;

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle(
                edicion ? "Editar Torneo" : "Agregar Torneo"
        );

        dialog.setHeaderText(
                edicion
                ? "Editar información del torneo"
                : "Crear un nuevo torneo"
        );

        ButtonType guardar =
                new ButtonType(
                        "Guardar",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        guardar,
                        ButtonType.CANCEL
                );

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre del torneo");

        ComboBox<Liga> ligas = new ComboBox<>();

        ligas.setPromptText("Selecciona una liga");

        ligas.setConverter(
                new StringConverter<Liga>() {

                    @Override
                    public String toString(Liga liga) {
                        return liga == null
                                ? ""
                                : liga.getNombreLiga()
                                + " (" + liga.getDeporte() + ")";
                    }

                    @Override
                    public Liga fromString(String string) {
                        return null;
                    }
                }
        );

        try {

            ligas.getItems().setAll(
                    gestionRepository.listarLigas()
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    "No se pudieron cargar las ligas",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }

        DatePicker fechaInicio = new DatePicker();
        DatePicker fechaFin = new DatePicker();

        ComboBox<String> estado = new ComboBox<>();

        estado.getItems().addAll(
                "activo",
                "finalizado",
                "cancelado"
        );

        estado.setValue("activo");

        if (edicion) {

            nombre.setText(
                    torneoExistente.getNombreTorneo()
            );

            for (Liga liga : ligas.getItems()) {

                if (liga.getIdLiga()
                        == torneoExistente.getIdLiga()) {

                    ligas.setValue(liga);
                    break;
                }
            }

            fechaInicio.setValue(
                    torneoExistente.getFechaInicio()
            );

            fechaFin.setValue(
                    torneoExistente.getFechaFin()
            );

            estado.setValue(
                    torneoExistente.getEstado()
            );
        }

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(12);
        grid.setPadding(new Insets(20));

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombre, 1, 0);

        grid.add(new Label("Liga:"), 0, 1);
        grid.add(ligas, 1, 1);

        grid.add(new Label("Fecha inicio:"), 0, 2);
        grid.add(fechaInicio, 1, 2);

        grid.add(new Label("Fecha fin:"), 0, 3);
        grid.add(fechaFin, 1, 3);

        grid.add(new Label("Estado:"), 0, 4);
        grid.add(estado, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Node botonGuardar =
                dialog.getDialogPane().lookupButton(guardar);

        botonGuardar.setDisable(!edicion);

        Runnable validar = () ->
                botonGuardar.setDisable(
                        nombre.getText().isBlank()
                        || ligas.getValue() == null
                        || fechaInicio.getValue() == null
                        || fechaFin.getValue() == null
                );

        nombre.textProperty().addListener(
                (obs, viejo, nuevo) -> validar.run()
        );

        ligas.valueProperty().addListener(
                (obs, viejo, nuevo) -> validar.run()
        );

        fechaInicio.valueProperty().addListener(
                (obs, viejo, nuevo) -> validar.run()
        );

        fechaFin.valueProperty().addListener(
                (obs, viejo, nuevo) -> validar.run()
        );

        validar.run();

        dialog.setResultConverter(boton -> boton);

        dialog.showAndWait().ifPresent(boton -> {

            if (boton != guardar) {
                return;
            }

            try {

                boolean resultado;

                if (edicion) {

                    torneoExistente.setNombreTorneo(
                            nombre.getText().trim()
                    );

                    torneoExistente.setIdLiga(
                            ligas.getValue().getIdLiga()
                    );

                    torneoExistente.setFechaInicio(
                            fechaInicio.getValue()
                    );

                    torneoExistente.setFechaFin(
                            fechaFin.getValue()
                    );

                    torneoExistente.setEstado(
                            estado.getValue()
                    );

                    resultado = gestionRepository.actualizarTorneo(
                            torneoExistente
                    );

                } else {

                    resultado = gestionRepository.crearTorneo(
                            ligas.getValue().getIdLiga(),
                            nombre.getText().trim(),
                            fechaInicio.getValue(),
                            fechaFin.getValue(),
                            estado.getValue()
                    );
                }

                if (resultado) {

                    mostrarAlerta(
                            "Torneo",
                            edicion
                            ? "Torneo actualizado"
                            : "Torneo creado",
                            edicion
                            ? "El torneo se actualizó correctamente."
                            : "El torneo se creó correctamente.",
                            AlertType.INFORMATION
                    );

                    if (actualizar != null) {
                        actualizar.run();
                    }

                } else {

                    mostrarAlerta(
                            "Error",
                            "No se pudo guardar",
                            "Intenta nuevamente.",
                            AlertType.ERROR
                    );
                }

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudo guardar el torneo",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        });
    }

    private void mostrarTorneosParticipante() {

        SesionUsuario sesion =
                SesionUsuario.getInstance();

        Dialog<Void> dialog =
                new Dialog<>();

        dialog.setTitle(
                "Torneos disponibles"
        );

        ButtonType cerrar =
                new ButtonType(
                        "Cerrar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .add(cerrar);

        VBox contenido =
                new VBox(12);

        contenido.setPadding(
                new Insets(20)
        );

        ComboBox<Pair<Integer, String>> combo =
                new ComboBox<>();

        combo.setConverter(
                new StringConverter<Pair<Integer, String>>() {

                    @Override
                    public String toString(
                            Pair<Integer, String> item) {

                        return item == null
                                ? ""
                                : item.getValue();
                    }

                    @Override
                    public Pair<Integer, String> fromString(
                            String string) {

                        return null;
                    }
                }
        );

        Button inscribirse =
                new Button(
                        "Inscribirme"
                );

        Button cancelar =
                new Button(
                        "Cancelar inscripción"
                );

        try {

            combo.getItems().setAll(
                    gestionRepository
                            .listarTorneosDisponibles()
            );

        } catch (Exception e) {

            mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los torneos",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }

        inscribirse.setOnAction(event -> {

            Pair<Integer, String> torneo =
                    combo.getValue();

            if (torneo == null) {

                mostrarAlerta(
                        "Torneo",
                        "Selecciona un torneo",
                        "Selecciona un torneo.",
                        AlertType.WARNING
                );

                return;
            }

            try {

                if (gestionRepository.estaInscritoTorneo(
                        sesion.getIdUsuario(),
                        torneo.getKey())) {

                    mostrarAlerta(
                            "Torneo",
                            "Ya estás inscrito",
                            "Ya estás inscrito en este torneo.",
                            AlertType.INFORMATION
                    );

                    return;
                }

                if (gestionRepository.inscribirTorneo(
                        sesion.getIdUsuario(),
                        torneo.getKey())) {

                    mostrarAlerta(
                            "Torneo",
                            "Inscripción realizada",
                            "Te has inscrito en "
                            + torneo.getValue() + ".",
                            AlertType.INFORMATION
                    );
                }

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudo realizar la inscripción",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        });

        cancelar.setOnAction(event -> {

            Pair<Integer, String> torneo =
                    combo.getValue();

            if (torneo == null) {

                mostrarAlerta(
                        "Torneo",
                        "Selecciona un torneo",
                        "Selecciona un torneo.",
                        AlertType.WARNING
                );

                return;
            }

            try {

                if (gestionRepository.cancelarInscripcion(
                        sesion.getIdUsuario(),
                        torneo.getKey())) {

                    mostrarAlerta(
                            "Torneo",
                            "Inscripción cancelada",
                            "Se canceló tu inscripción.",
                            AlertType.INFORMATION
                    );
                }

            } catch (Exception e) {

                mostrarAlerta(
                        "Error",
                        "No se pudo cancelar",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        });

        contenido.getChildren().addAll(
                new Label("Torneo:"),
                combo,
                new HBox(
                        10,
                        inscribirse,
                        cancelar
                )
        );

        dialog.getDialogPane()
                .setContent(contenido);

        dialog.showAndWait();
    }

    @FXML
    public void handleGestionarEquipos() {

        if (dashboardService == null) {
            return;
        }

        if (!SesionUsuario.getInstance().isManager()) {

            mostrarAccesoDenegado();
            return;
        }

        Dialog<Void> dialog = new Dialog<>();

        dialog.setTitle("Gestionar Equipos");
        dialog.setHeaderText("Administración de equipos");

        ButtonType cerrar =
                new ButtonType(
                        "Cerrar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .add(cerrar);

        VBox contenido = new VBox(12);

        contenido.setPadding(new Insets(20));
        contenido.setPrefWidth(380);

        ComboBox<Equipo> combo = new ComboBox<>();

        combo.setPromptText("Selecciona un equipo");

        combo.setConverter(
                new StringConverter<Equipo>() {

                    @Override
                    public String toString(Equipo item) {
                        return item == null
                                ? ""
                                : item.getNombreEquipo()
                                + " (" + item.getNombreLiga() + ")";
                    }

                    @Override
                    public Equipo fromString(String string) {
                        return null;
                    }
                }
        );

        Button agregar = new Button("Agregar equipo");
        Button editar = new Button("Editar equipo");
        Button eliminar = new Button("Eliminar equipo");

        Runnable recargar = () -> {

            Equipo actual = combo.getValue();

            combo.getItems().setAll(
                    dashboardService.findEquipos()
            );

            if (actual != null) {

                for (Equipo equipo : combo.getItems()) {

                    if (equipo.getIdEquipo()
                            == actual.getIdEquipo()) {

                        combo.setValue(equipo);
                        break;
                    }
                }
            }
        };

        agregar.setOnAction(event ->
                mostrarFormularioEquipo(null)
        );

        editar.setOnAction(event -> {

            Equipo seleccionado = combo.getValue();

            if (seleccionado == null) {

                mostrarAlerta(
                        "Editar equipo",
                        "Ningún equipo seleccionado",
                        "Selecciona un equipo.",
                        AlertType.WARNING
                );

                return;
            }

            mostrarFormularioEquipo(seleccionado);
        });

        eliminar.setOnAction(event -> {

            Equipo seleccionado = combo.getValue();

            if (seleccionado == null) {

                mostrarAlerta(
                        "Eliminar equipo",
                        "Ningún equipo seleccionado",
                        "Selecciona un equipo.",
                        AlertType.WARNING
                );

                return;
            }

            confirmarEliminarEquipo(seleccionado);
            recargar.run();
        });

        try {

            recargar.run();

        } catch (RuntimeException e) {

            mostrarAlerta(
                    "Error",
                    "No se pudieron cargar los equipos",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }

        contenido.getChildren().addAll(
                new Label("Equipo:"),
                combo,
                new HBox(10, agregar, editar, eliminar)
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.showAndWait();

        cargarEquipos();
    }

    private void confirmarEliminarEquipo(Equipo seleccionado) {

        if (dashboardService == null) {
            return;
        }

        if (!SesionUsuario.getInstance().isManager()) {

            mostrarAccesoDenegado();
            return;
        }

        Alert confirmacion =
                new Alert(
                        AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Eliminar equipo"
        );

        confirmacion.setHeaderText(
                "¿Eliminar "
                + seleccionado.getNombreEquipo()
                + "?"
        );

        confirmacion.setContentText(
                "También se eliminarán sus miembros registrados."
        );

        confirmacion.showAndWait()
                .ifPresent(boton -> {

                    if (boton == ButtonType.OK) {

                        try {

                            if (dashboardService
                                    .eliminarEquipo(
                                            seleccionado
                                                    .getIdEquipo()
                                    )) {

                                cargarEquipos();

                            } else {

                                mostrarAlerta(
                                        "Error",
                                        "No se pudo eliminar",
                                        "Intenta nuevamente.",
                                        AlertType.ERROR
                                );
                            }

                        } catch (Exception e) {

                            mostrarAlerta(
                                    "Error",
                                    "No se pudo eliminar",
                                    e.getMessage(),
                                    AlertType.ERROR
                            );
                        }
                    }
                });
    }

    private void mostrarFormularioEquipo(
            Equipo equipoExistente) {

        boolean edicion =
                equipoExistente != null;

        Dialog<Equipo> dialog =
                new Dialog<>();

        dialog.setTitle(
                edicion
                ? "Editar equipo"
                : "Agregar equipo"
        );

        ButtonType guardar =
                new ButtonType(
                        "Guardar",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        guardar,
                        ButtonType.CANCEL
                );

        TextField nombre =
                new TextField();

        nombre.setPromptText(
                "Nombre del equipo"
        );

        ComboBox<Pair<Integer, String>> ligas =
                new ComboBox<>();

        ligas.setItems(
                dashboardService.findLigas()
        );

        ligas.setPromptText(
                "Selecciona una liga"
        );

        ligas.setConverter(
                new StringConverter<Pair<Integer, String>>() {

                    @Override
                    public String toString(
                            Pair<Integer, String> liga) {

                        return liga == null
                                ? ""
                                : liga.getValue();
                    }

                    @Override
                    public Pair<Integer, String> fromString(
                            String string) {

                        return null;
                    }
                }
        );

        Button seleccionarImagen =
                new Button(
                        "Seleccionar imagen"
                );

        Label archivoImagen =
                new Label(
                        "Ninguna imagen seleccionada"
                );

        final String[] rutaImagen = {
            edicion
            ? equipoExistente.getImagenUrl()
            : null
        };

        seleccionarImagen.setOnAction(event -> {

            FileChooser chooser =
                    new FileChooser();

            chooser.setTitle(
                    "Seleccionar imagen"
            );

            chooser.getExtensionFilters()
                    .addAll(
                            new FileChooser.ExtensionFilter(
                                    "Imágenes",
                                    "*.png",
                                    "*.jpg",
                                    "*.jpeg",
                                    "*.gif",
                                    "*.bmp",
                                    "*.webp"
                            )
                    );

            File archivo =
                    chooser.showOpenDialog(
                            seleccionarImagen
                                    .getScene()
                                    .getWindow()
                    );

            if (archivo != null) {

                try {

                    Path carpeta =
                            Path.of(
                                    System.getProperty(
                                            "user.home"
                                    ),
                                    "AllStarsSports",
                                    "imagenes"
                            );

                    Files.createDirectories(
                            carpeta
                    );

                    String nombreArchivo =
                            System.currentTimeMillis()
                            + "_"
                            + archivo.getName();

                    Path destino =
                            carpeta.resolve(
                                    nombreArchivo
                            );

                    Files.copy(
                            archivo.toPath(),
                            destino,
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    rutaImagen[0] =
                            destino.toAbsolutePath()
                                    .toString();

                    archivoImagen.setText(
                            archivo.getName()
                    );

                } catch (IOException e) {

                    mostrarAlerta(
                            "Error",
                            "No se pudo guardar la imagen",
                            e.getMessage(),
                            AlertType.ERROR
                    );
                }
            }
        });

        if (edicion) {

            nombre.setText(
                    equipoExistente
                            .getNombreEquipo()
            );

            for (
                    Pair<Integer, String> liga :
                    ligas.getItems()) {

                if (liga.getKey()
                        == equipoExistente.getIdLiga()) {

                    ligas.setValue(liga);
                    break;
                }
            }

            if (rutaImagen[0] != null
                    && !rutaImagen[0].isBlank()) {

                archivoImagen.setText(
                        new File(
                                rutaImagen[0]
                        ).getName()
                );
            }
        }

        GridPane grid =
                new GridPane();

        grid.setHgap(10);
        grid.setVgap(12);

        grid.setPadding(
                new Insets(20)
        );

        grid.add(
                new Label("Nombre:"),
                0,
                0
        );

        grid.add(
                nombre,
                1,
                0
        );

        grid.add(
                new Label("Liga:"),
                0,
                1
        );

        grid.add(
                ligas,
                1,
                1
        );

        grid.add(
                new Label("Imagen:"),
                0,
                2
        );

        grid.add(
                new HBox(
                        10,
                        seleccionarImagen,
                        archivoImagen
                ),
                1,
                2
        );

        dialog.getDialogPane()
                .setContent(grid);

        Node botonGuardar =
                dialog.getDialogPane()
                        .lookupButton(guardar);

        botonGuardar.setDisable(true);

        Runnable validar = () ->
                botonGuardar.setDisable(
                        nombre.getText().isBlank()
                        || ligas.getValue() == null
                );

        nombre.textProperty()
                .addListener(
                        (obs, viejo, nuevo) ->
                                validar.run()
                );

        ligas.valueProperty()
                .addListener(
                        (obs, viejo, nuevo) ->
                                validar.run()
                );

        validar.run();

        dialog.setResultConverter(boton -> {

            if (boton == guardar) {

                Equipo equipo =
                        edicion
                        ? equipoExistente
                        : new Equipo();

                equipo.setNombreEquipo(
                        nombre.getText().trim()
                );

                equipo.setIdLiga(
                        ligas.getValue().getKey()
                );

                equipo.setImagenUrl(
                        rutaImagen[0]
                );

                return equipo;
            }

            return null;
        });

        dialog.showAndWait()
                .ifPresent(equipo -> {

                    try {

                        boolean resultado =
                                edicion
                                ? dashboardService
                                        .actualizarEquipo(equipo)
                                : dashboardService
                                        .crearEquipo(equipo);

                        if (resultado) {

                            cargarEquipos();

                        } else {

                            mostrarAlerta(
                                    "Error",
                                    "No se pudo guardar",
                                    "Intenta nuevamente.",
                                    AlertType.ERROR
                            );
                        }

                    } catch (Exception e) {

                        mostrarAlerta(
                                "Error",
                                "No se pudo guardar el equipo",
                                e.getMessage(),
                                AlertType.ERROR
                        );
                    }
                });
    }

    private void mostrarAccesoDenegado() {

        mostrarAlerta(
                "Acceso restringido",
                "Acción no permitida",
                "Esta función requiere permisos de manager.",
                AlertType.WARNING
        );
    }

    private void mostrarAlerta(
            String titulo,
            String encabezado,
            String contenido,
            AlertType tipo) {

        if (sceneManager != null) {

            sceneManager.showAlert(
                    titulo,
                    encabezado,
                    contenido,
                    tipo
            );

        } else {

            Alert alert =
                    new Alert(tipo);

            alert.setTitle(titulo);
            alert.setHeaderText(encabezado);
            alert.setContentText(contenido);
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCerrarSesion() {

        if (sceneManager == null) {
            return;
        }

        SesionUsuario.getInstance()
                .cerrarSesion();

        try {

            sceneManager.showLoginView();

        } catch (Exception e) {

            sceneManager.showAlert(
                    "Error",
                    "No se pudo cerrar sesión",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }
    }
}