import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/guia_remision?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "angel"; // pon aquí tu contraseña real de MySQL

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexion exitosa a guia_remision");
        } catch (SQLException e) {
            System.out.println("Error de conexion: " + e.getMessage());
        }
    }
}
