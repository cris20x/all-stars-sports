package main.java.com.tecnobinary.allstarssports.util;

import java.io.IOException;

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
import main.java.com.tecnobinary.allstarssports.controller.RecoveryController;
import main.java.com.tecnobinary.allstarssports.controller.RegisterController;
import main.java.com.tecnobinary.allstarssports.repository.AuthRepository;
import main.java.com.tecnobinary.allstarssports.repository.EquipoRepository;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.service.DashboardService;

public class SceneManager {

    private final Stage stage;

    private final AuthService authService;

    private double windowX = Double.NaN;
    private double windowY = Double.NaN;
    private double windowWidth = Double.NaN;
    private double windowHeight = Double.NaN;

    private boolean windowMaximized = false;

    public SceneManager(Stage stage) {
        this.stage = stage;
        AuthRepository authRepository =
                new AuthRepository();
        this.authService =
                new AuthService(authRepository);
    }
    public void showSplashView()
            throws IOException {
        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/splash-view.fxml"
                        )
                );
        Parent root =
                loader.load();
        stage.setScene(
                new Scene(root, 600, 400)
        );
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
        PauseTransition pausa =
                new PauseTransition(
                        Duration.seconds(1.5)
                );

        pausa.setOnFinished(event -> {
            guardarEstadoVentana();
            try {
                showLoginView();
            } catch (IOException e) {
                showAlert(
                        "Error",
                        "No se pudo abrir el inicio de sesión",
                        e.getMessage(),
                        AlertType.ERROR
                );
            }
        });
        pausa.play();
    }

    public void showLoginView()
            throws IOException {
        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/login-view.fxml"
                        )
                );
        loader.setControllerFactory(
                type -> {
                    if (type == LoginController.class) {

                        return new LoginController(
                                authService,
                                this
                        );
                    }

                    try {

                        return type.getDeclaredConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        Parent root =
                loader.load();

        stage.setResizable(true);
        stage.setMinWidth(500);
        stage.setMinHeight(350);

        cambiarEscena(
                root,
                600,
                400
        );
    }

    public void showRegisterView()
            throws IOException {
        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/register-view.fxml"
                        )
                );

        loader.setControllerFactory(
                type -> {
                    if (type == RegisterController.class) {
                        return new RegisterController(
                                authService,
                                this
                        );
                    }

                    try {

                        return type.getDeclaredConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        Parent root =
                loader.load();

        stage.setResizable(true);
        stage.setMinWidth(580);
        stage.setMinHeight(580);

        cambiarEscena(
                root,
                700,
                600
        );
    }

    public void showRecoveryView()
            throws IOException {
        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/recovery-view.fxml"
                        )
                );

        loader.setControllerFactory(
                type -> {
                    if (type == RecoveryController.class) {
                        return new RecoveryController(
                                authService,
                                this
                        );
                    }

                    try {
                        return type.getDeclaredConstructor()
                                .newInstance();
                    } catch (Exception e) {

                        throw new RuntimeException(e);
                    }
                }
        );

        Parent root =
                loader.load();
        stage.setResizable(true);
        stage.setMinWidth(500);
        stage.setMinHeight(500);
        cambiarEscena(
                root,
                600,
                500
        );
    }

    public void showDashboardView()
            throws IOException {
        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/main/resources/view/dashboard-view.fxml"
                        )
                );
        DashboardService dashboardService =
                new DashboardService(
                        new EquipoRepository()
                );
        loader.setControllerFactory(
                type -> {

                    if (type == DashboardController.class) {
                        return new DashboardController(
                                dashboardService,
                                this
                        );
                    }

                    try {
                        return type.getDeclaredConstructor()
                                .newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        Parent root =
                loader.load();
        stage.setResizable(true);
        stage.setMinWidth(700);
        stage.setMinHeight(500);
        cambiarEscena(
                root,
                900,
                600
        );
    }

    private void cambiarEscena(
            Parent root,
            double anchoInicial,
            double altoInicial) {
        boolean estabaMostrando =
                stage.isShowing();

        boolean estabaMaximizada =
                stage.isMaximized();

        double x =
                stage.getX();

        double y =
                stage.getY();

        double ancho =
                stage.getWidth();

        double alto =
                stage.getHeight();

        boolean tamanoValido =
                ancho > 0
                && alto > 0
                && !Double.isNaN(ancho)
                && !Double.isNaN(alto);

        Scene nuevaScene =
                new Scene(root);
        stage.setScene(nuevaScene);

        if (!estabaMostrando
                || !tamanoValido) {

            stage.setWidth(
                    anchoInicial
            );
            stage.setHeight(
                    altoInicial
            );
            stage.centerOnScreen();

        } else if (estabaMaximizada) {
            stage.setMaximized(true);
        } else {

            stage.setMaximized(false);

            double nuevoAncho =
                    Math.max(
                            ancho,
                            stage.getMinWidth()
                    );
            double nuevoAlto =
                    Math.max(
                            alto,
                            stage.getMinHeight()
                    );
            stage.setWidth(
                    nuevoAncho
            );
            stage.setHeight(
                    nuevoAlto
            );
            stage.setX(x);
            stage.setY(y);
        }
        if (!stage.isShowing()) {
            stage.show();
        }
    }

    private void guardarEstadoVentana() {
        if (!stage.isMaximized()) {
            windowX =
                    stage.getX();

            windowY =
                    stage.getY();

            windowWidth =
                    stage.getWidth();

            windowHeight =
                    stage.getHeight();
        }

        windowMaximized =
                stage.isMaximized();
    }

    public void showAlert(
            String titulo,
            String encabezado,
            String contenido,
            AlertType tipo) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);

        alert.showAndWait();
    }
}