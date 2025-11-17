package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL  = "jdbc:mysql://localhost:3306/guia_remision?useSSL=false&serverTimezone=America/Lima";
    private static final String USER = "root";              // <-- CAMBIA ESTO
    private static final String PASS = "angel";  // <-- CAMBIA ESTO

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Driver JDBC de MySQL
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo cargar el driver de MySQL: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
