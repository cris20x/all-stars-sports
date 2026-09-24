package main.java.com.tecnobinary.allstarssports.model;

import java.time.LocalDate;

public class Torneo {

    private int idTorneo;
    private int idLiga;
    private String nombreTorneo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String nombreLiga;

    public Torneo() {
    }

    public Torneo(int idTorneo, int idLiga, String nombreTorneo,
            LocalDate fechaInicio, LocalDate fechaFin,
            String estado, String nombreLiga) {
        this.idTorneo = idTorneo;
        this.idLiga = idLiga;
        this.nombreTorneo = nombreTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        this.nombreLiga = nombreLiga;
    }

    public int getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    public int getIdLiga() {
        return idLiga;
    }

    public void setIdLiga(int idLiga) {
        this.idLiga = idLiga;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreLiga() {
        return nombreLiga;
    }

    public void setNombreLiga(String nombreLiga) {
        this.nombreLiga = nombreLiga;
    }

    @Override
    public String toString() {
        return nombreTorneo + " - " + nombreLiga;
    }
}