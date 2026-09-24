package main.java.com.tecnobinary.allstarssports.dto.request;

public class RecoveryDTORequest {

    private String email;
    private String claveRecuperacion;
    private String nuevaPassword;

    public RecoveryDTORequest(
            String email,
            String claveRecuperacion,
            String nuevaPassword) {

        this.email = email;
        this.claveRecuperacion = claveRecuperacion;
        this.nuevaPassword = nuevaPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getClaveRecuperacion() {
        return claveRecuperacion;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }
}