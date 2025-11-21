package dao;

import db.Conexion;
import model.Destinatario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinatarioDAO {

    // listar usando función/procedimiento que devuelve filas en Postgres:
    public List<Destinatario> listarTodos() {
        List<Destinatario> lista = new ArrayList<>();
        String sql = "SELECT * FROM sp_listar_destinatarios()"; // Postgres: función que devuelve SETOF

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Destinatario d = new Destinatario();
                d.setRuc(rs.getString("ruc"));
                d.setNombre(rs.getString("nombre"));
                d.setNumeroTelefono(rs.getString("numero_telefono"));
                d.setCalleDireccion(rs.getString("calle_direccion"));
                d.setCodigoUbigeo(rs.getString("codigo_ubigeo"));
                d.setGmail(rs.getString("gmail"));
                lista.add(d);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar destinatarios: " + e.getMessage());
        }

        return lista;
    }

    // insertar: usar CALL si es procedimiento (Postgres 11+), o SELECT si es función; aquí usamos CALL
    public boolean insertar(Destinatario d) {
        String call = "CALL sp_insertar_destinatario(?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            cs.setString(1, d.getRuc());
            cs.setString(2, d.getNombre());
            cs.setString(3, d.getNumeroTelefono());
            cs.setString(4, d.getCalleDireccion());
            cs.setString(5, d.getCodigoUbigeo());
            cs.setString(6, d.getGmail());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al insertar destinatario (CALL): " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Destinatario d) {
        String call = "CALL sp_actualizar_destinatario(?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            // Validar ubigeo
            String codigoUbigeo = d.getCodigoUbigeo();
            if (codigoUbigeo != null && !codigoUbigeo.trim().isEmpty()) {
                if (!ubigeoExiste(cn, codigoUbigeo)) {
                    System.out.println("Error al actualizar destinatario: el codigo_ubigeo '" + codigoUbigeo + "' no existe en la tabla ubigeo.");
                    return false;
                }
            }

            cs.setString(1, d.getRuc());
            cs.setString(2, d.getNombre());
            cs.setString(3, d.getNumeroTelefono());
            cs.setString(4, d.getCalleDireccion());
            cs.setString(5, d.getCodigoUbigeo());
            cs.setString(6, d.getGmail());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar destinatario (CALL): " + e.getMessage());
            return false;
        }
    }

    public boolean ubigeoExiste(String codigoUbigeo) {
        if (codigoUbigeo == null || codigoUbigeo.trim().isEmpty()) return false;
        try (Connection cn = Conexion.getConnection()) {
            return ubigeoExiste(cn, codigoUbigeo);
        } catch (SQLException e) {
            System.out.println("Error comprobando ubigeo: " + e.getMessage());
            return false;
        }
    }

    private boolean ubigeoExiste(Connection cn, String codigoUbigeo) throws SQLException {
        String sql = "SELECT 1 FROM ubigeo WHERE codigo_ubigeo = ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, codigoUbigeo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean eliminar(String ruc) {
        String call = "CALL sp_eliminar_destinatario(?)";

        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            cs.setString(1, ruc);
            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar destinatario (CALL): " + e.getMessage());
            return false;
        }
    }
}
