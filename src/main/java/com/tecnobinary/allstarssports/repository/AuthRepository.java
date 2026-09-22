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

    public LoginDTOResponse findUsuarioByEmail(LoginDTORequest request) {

        String sql = "SELECT u.id_usuario, u.nombre, u.apellido, "
                + "u.password_hash, u.id_rol, r.nombre_rol "
                + "FROM usuarios u "
                + "INNER JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE u.email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, request.getEmail());

            ResultSet rs = pstm.executeQuery();

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
            System.err.println(e.getMessage());
        }

        return null;
    }

    public boolean existeEmail(String email) {

        String sql = "SELECT id_usuario "
                + "FROM usuarios "
                + "WHERE email = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            pstm.setString(1, email);

            ResultSet rs = pstm.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean verificarClaveManager(String clave) {

        String sql = "SELECT verificacion_rol "
                + "FROM roles "
                + "WHERE id_rol = 1";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {

                String hashGuardado =
                        rs.getString("verificacion_rol");

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
            System.err.println(e.getMessage());
        }

        return false;
    }

    public void registrarUsuario(
            RegisterDTORequest request,
            String passwordHash,
            String claveRecuperacionHash) {

        String sql = "INSERT INTO usuarios "
                + "(id_rol, id_usuario, nombre, apellido, email, "
                + "password_hash, clave_recuperacion_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement pstm = connection.prepareStatement(sql)) {

            String idUsuario =
                    UUID.randomUUID().toString();

            pstm.setInt(
                    1,
                    request.getIdRol()
            );

            pstm.setString(
                    2,
                    idUsuario
            );

            pstm.setString(
                    3,
                    request.getNombre()
            );

            pstm.setString(
                    4,
                    request.getApellido()
            );

            pstm.setString(
                    5,
                    request.getEmail()
            );

            pstm.setString(
                    6,
                    passwordHash
            );

            pstm.setString(
                    7,
                    claveRecuperacionHash
            );

            pstm.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "No se pudo registrar el usuario."
            );
        }
    }
}