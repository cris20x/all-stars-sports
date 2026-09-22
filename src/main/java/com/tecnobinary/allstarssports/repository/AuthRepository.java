package main.java.com.tecnobinary.allstarssports.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.com.tecnobinary.allstarssports.DatabaseConnection.DatabaseConnection;
import main.java.com.tecnobinary.allstarssports.dto.request.LoginDTORequest;
import main.java.com.tecnobinary.allstarssports.dto.response.LoginDTOResponse;

public class AuthRepository {

    public LoginDTOResponse findUsuarioByEmail(LoginDTORequest request) {

        String sql = "select u.id_usuario, u.nombre, u.apellido, u.password_hash, u.id_rol, r.nombre_rol "
                + "from usuarios u inner join roles r on u.id_rol = r.id_rol where u.email = ?";

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

}
