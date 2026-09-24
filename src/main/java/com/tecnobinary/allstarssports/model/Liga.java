package main.java.com.tecnobinary.allstarssports.model;

public class Liga {

    private int idLiga;
    private String nombreLiga;
    private String deporte;

    public Liga() {
    }

    public Liga(int idLiga, String nombreLiga, String deporte) {
        this.idLiga = idLiga;
        this.nombreLiga = nombreLiga;
        this.deporte = deporte;
    }

    public int getIdLiga() {
        return idLiga;
    }

    public void setIdLiga(int idLiga) {
        this.idLiga = idLiga;
    }

    public String getNombreLiga() {
        return nombreLiga;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga = nombreLiga;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    @Override
    public String toString() {
        return nombreLiga + " - " + deporte;
    }
}