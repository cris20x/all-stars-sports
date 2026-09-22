package main.java.com.tecnobinary.allstarssports.util;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Duration;

import main.java.com.tecnobinary.allstarssports.controller.DashboardController;
import main.java.com.tecnobinary.allstarssports.controller.LoginController;
import main.java.com.tecnobinary.allstarssports.controller.RegisterController;
import main.java.com.tecnobinary.allstarssports.repository.AuthRepository;
import main.java.com.tecnobinary.allstarssports.repository.EquipoRepository;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.service.DashboardService;

public class SceneManager {

    private final Stage stage;

    private boolean splashMostrado = false;

    private double windowX = Double.NaN;
    private double windowY = Double.NaN;

    private double windowWidth = Double.NaN;
    private double windowHeight = Double.NaN;

    private boolean windowMaximized = false;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    // =========================
    // SPLASH
    // =========================

    public void showSplashView() throws Exception {

        if (splashMostrado) {
            showLoginView();
            return;
        }

        splashMostrado = true;

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/splash-view.fxml"
                        )
                );

        Parent root = loader.load();

        Scene scene =
                new Scene(
                        root,
                        600,
                        400
                );

        stage.setScene(scene);

        stage.setResizable(false);

        stage.centerOnScreen();

        stage.show();

        PauseTransition pause =
                new PauseTransition(
                        Duration.seconds(1.5)
                );

        pause.setOnFinished(event -> {

            try {

                guardarEstadoVentana();

                showLoginView();

            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        pause.play();
    }

    // =========================
    // LOGIN
    // =========================

    public void showLoginView() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/login-view.fxml"
                        )
                );

        loader.setControllerFactory(clazz -> {

            if (clazz == LoginController.class) {

                AuthRepository authRepository =
                        new AuthRepository();

                AuthService authService =
                        new AuthService(
                                authRepository
                        );

                return new LoginController(
                        authService,
                        this
                );
            }

            try {

                return clazz
                        .getDeclaredConstructor()
                        .newInstance();

            } catch (Exception e) {

                throw new RuntimeException(
                        "Error al crear el controlador: "
                        + e.getMessage()
                );
            }
        });

        Parent root = loader.load();

        cambiarEscena(
                root,
                600,
                400
        );

        stage.setResizable(true);

        stage.setMinWidth(500);
        stage.setMinHeight(350);

        restaurarEstadoVentana();
    }

    // =========================
    // REGISTRO
    // =========================

    public void showRegisterView() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/register-view.fxml"
                        )
                );

        loader.setControllerFactory(clazz -> {

            if (clazz == RegisterController.class) {

                AuthRepository authRepository =
                        new AuthRepository();

                AuthService authService =
                        new AuthService(
                                authRepository
                        );

                return new RegisterController(
                        authService,
                        this
                );
            }

            try {

                return clazz
                        .getDeclaredConstructor()
                        .newInstance();

            } catch (Exception e) {

                throw new RuntimeException(
                        "Error al crear el controlador: "
                        + e.getMessage()
                );
            }
        });

        Parent root = loader.load();

        cambiarEscena(
                root,
                700,
                600
        );

        stage.setResizable(true);

        stage.setMinWidth(600);
        stage.setMinHeight(580);

        restaurarEstadoVentana();
    }

    // =========================
    // DASHBOARD
    // =========================

    public void showDashboardView() throws Exception {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/dashboard-view.fxml"
                        )
                );

        loader.setControllerFactory(clazz -> {

            if (clazz == DashboardController.class) {

                EquipoRepository equipoRepository =
                        new EquipoRepository();

                DashboardService dashboardService =
                        new DashboardService(
                                equipoRepository
                        );

                return new DashboardController(
                        dashboardService,
                        this
                );
            }

            try {

                return clazz
                        .getDeclaredConstructor()
                        .newInstance();

            } catch (Exception e) {

                throw new RuntimeException(
                        "Error al crear el controlador: "
                        + e.getMessage()
                );
            }
        });

        Parent root = loader.load();

        cambiarEscena(
                root,
                900,
                600
        );

        stage.setResizable(true);

        stage.setMinWidth(700);
        stage.setMinHeight(500);

        restaurarEstadoVentana();
    }

    // =========================
    // CAMBIO DE ESCENA
    // =========================

    private void cambiarEscena(
            Parent root,
            double anchoInicial,
            double altoInicial
    ) {

        guardarEstadoVentana();

        boolean primeraEscena =
                Double.isNaN(windowWidth)
                || Double.isNaN(windowHeight);

        Scene scene;

        if (primeraEscena) {

            scene =
                    new Scene(
                            root,
                            anchoInicial,
                            altoInicial
                    );

        } else {

            scene =
                    new Scene(
                            root,
                            windowWidth,
                            windowHeight
                    );
        }

        stage.setScene(scene);

        stage.show();
    }

    // =========================
    // GUARDAR ESTADO
    // =========================

    private void guardarEstadoVentana() {

        if (!stage.isShowing()) {
            return;
        }

        if (!stage.isMaximized()) {

            windowX = stage.getX();
            windowY = stage.getY();

            windowWidth = stage.getWidth();
            windowHeight = stage.getHeight();
        }

        windowMaximized =
                stage.isMaximized();
    }

    // =========================
    // RESTAURAR ESTADO
    // =========================

    private void restaurarEstadoVentana() {

        if (windowMaximized) {

            stage.setMaximized(true);

            return;
        }

        if (!Double.isNaN(windowX)
                && !Double.isNaN(windowY)) {

            stage.setX(windowX);
            stage.setY(windowY);
        }

        if (!Double.isNaN(windowWidth)
                && !Double.isNaN(windowHeight)) {

            stage.setWidth(windowWidth);
            stage.setHeight(windowHeight);
        }
    }

    // =========================
    // ALERTAS
    // =========================

    public void showAlert(
            String title,
            String header,
            String content,
            AlertType type
    ) {

        Alert alert =
                new Alert(type);

        alert.initOwner(stage);

        alert.setTitle(title);

        alert.setHeaderText(header);

        alert.setContentText(content);

        alert.showAndWait();
    }
}