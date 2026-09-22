package main.java.com.tecnobinary.allstarssports.service;

import main.java.com.tecnobinary.allstarssports.dto.request.LoginDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.response.LoginDTOResponse;
import main.java.com.tecnobinary.allstarssports.repository.AuthRepository;
import main.java.com.tecnobinary.allstarssports.security.jbcrypt.BCrypt;

public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LoginDTOResponse login(LoginDTORequest request) {

        if (request == null || request.getEmail() == null || request.getPassword() == null
                || request.getEmail().isBlank() || request.getPassword().isBlank()) {
            throw new RuntimeException("Debes ingresar correo y contraseña");
        }

        LoginDTOResponse response = authRepository.findUsuarioByEmail(request);

        if (response == null || response.getPasswordHash() == null) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        if (!BCrypt.checkpw(request.getPassword(), response.getPasswordHash())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        return response;
    }

}
