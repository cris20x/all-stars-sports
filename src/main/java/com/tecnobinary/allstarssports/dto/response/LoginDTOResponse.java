package main.java.com.tecnobinary.allstarssports.dto.response;

public class LoginDTOResponse {

    private String idUsuario;
    private String nombre;
    private String apellido;
    private String passwordHash;
    private int idRol;
    private String nombreRol;

    public LoginDTOResponse(String idUsuario, String nombre, String apellido, String passwordHash, int idRol, String nombreRol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.passwordHash = passwordHash;
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

}
