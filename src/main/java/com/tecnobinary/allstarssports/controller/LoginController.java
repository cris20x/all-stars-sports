package main.java.com.tecnobinary.allstarssports.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import main.java.com.tecnobinary.allstarssports.dto.request.LoginDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.response.LoginDTOResponse;
import main.java.com.tecnobinary.allstarssports.model.SesionUsuario;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class LoginController implements Initializable {

    private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML
    private ImageView imgLogo;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private PasswordField txtFieldPassword;

    @FXML
    private Button btnIniciarSesion;

    public LoginController() {
        this.authService = null;
        this.sceneManager = null;
    }

    public LoginController(
            AuthService authService,
            SceneManager sceneManager) {

        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb) {
    }

    @FXML
public void handleLogin() {
    if (authService == null
            || sceneManager == null) {
        return;
    }

    try {
        LoginDTORequest request =
                new LoginDTORequest(
                        txtFieldEmail.getText(),
                        txtFieldPassword.getText()
                );

        LoginDTOResponse response =
                authService.login(request);

        SesionUsuario.getInstance()
                .iniciarSesion(response);

        sceneManager.showDashboardView();

    } catch (RuntimeException e) {
        sceneManager.showAlert(
                "Error de inicio de sesión",
                "No se pudo iniciar sesión",
                "Correo o contraseña incorrectos.",
                AlertType.ERROR
        );

    } catch (Exception e) {
        sceneManager.showAlert(
                "Error inesperado",
                "Ocurrió un problema",
                "No se pudo iniciar sesión. Intenta nuevamente.",
                AlertType.ERROR
        );
    }
}

    @FXML
    public void handleRegister() {
        try {
            sceneManager.showRegisterView();
        } catch (Exception e) {
            sceneManager.showAlert(
                    "Error",
                    "No se pudo abrir el registro",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }
    }

    @FXML
    public void handleRecovery() {
        try {
            sceneManager.showRecoveryView();
        } catch (Exception e) {
            sceneManager.showAlert(
                    "Error",
                    "No se pudo abrir la recuperación",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }
    }
}