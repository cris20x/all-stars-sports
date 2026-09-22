package main.java.com.tecnobinary.allstarssports.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static Connection getConnection() {
        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );
            return DriverManager.getConnection(
                    Credentials.getURL(),
                    Credentials.getUSER(),
                    Credentials.getPASSWORD()
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "No se encontró el driver JDBC de MySQL.",
                    e
            );
        } catch (SQLException e) {
            throw new RuntimeException(
                    "No se pudo conectar a MySQL: "
                    + e.getMessage(),
                    e
            );
        }
    }
}