package main.java.com.tecnobinary.allstarssports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import main.java.com.tecnobinary.allstarssports.DatabaseConnection.DatabaseConnection;
import main.java.com.tecnobinary.allstarssports.dto.request.LoginDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.request.RegisterDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.response.LoginDTOResponse;
import main.java.com.tecnobinary.allstarssports.security.jbcrypt.BCrypt;

public class AuthRepository {

    public LoginDTOResponse findUsuarioByEmail(
            LoginDTORequest request) {
        String sql =
                "SELECT u.id_usuario, u.nombre, u.apellido, "
                + "u.password_hash, u.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE u.email = ?";

        try (Connection connection =
                DatabaseConnection.getConnection();
                PreparedStatement pstm =
                connection.prepareStatement(sql)) {
            pstm.setString(
                    1,
                    request.getEmail());

            ResultSet rs =
                    pstm.executeQuery();

            if (rs.next()) {
                return new LoginDTOResponse(
                        rs.getString("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("password_hash"),
                        rs.getInt("id_rol"),
                        rs.getString("nombre_rol")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo consultar el usuario.",
                    e
            );
        }

        return null;
    }
    public boolean existeEmail(
            String email) {
        String sql =
                "SELECT id_usuario "
                + "FROM usuarios "
                + "WHERE email = ?";
        try (Connection connection =
                DatabaseConnection.getConnection();
                PreparedStatement pstm =
                connection.prepareStatement(sql)) {
            pstm.setString(
                    1,
                    email);

            ResultSet rs =
                    pstm.executeQuery();

            return rs.next();
        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo comprobar el correo.",
                    e
            );
        }
    }

    public boolean verificarClaveManager(
            String clave) {
        String sql =
                "SELECT verificacion_rol "
                + "FROM roles "
                + "WHERE id_rol = 1";
        try (Connection connection =
                DatabaseConnection.getConnection();
                PreparedStatement pstm =
                connection.prepareStatement(sql)) {
            ResultSet rs =
                    pstm.executeQuery();
            if (rs.next()) {
                String hashGuardado =
                        rs.getString(
                                "verificacion_rol");
                if (hashGuardado == null
                        || hashGuardado.isBlank()) {
                    return false;
                }
                return BCrypt.checkpw(
                        clave,
                        hashGuardado
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo verificar la autorización de Manager.",
                    e
            );
        }

        return false;
    }

    public void registrarUsuario(
            RegisterDTORequest request,
            String passwordHash,
            String claveRecuperacionHash) {
        String sql =
                "INSERT INTO usuarios "
                + "(id_rol, id_usuario, nombre, apellido, email, "
                + "password_hash, clave_recuperacion_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                DatabaseConnection.getConnection();
                PreparedStatement pstm =
                connection.prepareStatement(sql)) {

            String idUsuario =
                    UUID.randomUUID().toString();

            pstm.setInt(
                    1,
                    request.getIdRol());

            pstm.setString(
                    2,
                    idUsuario);

            pstm.setString(
                    3,
                    request.getNombre());

            pstm.setString(
                    4,
                    request.getApellido());

            pstm.setString(
                    5,
                    request.getEmail());

            pstm.setString(
                    6,
                    passwordHash);

            pstm.setString(
                    7,
                    claveRecuperacionHash);

            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo registrar el usuario.",
                    e
            );
        }
    }

    public boolean verificarClaveRecuperacion(
            String email,
            String claveRecuperacion) {
        String sql =
                "SELECT clave_recuperacion_hash "
                + "FROM usuarios "
                + "WHERE email = ?";
        try (Connection connection =
                DatabaseConnection.getConnection();
                PreparedStatement pstm =
                connection.prepareStatement(sql)) {

            pstm.setString(
                    1,
                    email);

            ResultSet rs =
                    pstm.executeQuery();

            if (rs.next()) {
                String hashGuardado =
                        rs.getString(
                                "clave_recuperacion_hash");

                if (hashGuardado == null
                        || hashGuardado.isBlank()) {
                    return false;
                }
                return BCrypt.checkpw(
                        claveRecuperacion,
                        hashGuardado
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo verificar la clave de recuperación.",
                    e
            );
        }
        return false;
    }

    public void actualizarPassword(
            String email,
            String nuevaPasswordHash) {
        String sql =
                "UPDATE usuarios "
                + "SET password_hash = ? "
                + "WHERE email = ?";
        try (Connection connection =
                DatabaseConnection.getConnection();
                PreparedStatement pstm =
                connection.prepareStatement(sql)) {
            pstm.setString(
                    1,
                    nuevaPasswordHash);

            pstm.setString(
                    2,
                    email);
            int filasActualizadas =
                    pstm.executeUpdate();
            if (filasActualizadas == 0) {
                throw new RuntimeException(
                        "No se pudo actualizar la contraseña.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo actualizar la contraseña.",
                    e
            );
        }
    }
}