package main.java.com.tecnobinary.allstarssports.dto.request;

public class RegisterDTORequest {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private int idRol;
    private String claveRecuperacion;

    public RegisterDTORequest(
            String nombre,
            String apellido,
            String email,
            String password,
            int idRol,
            String claveRecuperacion) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.idRol = idRol;
        this.claveRecuperacion = claveRecuperacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getClaveRecuperacion() {
        return claveRecuperacion;
    }
}