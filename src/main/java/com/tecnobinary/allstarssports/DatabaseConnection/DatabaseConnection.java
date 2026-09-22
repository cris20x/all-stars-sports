package main.java.com.tecnobinary.allstarssports.DatabaseConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
 public static Connection getConnection(){
try{
    Connection connection = DriverManager.getConnection(Credentials.getURL(), Credentials.getUSER(), Credentials.getPASSWORD());
    return connection;
}catch(SQLException e){
    System.err.println(e.getMessage());
return null;
}
}
}
