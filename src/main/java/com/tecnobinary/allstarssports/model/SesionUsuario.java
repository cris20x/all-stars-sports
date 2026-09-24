package main.java.com.tecnobinary.allstarssports.model;

import main.java.com.tecnobinary.allstarssports.dto.response.LoginDTOResponse;

public class SesionUsuario {

    private static SesionUsuario instance;

    private String idUsuario;
    private String nombre;
    private String apellido;
    private int idRol;
    private String nombreRol;

    private SesionUsuario() {
    }

    public static SesionUsuario getInstance() {
        if (instance == null) {
            instance = new SesionUsuario();
        }
        return instance;
    }

    public void iniciarSesion(LoginDTOResponse response) {
        this.idUsuario = response.getIdUsuario();
        this.nombre = response.getNombre();
        this.apellido = response.getApellido();
        this.idRol = response.getIdRol();
        this.nombreRol = response.getNombreRol();
    }

    public void cerrarSesion() {
        instance = null;
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

    public int getIdRol() {
        return idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public boolean isManager() {
        return idRol == 1;
    }

}
