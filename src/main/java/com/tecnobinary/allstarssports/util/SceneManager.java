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
import main.java.com.tecnobinary.allstarssports.repository.AuthRepository;
import main.java.com.tecnobinary.allstarssports.repository.EquipoRepository;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.service.DashboardService;

public class SceneManager {

    private final Stage stage;

    private boolean splashMostrado = false;


    private double windowX = Double.NaN;
    private double windowY = Double.NaN;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void showSplashView() throws Exception {


        if (splashMostrado) {
            showLoginView();
            return;
        }

        splashMostrado = true;

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/main/resources/view/splash-view.fxml"
                )
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("All-Stars Sports League");
        stage.setScene(scene);

  
        stage.centerOnScreen();

 
        stage.setResizable(false);

        stage.show();

        PauseTransition pause =
                new PauseTransition(
                        Duration.seconds(1.5)
                );

        pause.setOnFinished(event -> {

            try {

                guardarPosicion();

                showLoginView();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        pause.play();
    }

    public void showLoginView() throws Exception {

        guardarPosicion();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/main/resources/view/login-view.fxml"
                )
        );

        loader.setControllerFactory(clazz -> {

            if (clazz == LoginController.class) {

                AuthRepository authRepository =
                        new AuthRepository();

                AuthService authService =
                        new AuthService(authRepository);

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

        Scene scene = new Scene(
                root,
                600,
                400
        );

        stage.setTitle(
                "All-Stars Sports League"
        );

        stage.setScene(scene);

        stage.setResizable(true);

        stage.setMinWidth(500);
        stage.setMinHeight(350);

        restaurarPosicion();

        stage.show();
    }

    public void showDashboardView() throws Exception {

        guardarPosicion();

        FXMLLoader loader = new FXMLLoader(
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

        Scene scene = new Scene(
                root,
                900,
                600
        );

        stage.setTitle(
                "All-Stars Sports League - Dashboard"
        );

        stage.setScene(scene);

        stage.setResizable(true);

        stage.setMinWidth(700);
        stage.setMinHeight(500);

        restaurarPosicion();

        stage.show();
    }

    private void guardarPosicion() {

        windowX = stage.getX();
        windowY = stage.getY();
    }

    private void restaurarPosicion() {

        if (!Double.isNaN(windowX)
                && !Double.isNaN(windowY)) {

            stage.setX(windowX);
            stage.setY(windowY);
        }
    }

    public void showAlert(
            String title,
            String header,
            String content,
            AlertType type
    ) {

        Alert alert = new Alert(type);

        alert.initOwner(stage);

        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}