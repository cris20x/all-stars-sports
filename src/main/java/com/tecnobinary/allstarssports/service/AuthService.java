package main.java.com.tecnobinary.allstarssports.service;

import main.java.com.tecnobinary.allstarssports.dto.request.LoginDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.request.RegisterDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.response.LoginDTOResponse;
import main.java.com.tecnobinary.allstarssports.repository.AuthRepository;
import main.java.com.tecnobinary.allstarssports.security.jbcrypt.BCrypt;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    // =========================
    // LOGIN
    // =========================

    public LoginDTOResponse login(LoginDTORequest request) {

        if (request == null
                || request.getEmail() == null
                || request.getPassword() == null
                || request.getEmail().isBlank()
                || request.getPassword().isBlank()) {

            throw new RuntimeException(
                    "Debes ingresar correo y contraseña"
            );
        }

        LoginDTOResponse response =
                authRepository.findUsuarioByEmail(request);

        if (response == null
                || response.getPasswordHash() == null) {

            throw new RuntimeException(
                    "Usuario o contraseña incorrectos"
            );
        }

        if (!BCrypt.checkpw(
                request.getPassword(),
                response.getPasswordHash())) {

            throw new RuntimeException(
                    "Usuario o contraseña incorrectos"
            );
        }

        return response;
    }

    // =========================
    // REGISTRO
    // =========================

    public void registrarUsuario(
            RegisterDTORequest request,
            String claveManager) {

        if (request == null) {
            throw new RuntimeException(
                    "Los datos de registro son inválidos"
            );
        }

        validarNombre(
                request.getNombre(),
                "nombre"
        );

        validarNombre(
                request.getApellido(),
                "apellido"
        );

        validarEmail(
                request.getEmail()
        );

        validarPassword(
                request.getPassword()
        );

        validarClaveRecuperacion(
                request.getClaveRecuperacion()
        );

        int idRol = request.getIdRol();

        if (idRol != 1 && idRol != 2) {
            throw new RuntimeException(
                    "El tipo de cuenta seleccionado no es válido"
            );
        }

        // Manager
        if (idRol == 1) {

            if (claveManager == null
                    || claveManager.isBlank()) {

                throw new RuntimeException(
                        "Debes ingresar la clave de autorización de Manager"
                );
            }

            if (!authRepository.verificarClaveManager(
                    claveManager)) {

                throw new RuntimeException(
                        "La clave de autorización de Manager es incorrecta"
                );
            }
        }

        if (authRepository.existeEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Ya existe una cuenta registrada con ese correo"
            );
        }

        String passwordHash =
                BCrypt.hashpw(
                        request.getPassword(),
                        BCrypt.gensalt(12)
                );

        String claveRecuperacionHash =
                BCrypt.hashpw(
                        request.getClaveRecuperacion(),
                        BCrypt.gensalt(12)
                );

        RegisterDTORequest requestFinal =
                new RegisterDTORequest(
                        request.getNombre(),
                        request.getApellido(),
                        request.getEmail(),
                        request.getPassword(),
                        idRol,
                        request.getClaveRecuperacion()
                );

        authRepository.registrarUsuario(
                requestFinal,
                passwordHash,
                claveRecuperacionHash
        );
    }

    // =========================
    // VALIDACIONES
    // =========================

    private void validarNombre(
            String valor,
            String campo) {

        if (valor == null || valor.isBlank()) {

            throw new RuntimeException(
                    "Debes ingresar tu " + campo
            );
        }

        String texto =
                valor.trim();

        if (texto.length() < 2) {

            throw new RuntimeException(
                    "El " + campo + " debe tener al menos 2 caracteres"
            );
        }

        if (texto.length() > 60) {

            throw new RuntimeException(
                    "El " + campo + " no puede superar los 60 caracteres"
            );
        }

        if (!texto.matches(
                "[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ ]+")) {

            throw new RuntimeException(
                    "El " + campo
                    + " solo puede contener letras"
            );
        }
    }

    private void validarEmail(
            String email) {

        if (email == null || email.isBlank()) {

            throw new RuntimeException(
                    "Debes ingresar tu correo electrónico"
            );
        }

        String correo =
                email.trim().toLowerCase();

        if (!correo.endsWith("@gmail.com")
                && !correo.endsWith("@kinal.edu.gt")) {

            throw new RuntimeException(
                    "Solo se permiten correos @gmail.com o @kinal.edu.gt"
            );
        }

        String usuario;

        if (correo.endsWith("@gmail.com")) {

            usuario = correo.substring(
                    0,
                    correo.length() - "@gmail.com".length()
            );

        } else {

            usuario = correo.substring(
                    0,
                    correo.length() - "@kinal.edu.gt".length()
            );
        }

        if (usuario.isBlank()) {

            throw new RuntimeException(
                    "Debes ingresar la parte del correo antes de @"
            );
        }

        if (usuario.length() > 60) {

            throw new RuntimeException(
                    "El correo es demasiado largo"
            );
        }

        if (!usuario.matches(
                "[a-z0-9.]+")) {

            throw new RuntimeException(
                    "El correo solo puede contener letras, números y puntos"
            );
        }

        if (usuario.startsWith(".")
                || usuario.endsWith(".")
                || usuario.contains("..")) {

            throw new RuntimeException(
                    "El formato del correo no es válido"
            );
        }
    }

    private void validarPassword(
            String password) {

        if (password == null
                || password.isBlank()) {

            throw new RuntimeException(
                    "Debes ingresar una contraseña"
            );
        }

        if (password.length() < 8) {

            throw new RuntimeException(
                    "La contraseña debe tener al menos 8 caracteres"
            );
        }

        if (password.length() > 100) {

            throw new RuntimeException(
                    "La contraseña es demasiado larga"
            );
        }
    }

    private void validarClaveRecuperacion(
            String clave) {

        if (clave == null
                || clave.isBlank()) {

            throw new RuntimeException(
                    "Debes ingresar una clave de recuperación"
            );
        }

        if (clave.length() < 6) {

            throw new RuntimeException(
                    "La clave de recuperación debe tener al menos 6 caracteres"
            );
        }

        if (clave.length() > 100) {

            throw new RuntimeException(
                    "La clave de recuperación es demasiado larga"
            );
        }
    }
}