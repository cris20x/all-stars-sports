package main.java.com.tecnobinary.allstarssports.DatabaseConnection;

public class Credentials {

private static final String URL = "jdbc:mysql://localhost:3306/all_sports_league_in4bv";
private static final String USER = "root";
private static final String PASSWORD = "seratosaurio123";
 
public static String getURL(){
return URL;
}
public static String getUSER(){
return USER;
}
public static String getPASSWORD(){
return PASSWORD;
}
}

