package main.java.com.tecnobinary.allstarssports.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import main.java.com.tecnobinary.allstarssports.DatabaseConnection.DatabaseConnection;
import main.java.com.tecnobinary.allstarssports.model.Liga;
import main.java.com.tecnobinary.allstarssports.model.Torneo;

public class GestionRepository {

    public List<Liga> listarLigas() throws SQLException {

        List<Liga> ligas = new ArrayList<>();

        String sql = """
                SELECT id_liga, nombre_liga, deporte
                FROM ligas
                ORDER BY id_liga
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                ligas.add(
                        new Liga(
                                resultado.getInt("id_liga"),
                                resultado.getString("nombre_liga"),
                                resultado.getString("deporte")
                        )
                );
            }
        }

        return ligas;
    }

    public boolean crearLiga(String nombre, String deporte)
            throws SQLException {

        String sql = """
                INSERT INTO ligas
                (id_liga, nombre_liga, deporte)
                VALUES (?, ?, ?)
                """;

        int nuevoId = obtenerSiguienteIdLiga();

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, nuevoId);
            sentencia.setString(2, nombre);
            sentencia.setString(3, deporte);

            return sentencia.executeUpdate() > 0;
        }
    }

    private int obtenerSiguienteIdLiga() throws SQLException {

        String sql = "SELECT COALESCE(MAX(id_liga), 0) + 1 AS siguiente FROM ligas";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            if (resultado.next()) {
                return resultado.getInt("siguiente");
            }
        }

        return 1;
    }

    public boolean actualizarLiga(Liga liga)
            throws SQLException {

        String sql = """
                UPDATE ligas
                SET nombre_liga = ?, deporte = ?
                WHERE id_liga = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, liga.getNombreLiga());
            sentencia.setString(2, liga.getDeporte());
            sentencia.setInt(3, liga.getIdLiga());

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminarLiga(int idLiga)
            throws SQLException {

        String sql = "DELETE FROM ligas WHERE id_liga = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, idLiga);

            return sentencia.executeUpdate() > 0;
        }
    }

    public List<Torneo> listarTorneos()
            throws SQLException {

        List<Torneo> torneos = new ArrayList<>();

        String sql = """
                SELECT t.id_torneo,
                       t.id_liga,
                       t.nombre_torneo,
                       t.fecha_inicio,
                       t.fecha_fin,
                       t.estado,
                       l.nombre_liga
                FROM torneos t
                INNER JOIN ligas l
                    ON t.id_liga = l.id_liga
                ORDER BY t.id_torneo
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                torneos.add(
                        new Torneo(
                                resultado.getInt("id_torneo"),
                                resultado.getInt("id_liga"),
                                resultado.getString("nombre_torneo"),
                                resultado.getDate("fecha_inicio").toLocalDate(),
                                resultado.getDate("fecha_fin").toLocalDate(),
                                resultado.getString("estado"),
                                resultado.getString("nombre_liga")
                        )
                );
            }
        }

        return torneos;
    }

    public boolean crearTorneo(
            int idLiga,
            String nombre,
            LocalDate inicio,
            LocalDate fin,
            String estado) throws SQLException {

        String sql = """
                INSERT INTO torneos
                (id_liga, nombre_torneo, fecha_inicio, fecha_fin, estado)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, idLiga);
            sentencia.setString(2, nombre);
            sentencia.setDate(3, Date.valueOf(inicio));
            sentencia.setDate(4, Date.valueOf(fin));
            sentencia.setString(5, estado);

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean actualizarTorneo(Torneo torneo)
            throws SQLException {

        String sql = """
                UPDATE torneos
                SET id_liga = ?,
                    nombre_torneo = ?,
                    fecha_inicio = ?,
                    fecha_fin = ?,
                    estado = ?
                WHERE id_torneo = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, torneo.getIdLiga());
            sentencia.setString(2, torneo.getNombreTorneo());
            sentencia.setDate(3, Date.valueOf(torneo.getFechaInicio()));
            sentencia.setDate(4, Date.valueOf(torneo.getFechaFin()));
            sentencia.setString(5, torneo.getEstado());
            sentencia.setInt(6, torneo.getIdTorneo());

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean eliminarTorneo(int idTorneo)
            throws SQLException {

        String sql = "DELETE FROM torneos WHERE id_torneo = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setInt(1, idTorneo);

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean unirseEquipo(
            String idUsuario,
            int idEquipo) throws SQLException {

        String sql = """
                INSERT INTO miembros_equipos
                (id_usuario, id_equipo)
                VALUES (?, ?)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);
            sentencia.setInt(2, idEquipo);

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean salirEquipo(
            String idUsuario,
            int idEquipo) throws SQLException {

        String sql = """
                DELETE FROM miembros_equipos
                WHERE id_usuario = ?
                  AND id_equipo = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);
            sentencia.setInt(2, idEquipo);

            return sentencia.executeUpdate() > 0;
        }
    }

    public List<Pair<Integer, String>> listarMisEquipos(
            String idUsuario) throws SQLException {

        List<Pair<Integer, String>> equipos = new ArrayList<>();

        String sql = """
                SELECT e.id_equipo, e.nombre_equipo, l.nombre_liga, l.deporte
                FROM miembros_equipos m
                INNER JOIN equipos e ON e.id_equipo = m.id_equipo
                INNER JOIN ligas l ON l.id_liga = e.id_liga
                WHERE m.id_usuario = ?
                ORDER BY e.nombre_equipo
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);

            try (ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {

                    equipos.add(
                            new Pair<>(
                                    resultado.getInt("id_equipo"),
                                    resultado.getString("nombre_equipo")
                                    + "  —  Liga: "
                                    + resultado.getString("nombre_liga")
                                    + "  —  Deporte: "
                                    + resultado.getString("deporte")
                            )
                    );
                }
            }
        }

        return equipos;
    }

    public List<Torneo> listarTorneosInscritos(
            String idUsuario) throws SQLException {

        List<Torneo> torneos = new ArrayList<>();

        String sql = """
                SELECT t.id_torneo,
                       t.id_liga,
                       t.nombre_torneo,
                       t.fecha_inicio,
                       t.fecha_fin,
                       t.estado,
                       l.nombre_liga
                FROM inscripciones_torneos i
                INNER JOIN torneos t ON t.id_torneo = i.id_torneo
                INNER JOIN ligas l ON l.id_liga = t.id_liga
                WHERE i.id_usuario = ?
                ORDER BY t.nombre_torneo
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);

            try (ResultSet resultado = sentencia.executeQuery()) {

                while (resultado.next()) {

                    torneos.add(
                            new Torneo(
                                    resultado.getInt("id_torneo"),
                                    resultado.getInt("id_liga"),
                                    resultado.getString("nombre_torneo"),
                                    resultado.getDate("fecha_inicio").toLocalDate(),
                                    resultado.getDate("fecha_fin").toLocalDate(),
                                    resultado.getString("estado"),
                                    resultado.getString("nombre_liga")
                            )
                    );
                }
            }
        }

        return torneos;
    }

    public List<Pair<Integer, String>> listarEquipos()
            throws SQLException {

        List<Pair<Integer, String>> equipos = new ArrayList<>();

        String sql = """
                SELECT id_equipo, nombre_equipo
                FROM equipos
                ORDER BY nombre_equipo
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                equipos.add(
                        new Pair<>(
                                resultado.getInt("id_equipo"),
                                resultado.getString("nombre_equipo")
                        )
                );
            }
        }

        return equipos;
    }

    public List<Pair<Integer, String>> listarTorneosDisponibles()
            throws SQLException {

        List<Pair<Integer, String>> torneos = new ArrayList<>();

        String sql = """
                SELECT id_torneo, nombre_torneo
                FROM torneos
                WHERE estado = 'activo'
                ORDER BY nombre_torneo
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {

                torneos.add(
                        new Pair<>(
                                resultado.getInt("id_torneo"),
                                resultado.getString("nombre_torneo")
                        )
                );
            }
        }

        return torneos;
    }

    public boolean inscribirTorneo(
            String idUsuario,
            int idTorneo) throws SQLException {

        String sql = """
                INSERT INTO inscripciones_torneos
                (id_usuario, id_torneo, fecha_inscripcion)
                VALUES (?, ?, CURRENT_DATE)
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);
            sentencia.setInt(2, idTorneo);

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean cancelarInscripcion(
            String idUsuario,
            int idTorneo) throws SQLException {

        String sql = """
                DELETE FROM inscripciones_torneos
                WHERE id_usuario = ?
                  AND id_torneo = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);
            sentencia.setInt(2, idTorneo);

            return sentencia.executeUpdate() > 0;
        }
    }

    public boolean estaEnEquipo(
            String idUsuario,
            int idEquipo) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM miembros_equipos
                WHERE id_usuario = ?
                  AND id_equipo = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);
            sentencia.setInt(2, idEquipo);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public boolean estaInscritoTorneo(
            String idUsuario,
            int idTorneo) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM inscripciones_torneos
                WHERE id_usuario = ?
                  AND id_torneo = ?
                """;

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement sentencia = connection.prepareStatement(sql)) {

            sentencia.setString(1, idUsuario);
            sentencia.setInt(2, idTorneo);

            try (ResultSet resultado = sentencia.executeQuery()) {

                if (resultado.next()) {
                    return resultado.getInt(1) > 0;
                }
            }
        }

        return false;
    }
}