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
        String sql = "select e.id_equipo, e.id_liga, e.nombre_equipo, "
                + "e.imagen_url, l.nombre_liga, l.deporte "
                + "from equipos e "
                + "inner join ligas l on e.id_liga = l.id_liga";

        ObservableList<Equipo> equipos = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                Equipo equipo = new Equipo(
                        rs.getInt("id_equipo"),
                        rs.getInt("id_liga"),
                        rs.getString("nombre_equipo"),
                        rs.getString("nombre_liga"),
                        rs.getString("imagen_url")
                );
                equipo.setDeporte(rs.getString("deporte"));
                equipos.add(equipo);
            }

            return equipos;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    public boolean insertar(Equipo equipo) {
        String sql = "insert into equipos "
                + "(id_liga, nombre_equipo, imagen_url) "
                + "values (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS)) {

            pstm.setInt(1, equipo.getIdLiga());
            pstm.setString(2, equipo.getNombreEquipo());
            pstm.setString(3, equipo.getImagenUrl());

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
        String sql = "update equipos set "
                + "id_liga = ?, "
                + "nombre_equipo = ?, "
                + "imagen_url = ? "
                + "where id_equipo = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setInt(1, equipo.getIdLiga());
            pstm.setString(2, equipo.getNombreEquipo());
            pstm.setString(3, equipo.getImagenUrl());
            pstm.setInt(4, equipo.getIdEquipo());

            return pstm.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public boolean eliminar(int idEquipo) {
        String sqlMiembros = "delete from miembros_equipos where id_equipo = ?";
        String sql = "delete from equipos where id_equipo = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {

            boolean autoCommitOriginal = connection.getAutoCommit();
            connection.setAutoCommit(false);

            try (PreparedStatement pstmMiembros =
                            connection.prepareStatement(sqlMiembros);
                    PreparedStatement pstm =
                            connection.prepareStatement(sql)) {

                pstmMiembros.setInt(1, idEquipo);
                pstmMiembros.executeUpdate();

                pstm.setInt(1, idEquipo);
                int filas = pstm.executeUpdate();

                connection.commit();

                return filas > 0;

            } catch (SQLException e) {
                connection.rollback();
                System.err.println(e.getMessage());
            } finally {
                connection.setAutoCommit(autoCommitOriginal);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return false;
    }

    public ObservableList<Pair<Integer, String>> findLigas() {
        String sql = "select id_liga, nombre_liga "
                + "from ligas "
                + "order by nombre_liga";

        ObservableList<Pair<Integer, String>> ligas =
                FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()) {

            while (rs.next()) {
                ligas.add(new Pair<>(
                        rs.getInt("id_liga"),
                        rs.getString("nombre_liga")
                ));
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return ligas;
    }
}