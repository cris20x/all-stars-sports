package main.java.com.tecnobinary.allstarssports.model;

public class Equipo {

    private int idEquipo;
    private int idLiga;
    private String nombreEquipo;
    private String nombreLiga;

    public Equipo() {
    }

    public Equipo(int idEquipo, int idLiga, String nombreEquipo, String nombreLiga) {
        this.idEquipo = idEquipo;
        this.idLiga = idLiga;
        this.nombreEquipo = nombreEquipo;
        this.nombreLiga = nombreLiga;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public int getIdLiga() {
        return idLiga;
    }

    public void setIdLiga(int idLiga) {
        this.idLiga = idLiga;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNombreLiga() {
        return nombreLiga;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga = nombreLiga;
    }

}
