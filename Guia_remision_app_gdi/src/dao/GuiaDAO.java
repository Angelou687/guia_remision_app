package dao;

import db.Conexion;
import model.CabeceraGuia;
import model.DetalleGuia;

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

    // Nuevo: emitir guía usando el PROCEDURE sp_emitir_guia
    public boolean emitirGuia(String codigoGuia,
                              String serie,
                              String numero,
                              String codOrden,
                              String rucRemitente,
                              String rucDestinatario,
                              String dirPartida,
                              String dirLlegada,
                              String ubigeoOrigen,
                              String ubigeoDestino,
                              String motivo,
                              String modalidad,
                              double pesoTotal,
                              int numeroBultos) {
        String call = "{ CALL sp_emitir_guia(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            cs.setString(1, codigoGuia);
            cs.setString(2, serie);
            cs.setString(3, numero);
            cs.setString(4, codOrden);
            cs.setString(5, rucRemitente);
            cs.setString(6, rucDestinatario);
            cs.setString(7, dirPartida);
            cs.setString(8, dirLlegada);
            cs.setString(9, ubigeoOrigen);
            cs.setString(10, ubigeoDestino);
            cs.setString(11, motivo);
            cs.setString(12, modalidad);
            cs.setDouble(13, pesoTotal);
            cs.setInt(14, numeroBultos);

            int updated = cs.executeUpdate();
            return updated >= 0;

        } catch (SQLException e) {
            System.out.println("Error al emitir guía (CALL): " + e.getMessage());
            return false;
        }
    }

    // eliminar guía por codigo
    public boolean eliminar(String codigoGuia) {
        String sql = "DELETE FROM cabecera_guia WHERE codigo_guia = ?";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigoGuia);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar guía: " + e.getMessage());
            return false;
        }
    }

    /**
     * Recupera los detalles (líneas) asociados a una guía.
     * Ajusta los nombres de columnas según tu esquema si difieren.
     */
    public List<DetalleGuia> listarDetallePorGuia(String codigoGuia) {
        List<DetalleGuia> lista = new ArrayList<>();
        String sql = "SELECT nro_item, bien_normalizado, codigo_bien, codigo_producto_sunat, partida_arancelaria, " +
                     "codigo_gtin, descripcion, unidad_medida, cantidad " +
                     "FROM detalle_guia WHERE codigo_guia = ? ORDER BY nro_item";

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, codigoGuia);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int nro = rs.getInt("nro_item");
                    String bien = rs.getString("bien_normalizado");
                    String codBien = rs.getString("codigo_bien");
                    String codProd = rs.getString("codigo_producto_sunat");
                    String partida = rs.getString("partida_arancelaria");
                    String gtin = rs.getString("codigo_gtin");
                    String desc = rs.getString("descripcion");
                    String unidad = rs.getString("unidad_medida");
                    String cantidad = rs.getString("cantidad");
                    DetalleGuia d = new DetalleGuia(nro, bien, codBien, codProd, partida, gtin, desc, unidad, cantidad);
                    lista.add(d);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al listar detalle de guía: " + e.getMessage());
        }

        return lista;
    }
}
