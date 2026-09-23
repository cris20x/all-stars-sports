package main.java.com.tecnobinary.allstarssports.service;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import main.java.com.tecnobinary.allstarssports.model.Equipo;
import main.java.com.tecnobinary.allstarssports.repository.EquipoRepository;

public class DashboardService {

    private final EquipoRepository equipoRepository;

    public DashboardService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    public ObservableList<Equipo> findEquipos() {
        ObservableList<Equipo> equipos = equipoRepository.findAll();
        if (equipos == null) {
            throw new RuntimeException("No se pudieron cargar los equipos");
        }
        return equipos;
    }

    public boolean crearEquipo(Equipo equipo) {
        return equipoRepository.insertar(equipo);
    }

    public boolean actualizarEquipo(Equipo equipo) {
        return equipoRepository.actualizar(equipo);
    }

    public boolean eliminarEquipo(int idEquipo) {
        return equipoRepository.eliminar(idEquipo);
    }

    public ObservableList<Pair<Integer, String>> findLigas() {
        return equipoRepository.findLigas();
    }

}
