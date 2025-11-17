package dao;

import db.Conexion;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class ReporteDAO {

    // Ejecuta una consulta SELECT sin parámetros y devuelve un TableModel
    public DefaultTableModel ejecutarConsulta(String sql) {
        DefaultTableModel model = new DefaultTableModel();

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();

            // Encabezados
            for (int i = 1; i <= columnas; i++) {
                model.addColumn(meta.getColumnLabel(i));
            }

            // Filas
            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                model.addRow(fila);
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar reporte: " + e.getMessage());
        }

        return model;
    }

    // Versión con un parámetro (por ejemplo, código de orden)
    public DefaultTableModel ejecutarConsultaConParametro(String sql, String param) {
        DefaultTableModel model = new DefaultTableModel();

        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, param);

            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();

                for (int i = 1; i <= columnas; i++) {
                    model.addColumn(meta.getColumnLabel(i));
                }

                while (rs.next()) {
                    Object[] fila = new Object[columnas];
                    for (int i = 0; i < columnas; i++) {
                        fila[i] = rs.getObject(i + 1);
                    }
                    model.addRow(fila);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar reporte con parametro: " + e.getMessage());
        }

        return model;
    }

    // Nuevo: ejecutar SP sin parámetros (ej: sp_listar_traslados, sp_reporte_guias_por_fecha_estado)
    public DefaultTableModel ejecutarSP(String spName) {
        DefaultTableModel model = new DefaultTableModel();
        String call = "{ CALL " + spName + " }"; // spName por ejemplo: sp_listar_traslados()

        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call);
             ResultSet rs = cs.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            int columnas = meta.getColumnCount();
            for (int i = 1; i <= columnas; i++) model.addColumn(meta.getColumnLabel(i));

            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) fila[i] = rs.getObject(i + 1);
                model.addRow(fila);
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar SP " + spName + ": " + e.getMessage());
        }

        return model;
    }

    // Nuevo: ejecutar SP con 1 parámetro (ej: sp_reporte_detalle_orden, sp_reporte_licencias_por_vencer)
    public DefaultTableModel ejecutarSPConParametro(String spName, String param) {
        DefaultTableModel model = new DefaultTableModel();
        String call = "{ CALL " + spName + "(?) }"; // spName por ejemplo: sp_reporte_detalle_orden

        try (Connection cn = Conexion.getConnection();
             CallableStatement cs = cn.prepareCall(call)) {

            cs.setString(1, param);
            try (ResultSet rs = cs.executeQuery()) {
                ResultSetMetaData meta = rs.getMetaData();
                int columnas = meta.getColumnCount();
                for (int i = 1; i <= columnas; i++) model.addColumn(meta.getColumnLabel(i));

                while (rs.next()) {
                    Object[] fila = new Object[columnas];
                    for (int i = 0; i < columnas; i++) fila[i] = rs.getObject(i + 1);
                    model.addRow(fila);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al ejecutar SP con param " + spName + ": " + e.getMessage());
        }

        return model;
    }
}
