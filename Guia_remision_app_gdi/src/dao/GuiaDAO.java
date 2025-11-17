package dao;

import db.Conexion;
import model.CabeceraGuia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuiaDAO {

    public List<CabeceraGuia> listarTodas() {
        List<CabeceraGuia> lista = new ArrayList<>();
        String sql = "SELECT codigo_guia, serie, numero, cod_orden, ruc_remitente, " +
                     "fecha_emision, hora_emision, estado_guia " +
                     "FROM cabecera_guia";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CabeceraGuia g = new CabeceraGuia();
                g.setCodigoGuia(rs.getString("codigo_guia"));
                g.setSerie(rs.getString("serie"));
                g.setNumero(rs.getString("numero"));
                g.setCodOrden(rs.getString("cod_orden"));
                g.setRucRemitente(rs.getString("ruc_remitente"));
                g.setFechaEmision(rs.getDate("fecha_emision"));
                g.setHoraEmision(rs.getTime("hora_emision"));
                g.setEstadoGuia(rs.getString("estado_guia"));
                lista.add(g);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar guias: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstadoGuia(String codigoGuia, String nuevoEstado) {
        String sql = "UPDATE cabecera_guia SET estado_guia = ? WHERE codigo_guia = ?";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setString(2, codigoGuia);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de guia: " + e.getMessage());
            return false;
        }
    }
}
