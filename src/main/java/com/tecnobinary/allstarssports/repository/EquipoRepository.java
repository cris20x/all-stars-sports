package main.java.com.tecnobinary.allstarssports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
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

    public boolean insertar(Equipo equipo) {
        String sql = "insert into equipos (id_liga, nombre_equipo) values (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, equipo.getIdLiga());
            pstm.setString(2, equipo.getNombreEquipo());

            int filasAfectadas = pstm.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        equipo.setIdEquipo(rs.getInt(1));
                    }
                }
            }
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean actualizar(Equipo equipo) {
        String sql = "update equipos set id_liga = ?, nombre_equipo = ? where id_equipo = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, equipo.getIdLiga());
            pstm.setString(2, equipo.getNombreEquipo());
            pstm.setInt(3, equipo.getIdEquipo());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public boolean eliminar(int idEquipo) {
        String sql = "delete from equipos where id_equipo = ?";
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, idEquipo);
            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public ObservableList<Pair<Integer, String>> findLigas() {
        String sql = "select id_liga, nombre_liga from ligas order by nombre_liga";
        ObservableList<Pair<Integer, String>> ligas = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                ligas.add(new Pair<>(rs.getInt("id_liga"), rs.getString("nombre_liga")));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return ligas;
    }

}
