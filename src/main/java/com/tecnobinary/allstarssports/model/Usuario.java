/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.java.com.tecnobinary.allstarssports.model;

/**
 *
 * @author informatica
 */
public class Usuario {
    private String idUsuario;
    private int idRol;
    private String nombre;
    private String apellido;
    private String email;
    private String passwordHash;

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    
     public Usuario(){
}
     
     public Usuario(String idUsuario, int idRol, String nombre, String apellido, String email, String passwordHash){
     this.idUsuario = idUsuario;
     this.idRol = idRol;
     this.nombre = nombre;
     this.apellido = apellido;
     this.email = email;
     this.passwordHash = passwordHash;
     }
}
