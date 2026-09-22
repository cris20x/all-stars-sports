package main.java.com.tecnobinary.allstarssports.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import main.java.com.tecnobinary.allstarssports.controller.DashboardController;
import main.java.com.tecnobinary.allstarssports.controller.LoginController;
import main.java.com.tecnobinary.allstarssports.repository.AuthRepository;
import main.java.com.tecnobinary.allstarssports.repository.EquipoRepository;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.service.DashboardService;

public class SceneManager {

    private final Stage stage;

    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void showLoginView() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/login-view.fxml"));

        loader.setControllerFactory(clazz -> {
            if (clazz == LoginController.class) {
                AuthRepository authRepository = new AuthRepository();
                AuthService authService = new AuthService(authRepository);
                return new LoginController(authService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("All-Stars Sports League");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void showDashboardView() throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/resources/view/dashboard-view.fxml"));

        loader.setControllerFactory(clazz -> {
            if (clazz == DashboardController.class) {
                EquipoRepository equipoRepository = new EquipoRepository();
                DashboardService dashboardService = new DashboardService(equipoRepository);
                return new DashboardController(dashboardService, this);
            }
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Error al crear el controlador: " + e.getMessage());
            }
        });

        Parent root = loader.load();
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("All-Stars Sports League - Dashboard");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void showAlert(String title, String header, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
