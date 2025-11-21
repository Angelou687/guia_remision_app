package dao;

import db.Conexion;
import model.Traslado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrasladoDAO {

    public boolean registrarTraslado(Traslado t) {
        String call = "CALL sp_registrar_traslado(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            cs.setString(1, t.getCodigoTraslado());
            cs.setString(2, t.getCodigoGuia());
            cs.setString(3, t.getPlaca());
            cs.setString(4, t.getLicencia());
            cs.setTimestamp(5, t.getFechaInicio());
            cs.setTimestamp(6, t.getFechaFin());
            cs.setString(7, t.getEstadoTraslado());
            cs.setString(8, t.getObservaciones());

            cs.execute();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al registrar traslado (CALL): " + e.getMessage());
            return false;
        }
    }

    // listar usando función en Postgres que devuelve SETOF
    public List<Traslado> listarTodos() {
        List<Traslado> lista = new ArrayList<>();
        String sql = "SELECT * FROM sp_listar_traslados()";

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
