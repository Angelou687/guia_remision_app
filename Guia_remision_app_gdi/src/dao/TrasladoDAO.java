package dao;

import db.Conexion;
import model.Traslado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrasladoDAO {

    public boolean registrarTraslado(Traslado t) {
        // Llamar al procedimiento que inserta el traslado (disparará trg_traslado_ai)
        String call = "{ CALL sp_registrar_traslado(?, ?, ?, ?, ?, ?, ?, ?) }";
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

            int updated = cs.executeUpdate();
            return updated >= 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar traslado (CALL): " + e.getMessage());
            return false;
        }
    }

    // listar usando SP sp_listar_traslados()
    public List<Traslado> listarTodos() {
        List<Traslado> lista = new ArrayList<>();
        String call = "{ CALL sp_listar_traslados() }";

        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call);
             ResultSet rs = cs.executeQuery()) {

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
            System.out.println("Error al listar traslados (CALL): " + e.getMessage());
        }

        return lista;
    }
}
