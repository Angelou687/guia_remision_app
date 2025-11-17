package dao;

import db.Conexion;
import model.Destinatario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DestinatarioDAO {

    public List<Destinatario> listarTodos() {
        List<Destinatario> lista = new ArrayList<>();
        String sql = "SELECT ruc, nombre, numero_telefono, calle_direccion, codigo_ubigeo, gmail FROM destinatario";

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

    public boolean insertar(Destinatario d) {
        String sql = "INSERT INTO destinatario (ruc, nombre, numero_telefono, calle_direccion, codigo_ubigeo, gmail) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, d.getRuc());
            ps.setString(2, d.getNombre());
            ps.setString(3, d.getNumeroTelefono());
            ps.setString(4, d.getCalleDireccion());
            ps.setString(5, d.getCodigoUbigeo());
            ps.setString(6, d.getGmail());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar destinatario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Destinatario d) {
        String sql = "UPDATE destinatario SET nombre = ?, numero_telefono = ?, " +
                     "calle_direccion = ?, codigo_ubigeo = ?, gmail = ? WHERE ruc = ?";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, d.getNombre());
            ps.setString(2, d.getNumeroTelefono());
            ps.setString(3, d.getCalleDireccion());
            ps.setString(4, d.getCodigoUbigeo());
            ps.setString(5, d.getGmail());
            ps.setString(6, d.getRuc());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar destinatario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String ruc) {
        String sql = "DELETE FROM destinatario WHERE ruc = ?";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, ruc);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar destinatario: " + e.getMessage());
            return false;
        }
    }
}
