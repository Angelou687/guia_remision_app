package dao;

import db.Conexion;
import model.Traslado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrasladoDAO {

    public boolean registrarTraslado(Traslado t) {
        String sql = "INSERT INTO traslado " +
                "(codigo_traslado, codigo_guia, placa, licencia, fecha_inicio, fecha_fin, estado_traslado, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, t.getCodigoTraslado());
            ps.setString(2, t.getCodigoGuia());
            ps.setString(3, t.getPlaca());
            ps.setString(4, t.getLicencia());
            ps.setTimestamp(5, t.getFechaInicio());
            ps.setTimestamp(6, t.getFechaFin());
            ps.setString(7, t.getEstadoTraslado());
            ps.setString(8, t.getObservaciones());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar traslado: " + e.getMessage());
            return false;
        }
    }

    public List<Traslado> listarTodos() {
        List<Traslado> lista = new ArrayList<>();
        String sql = "SELECT codigo_traslado, codigo_guia, placa, licencia, fecha_inicio, " +
                     "fecha_fin, estado_traslado, observaciones FROM traslado";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Traslado t = new Traslado();
                t.setCodigoTraslado(rs.getString("codigo_traslado"));
                t.setCodigoGuia(rs.getString("codigo_guia"));
                t.setPlaca(rs.getString("placa"));
                t.setLicencia(rs.getString("licencia"));
                t.setFechaInicio(rs.getTimestamp("fecha_inicio"));
                t.setFechaFin(rs.getTimestamp("fecha_fin"));
                t.setEstadoTraslado(rs.getString("estado_traslado"));
                t.setObservaciones(rs.getString("observaciones"));
                lista.add(t);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar traslados: " + e.getMessage());
        }

        return lista;
    }
}
