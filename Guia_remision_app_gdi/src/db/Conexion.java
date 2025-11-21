package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    // Ajusta host/puerto/nombre_basedatos/usuario/clave según tu entorno PostgreSQL
    private static final String URL  = "jdbc:postgresql://localhost:5432/guia_remision";
    private static final String USER = "postgres";    // <-- CAMBIA ESTO
    private static final String PASS = "root";    // <-- CAMBIA ESTO

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Driver JDBC de PostgreSQL
        } catch (ClassNotFoundException e) {
            System.out.println("No se pudo cargar el driver de PostgreSQL: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
