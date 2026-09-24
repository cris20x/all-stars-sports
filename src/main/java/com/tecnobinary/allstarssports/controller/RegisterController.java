package main.java.com.tecnobinary.allstarssports.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import main.java.com.tecnobinary.allstarssports.dto.request.RegisterDTORequest;
import main.java.com.tecnobinary.allstarssports.service.AuthService;
import main.java.com.tecnobinary.allstarssports.util.SceneManager;

public class RegisterController implements Initializable {

    private final AuthService authService;
    private final SceneManager sceneManager;

    @FXML
    private TextField txtFieldNombre;

    @FXML
    private TextField txtFieldApellido;

    @FXML
    private TextField txtFieldEmail;

    @FXML
    private ComboBox<String> comboExtension;

    @FXML
    private ComboBox<String> comboTipoCuenta;

    @FXML
    private PasswordField txtFieldPassword;

    @FXML
    private PasswordField txtFieldClaveRecuperacion;

    @FXML
    private PasswordField txtFieldClaveManager;

    @FXML
    private VBox boxClaveManager;

    @FXML
    private Button btnRegistrar;
    public RegisterController() {
        this.authService = null;
        this.sceneManager = null;
    }

    public RegisterController(
            AuthService authService,
            SceneManager sceneManager) {

        this.authService = authService;
        this.sceneManager = sceneManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboExtension.getItems().addAll(
                "@gmail.com",
                "@kinal.edu.gt"
        );

        comboExtension.setValue("@gmail.com");

        comboTipoCuenta.getItems().addAll(
                "Participante",
                "Manager"
        );

        comboTipoCuenta.setValue("Participante");

        comboTipoCuenta.setOnAction(event -> {

            boolean esManager =
                    "Manager".equals(
                            comboTipoCuenta.getValue()
                    );

            boxClaveManager.setVisible(esManager);
            boxClaveManager.setManaged(esManager);

        });
    }
    @FXML
    public void handleRegister() {
        if (authService == null
                || sceneManager == null) {
            return;
        }
        try {
            String nombre =
                    txtFieldNombre.getText().trim();

            String apellido =
                    txtFieldApellido.getText().trim();

            String parteCorreo =
                    txtFieldEmail.getText()
                            .trim()
                            .toLowerCase();

            String extension =
                    comboExtension.getValue();

            String email =
                    parteCorreo + extension;

            String password =
                    txtFieldPassword.getText();

            String claveRecuperacion =
                    txtFieldClaveRecuperacion.getText();
            int idRol =
                    "Manager".equals(
                            comboTipoCuenta.getValue()
                    ) ? 1 : 2;

            String claveManager =
                    txtFieldClaveManager.getText();

            RegisterDTORequest request =
                    new RegisterDTORequest(
                            nombre,
                            apellido,
                            email,
                            password,
                            idRol,
                            claveRecuperacion
                    );

            authService.registrarUsuario(
                    request,
                    claveManager
            );
            sceneManager.showAlert(
                    "Registro exitoso",
                    "Cuenta creada",
                    "Tu cuenta fue registrada correctamente.",
                    AlertType.INFORMATION
            );
            sceneManager.showLoginView();
        } catch (RuntimeException e) {
            sceneManager.showAlert(
                    "Error de registro",
                    "No se pudo crear la cuenta",
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