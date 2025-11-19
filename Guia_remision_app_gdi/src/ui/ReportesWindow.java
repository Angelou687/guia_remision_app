package ui;

import dao.ReporteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReportesWindow extends JFrame {

    private ReporteDAO reporteDAO = new ReporteDAO();

    public ReportesWindow() {
        setTitle("Reportes");
        setSize(900, 500);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] opciones = {
                "1) Detalle de una orden (JOIN)",
                "2) Guías por fecha y estado",
                "3) Productos más vendidos (JOIN)",
                "4) Utilización de vehículos (JOIN)",
                "5) Licencias por vencer (JOIN)",
                "6) Guías sin traslado (JOIN)",
                "7) Bultos por cliente 90d (JOIN)",
                "8) KPI diario de guías (JOIN)",
                "9) Clientes sin compras 60d (JOIN)",
                "10) Traslados con vehículo y guía (JOIN)"
        };
        JComboBox<String> cbReporte = new JComboBox<>(opciones);
        JButton btnEjecutar = new JButton("Ejecutar reporte");
        JTextField txtParametro = new JTextField(10);
        JLabel lblParam = new JLabel("Parámetro (según reporte):");

        top.add(new JLabel("Reporte:"));
        top.add(cbReporte);
        top.add(lblParam);
        top.add(txtParametro);
        top.add(btnEjecutar);

        DefaultTableModel model = new DefaultTableModel();
        JTable tabla = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabla);

        root.add(top, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);

        btnEjecutar.addActionListener(e -> {
            int idx = cbReporte.getSelectedIndex();
            DefaultTableModel tm;
            switch (idx) {
                case 0:
                    String codOrden = txtParametro.getText().trim();
                    if (codOrden.isEmpty()) { JOptionPane.showMessageDialog(this, "Ingrese código de orden, ej: ORD0001"); return; }
                    tm = reporteDAO.ejecutarSPConParametro("sp_reporte_detalle_orden", codOrden);
                    break;
                case 1:
                    tm = reporteDAO.ejecutarSP("sp_reporte_guias_por_fecha_estado()");
                    break;
                case 2:
                    tm = reporteDAO.ejecutarSP("sp_reporte_productos_mas_vendidos()");
                    break;
                case 3:
                    tm = reporteDAO.ejecutarSP("sp_reporte_utilizacion_vehiculos()");
                    break;
                case 4:
                    String dias = txtParametro.getText().trim(); if (dias.isEmpty()) dias = "180";
                    tm = reporteDAO.ejecutarSPConParametro("sp_reporte_licencias_por_vencer", dias);
                    break;
                case 5:
                    tm = reporteDAO.ejecutarSP("sp_reporte_guias_sin_traslado()");
                    break;
                case 6:
                    tm = reporteDAO.ejecutarSP("sp_reporte_bultos_por_cliente_90d()");
                    break;
                case 7:
                    tm = reporteDAO.ejecutarSP("sp_reporte_kpi_guias_diario()");
                    break;
                case 8:
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT d.ruc, d.nombre " +
                                    "FROM destinatario d " +
                                    "WHERE d.ruc NOT IN ( " +
                                    "  SELECT o.ruc_cliente " +
                                    "  FROM orden_de_pago o " +
                                    "  WHERE o.fecha >= DATE_SUB(CURDATE(), INTERVAL 60 DAY) " +
                                    ") " +
                                    "ORDER BY d.nombre"
                    );
                    break;
                case 9:
                    tm = reporteDAO.ejecutarSP("sp_listar_traslados()");
                    break;
                default:
                    tm = new DefaultTableModel();
            }
            tabla.setModel(tm);
        });

        add(root);
    }
}
