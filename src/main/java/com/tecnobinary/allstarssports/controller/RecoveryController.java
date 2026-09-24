package main.java.com.tecnobinary.allstarssports.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import main.java.com.tecnobinary.allstarssports.dto.request.RecoveryDTORequest;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class RecoveryController implements Initializable {

    private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private PasswordField txtFieldClaveRecuperacion;

    @FXML
    private PasswordField txtFieldNuevaPassword;

    @FXML
    private PasswordField txtFieldConfirmarPassword;

    public RecoveryController() {
        this.authService = null;
        this.sceneManager = null;
    }

    public RecoveryController(
            AuthService authService,
            SceneManager sceneManager) {

        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void handleRecovery() {

        if (authService == null || sceneManager == null) {
            return;
        }
        try {
            String email =
                    txtFieldEmail.getText()
                            .trim()
                            .toLowerCase();

            String claveRecuperacion =
                    txtFieldClaveRecuperacion.getText();

            String nuevaPassword =
                    txtFieldNuevaPassword.getText();

            String confirmarPassword =
                    txtFieldConfirmarPassword.getText();

            if (!nuevaPassword.equals(confirmarPassword)) {
                throw new RuntimeException(
                        "Las contraseñas nuevas no coinciden");
            }

            RecoveryDTORequest request =
                    new RecoveryDTORequest(
                            email,
                            claveRecuperacion,
                            nuevaPassword
                    );
            authService.recuperarCuenta(request);

            sceneManager.showAlert(
                    "Recuperación exitosa",
                    "Contraseña actualizada",
                    "Tu contraseña fue actualizada correctamente.",
                    AlertType.INFORMATION
            );
            sceneManager.showLoginView();

        } catch (RuntimeException e) {
            sceneManager.showAlert(
                    "Error de recuperación",
                    "No se pudo recuperar la cuenta",
                    e.getMessage(),
                    AlertType.ERROR
            );
        } catch (Exception e) {
            sceneManager.showAlert(
                    "Error inesperado",
                    "Ocurrió un problema",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }
    }

    @FXML
    public void handleBack() {
        try {
            sceneManager.showLoginView();
        } catch (Exception e) {
            sceneManager.showAlert(
                    "Error",
                    "No se pudo regresar",
                    e.getMessage(),
                    AlertType.ERROR
            );
        }
    }
}