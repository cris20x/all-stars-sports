package main.java.com.tecnobinary.allstarssports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.java.com.tecnobinary.allstarssports.DatabaseConnection.DatabaseConnection;
import main.java.com.tecnobinary.allstarssports.model.Equipo;

public class EquipoRepository {

    public ObservableList<Equipo> findAll() {
        String sql = "select e.id_equipo, e.id_liga, e.nombre_equipo, l.nombre_liga "
                + "from equipos e inner join ligas l on e.id_liga = l.id_liga";
        ObservableList<Equipo> equipos = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                equipos.add(new Equipo(
                        rs.getInt("id_equipo"),
                        rs.getInt("id_liga"),
                        rs.getString("nombre_equipo"),
                        rs.getString("nombre_liga")
                ));
            }
            return equipos;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

}
