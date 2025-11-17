import dao.DestinatarioDAO;
import dao.GuiaDAO;
import dao.TrasladoDAO;
import dao.ReporteDAO;
import model.Destinatario;
import model.CabeceraGuia;
import model.Traslado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.Timestamp;
import java.util.List;

public class MainApp extends JFrame {

    private JTabbedPane tabs;

    // Paneles
    private JPanel panelDestinatarios;
    private JPanel panelGuias;
    private JPanel panelTraslados;
    private JPanel panelReportes;

    // DAO
    private DestinatarioDAO destinatarioDAO = new DestinatarioDAO();
    private GuiaDAO guiaDAO = new GuiaDAO();
    private TrasladoDAO trasladoDAO = new TrasladoDAO();
    private ReporteDAO reporteDAO = new ReporteDAO();

    public MainApp() {
        setTitle("Guía de Remisión - RED LIPA (Java)");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabs = new JTabbedPane();

        panelDestinatarios = crearPanelDestinatarios();
        panelGuias        = crearPanelGuias();
        panelTraslados    = crearPanelTraslados();
        panelReportes     = crearPanelReportes();

        tabs.addTab("Destinatarios", panelDestinatarios);
        tabs.addTab("Guías", panelGuias);
        tabs.addTab("Traslados", panelTraslados);
        tabs.addTab("Reportes", panelReportes);

        add(tabs, BorderLayout.CENTER);
    }

    // =========================
    // PANEL DESTINATARIOS (CRUD)
    // =========================
    private JPanel crearPanelDestinatarios() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Tabla
        String[] columnas = {"RUC", "Nombre", "Teléfono", "Dirección", "Ubigeo", "Gmail"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabla);

        // Formulario
        JPanel form = new JPanel(new GridLayout(7, 2, 5, 5));
        JLabel lblRuc     = new JLabel("RUC:");
        JLabel lblNombre  = new JLabel("Nombre:");
        JLabel lblTel     = new JLabel("Teléfono:");
        JLabel lblDir     = new JLabel("Dirección:");
        JLabel lblUbigeo  = new JLabel("Código Ubigeo:");
        JLabel lblGmail   = new JLabel("Gmail:");

        JTextField txtRuc    = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtTel    = new JTextField();
        JTextField txtDir    = new JTextField();
        JTextField txtUbigeo = new JTextField();
        JTextField txtGmail  = new JTextField();

        form.add(lblRuc);    form.add(txtRuc);
        form.add(lblNombre); form.add(txtNombre);
        form.add(lblTel);    form.add(txtTel);
        form.add(lblDir);    form.add(txtDir);
        form.add(lblUbigeo); form.add(txtUbigeo);
        form.add(lblGmail);  form.add(txtGmail);

        JButton btnNuevo   = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar  = new JButton("Limpiar");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(btnNuevo);
        acciones.add(btnGuardar);
        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnLimpiar);

        JPanel derecha = new JPanel(new BorderLayout(5,5));
        derecha.add(form, BorderLayout.CENTER);
        derecha.add(acciones, BorderLayout.SOUTH);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        // Cargar datos
        cargarDestinatariosEnTabla(model);

        // Eventos
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtRuc.setText(model.getValueAt(fila,0).toString());
                    txtNombre.setText(model.getValueAt(fila,1).toString());
                    txtTel.setText(model.getValueAt(fila,2) != null ? model.getValueAt(fila,2).toString() : "");
                    txtDir.setText(model.getValueAt(fila,3).toString());
                    txtUbigeo.setText(model.getValueAt(fila,4).toString());
                    txtGmail.setText(model.getValueAt(fila,5) != null ? model.getValueAt(fila,5).toString() : "");
                    txtRuc.setEnabled(false);
                }
            }
        });

        btnNuevo.addActionListener(e -> {
            limpiarDestinatarioForm(txtRuc, txtNombre, txtTel, txtDir, txtUbigeo, txtGmail);
            txtRuc.setEnabled(true);
        });

        btnGuardar.addActionListener(e -> {
            String ruc    = txtRuc.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tel    = txtTel.getText().trim();
            String dir    = txtDir.getText().trim();
            String ubigeo = txtUbigeo.getText().trim();
            String gmail  = txtGmail.getText().trim();

            if (ruc.isEmpty() || nombre.isEmpty() || dir.isEmpty() || ubigeo.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "RUC, Nombre, Dirección y Ubigeo son obligatorios");
                return;
            }

            Destinatario d = new Destinatario(ruc, nombre, tel, dir, ubigeo, gmail);
            if (destinatarioDAO.insertar(d)) {
                JOptionPane.showMessageDialog(panel, "Destinatario insertado correctamente");
                cargarDestinatariosEnTabla(model);
                limpiarDestinatarioForm(txtRuc, txtNombre, txtTel, txtDir, txtUbigeo, txtGmail);
            } else {
                JOptionPane.showMessageDialog(panel, "Error al insertar destinatario");
            }
        });

        btnActualizar.addActionListener(e -> {
            String ruc    = txtRuc.getText().trim();
            if (ruc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Seleccione un destinatario de la tabla");
                return;
            }

            String nombre = txtNombre.getText().trim();
            String tel    = txtTel.getText().trim();
            String dir    = txtDir.getText().trim();
            String ubigeo = txtUbigeo.getText().trim();
            String gmail  = txtGmail.getText().trim();

            Destinatario d = new Destinatario(ruc, nombre, tel, dir, ubigeo, gmail);
            if (destinatarioDAO.actualizar(d)) {
                JOptionPane.showMessageDialog(panel, "Destinatario actualizado correctamente");
                cargarDestinatariosEnTabla(model);
            } else {
                JOptionPane.showMessageDialog(panel, "Error al actualizar destinatario");
            }
        });

        btnEliminar.addActionListener(e -> {
            String ruc = txtRuc.getText().trim();
            if (ruc.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Seleccione un destinatario de la tabla");
                return;
            }
            int resp = JOptionPane.showConfirmDialog(panel,
                    "¿Seguro que desea eliminar el destinatario con RUC " + ruc + "?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                if (destinatarioDAO.eliminar(ruc)) {
                    JOptionPane.showMessageDialog(panel, "Destinatario eliminado");
                    cargarDestinatariosEnTabla(model);
                    limpiarDestinatarioForm(txtRuc, txtNombre, txtTel, txtDir, txtUbigeo, txtGmail);
                } else {
                    JOptionPane.showMessageDialog(panel, "Error al eliminar destinatario");
                }
            }
        });

        btnLimpiar.addActionListener(e -> {
            limpiarDestinatarioForm(txtRuc, txtNombre, txtTel, txtDir, txtUbigeo, txtGmail);
            txtRuc.setEnabled(true);
        });

        return panel;
    }

    private void cargarDestinatariosEnTabla(DefaultTableModel model) {
        model.setRowCount(0);
        List<Destinatario> lista = destinatarioDAO.listarTodos();
        for (Destinatario d : lista) {
            model.addRow(new Object[]{
                    d.getRuc(),
                    d.getNombre(),
                    d.getNumeroTelefono(),
                    d.getCalleDireccion(),
                    d.getCodigoUbigeo(),
                    d.getGmail()
            });
        }
    }

    private void limpiarDestinatarioForm(JTextField txtRuc, JTextField txtNombre,
                                         JTextField txtTel, JTextField txtDir,
                                         JTextField txtUbigeo, JTextField txtGmail) {
        txtRuc.setText("");
        txtNombre.setText("");
        txtTel.setText("");
        txtDir.setText("");
        txtUbigeo.setText("");
        txtGmail.setText("");
    }

    // =========================
    // PANEL GUIAS (ESTADO)
    // =========================
    private JPanel crearPanelGuias() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblGuia = new JLabel("Guía:");
        JComboBox<CabeceraGuia> cbGuias = new JComboBox<>();

        JButton btnRefrescar = new JButton("Refrescar");
        JLabel lblEstado = new JLabel("Nuevo estado:");
        String[] estados = {"emitida", "en tránsito", "entregada"};
        JComboBox<String> cbEstado = new JComboBox<>(estados);
        JButton btnActualizar = new JButton("Actualizar estado");

        top.add(lblGuia);
        top.add(cbGuias);
        top.add(btnRefrescar);
        top.add(lblEstado);
        top.add(cbEstado);
        top.add(btnActualizar);

        // Tabla de guías
        String[] cols = {"Código", "Serie", "Número", "Orden", "Remitente", "Fecha", "Hora", "Estado"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabla);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        // Cargar
        cargarGuias(cbGuias, model);

        btnRefrescar.addActionListener(e -> cargarGuias(cbGuias, model));

        btnActualizar.addActionListener(e -> {
            CabeceraGuia g = (CabeceraGuia) cbGuias.getSelectedItem();
            if (g == null) {
                JOptionPane.showMessageDialog(panel, "No hay guías seleccionadas");
                return;
            }
            String nuevo = (String) cbEstado.getSelectedItem();
            if (guiaDAO.actualizarEstadoGuia(g.getCodigoGuia(), nuevo)) {
                JOptionPane.showMessageDialog(panel, "Estado actualizado");
                cargarGuias(cbGuias, model);
            } else {
                JOptionPane.showMessageDialog(panel, "Error al actualizar estado");
            }
        });

        return panel;
    }

    private void cargarGuias(JComboBox<CabeceraGuia> combo, DefaultTableModel model) {
        combo.removeAllItems();
        model.setRowCount(0);
        List<CabeceraGuia> lista = guiaDAO.listarTodas();
        for (CabeceraGuia g : lista) {
            combo.addItem(g);
            model.addRow(new Object[]{
                    g.getCodigoGuia(),
                    g.getSerie(),
                    g.getNumero(),
                    g.getCodOrden(),
                    g.getRucRemitente(),
                    g.getFechaEmision(),
                    g.getHoraEmision(),
                    g.getEstadoGuia()
            });
        }
    }

    // =========================
    // PANEL TRASLADOS
    // =========================
    private JPanel crearPanelTraslados() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        String[] cols = {
                "Código traslado", "Guía", "Placa", "Licencia",
                "Inicio", "Fin", "Estado", "Obs."
        };
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable tabla = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel form = new JPanel(new GridLayout(9, 2, 5, 5));
        JTextField txtCodTr   = new JTextField();
        JTextField txtCodGuia = new JTextField();
        JTextField txtPlaca   = new JTextField();
        JTextField txtLic     = new JTextField();
        JTextField txtInicio  = new JTextField("2025-10-21 10:00:00");
        JTextField txtFin     = new JTextField("2025-10-21 16:00:00");
        JTextField txtEstado  = new JTextField("en tránsito");
        JTextArea  txtObs     = new JTextArea(3, 20);

        form.add(new JLabel("Código traslado:")); form.add(txtCodTr);
        form.add(new JLabel("Código guía:"));     form.add(txtCodGuia);
        form.add(new JLabel("Placa:"));           form.add(txtPlaca);
        form.add(new JLabel("Licencia:"));        form.add(txtLic);
        form.add(new JLabel("Fecha/hora inicio:")); form.add(txtInicio);
        form.add(new JLabel("Fecha/hora fin:"));    form.add(txtFin);
        form.add(new JLabel("Estado:"));            form.add(txtEstado);
        form.add(new JLabel("Observaciones:"));     form.add(new JScrollPane(txtObs));

        JButton btnRegistrar = new JButton("Registrar traslado");
        JButton btnRefrescar = new JButton("Refrescar lista");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(btnRegistrar);
        acciones.add(btnRefrescar);

        JPanel derecha = new JPanel(new BorderLayout(5,5));
        derecha.add(form, BorderLayout.CENTER);
        derecha.add(acciones, BorderLayout.SOUTH);

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(derecha, BorderLayout.EAST);

        cargarTraslados(model);

        btnRegistrar.addActionListener(e -> {
            try {
                String codTr   = txtCodTr.getText().trim();
                String codG    = txtCodGuia.getText().trim();
                String placa   = txtPlaca.getText().trim();
                String lic     = txtLic.getText().trim();
                String inicioS = txtInicio.getText().trim();
                String finS    = txtFin.getText().trim();
                String estado  = txtEstado.getText().trim();
                String obs     = txtObs.getText().trim();

                if (codTr.isEmpty() || codG.isEmpty() || placa.isEmpty() || lic.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Código traslado, guía, placa y licencia son obligatorios.");
                    return;
                }

                Timestamp inicio = Timestamp.valueOf(inicioS);
                Timestamp fin    = Timestamp.valueOf(finS);

                Traslado t = new Traslado(codTr, codG, placa, lic, inicio, fin, estado, obs);
                if (trasladoDAO.registrarTraslado(t)) {
                    JOptionPane.showMessageDialog(panel, "Traslado registrado correctamente");
                    cargarTraslados(model);
                } else {
                    JOptionPane.showMessageDialog(panel, "Error al registrar traslado");
                }

            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(panel, "Formato de fecha/hora incorrecto. Use: yyyy-MM-dd HH:mm:ss");
            }
        });

        btnRefrescar.addActionListener(e -> cargarTraslados(model));

        return panel;
    }

    private void cargarTraslados(DefaultTableModel model) {
        model.setRowCount(0);
        List<Traslado> lista = trasladoDAO.listarTodos();
        for (Traslado t : lista) {
            model.addRow(new Object[]{
                    t.getCodigoTraslado(),
                    t.getCodigoGuia(),
                    t.getPlaca(),
                    t.getLicencia(),
                    t.getFechaInicio(),
                    t.getFechaFin(),
                    t.getEstadoTraslado(),
                    t.getObservaciones()
            });
        }
    }

    // =========================
    // PANEL REPORTES
    // =========================
    private JPanel crearPanelReportes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

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

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        btnEjecutar.addActionListener(e -> {
            int idx = cbReporte.getSelectedIndex();
            DefaultTableModel tm;

            switch (idx) {
                case 0: // Detalle de orden, requiere codigo_orden
                    String codOrden = txtParametro.getText().trim();
                    if (codOrden.isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "Ingrese código de orden, ej: ORD0001");
                        return;
                    }
                    tm = reporteDAO.ejecutarConsultaConParametro(
                            "SELECT o.codigo_orden, p.codigo_producto, p.nombre_producto, " +
                                    "do.cantidad, do.precio_unitario, do.subtotal " +
                                    "FROM orden_de_pago o " +
                                    "JOIN detalle_orden do ON do.codigo_orden = o.codigo_orden " +
                                    "JOIN producto p       ON p.codigo_producto = do.codigo_producto " +
                                    "WHERE o.codigo_orden = ?",
                            codOrden
                    );
                    break;

                case 1: // Guías por fecha y estado
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT fecha_emision, estado_guia, COUNT(*) AS guias " +
                            "FROM cabecera_guia " +
                            "GROUP BY fecha_emision, estado_guia " +
                            "ORDER BY fecha_emision DESC, estado_guia"
                    );
                    break;

                case 2: // Productos más vendidos
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT p.codigo_producto, p.nombre_producto, " +
                            "SUM(do.cantidad) AS cantidad_vendida " +
                            "FROM producto p " +
                            "JOIN detalle_orden do ON do.codigo_producto = p.codigo_producto " +
                            "GROUP BY p.codigo_producto, p.nombre_producto " +
                            "ORDER BY cantidad_vendida DESC"
                    );
                    break;

                case 3: // Utilización de vehículos
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT v.placa, v.marca, v.modelo, " +
                            "COUNT(t.codigo_traslado) AS viajes " +
                            "FROM vehiculo v " +
                            "LEFT JOIN traslado t ON t.placa = v.placa " +
                            "GROUP BY v.placa, v.marca, v.modelo " +
                            "ORDER BY viajes DESC"
                    );
                    break;

                case 4: // Licencias por vencer (N días)
                    String dias = txtParametro.getText().trim();
                    if (dias.isEmpty()) dias = "180";
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT licencia, nombre, telefono, fecha_vencimiento_licencia " +
                            "FROM conductor " +
                            "WHERE fecha_vencimiento_licencia <= DATE_ADD(CURDATE(), INTERVAL " + dias + " DAY) " +
                            "ORDER BY fecha_vencimiento_licencia"
                    );
                    break;

                case 5: // Guías sin traslado
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT cg.codigo_guia, cg.fecha_emision, cg.estado_guia " +
                            "FROM cabecera_guia cg " +
                            "LEFT JOIN traslado t ON t.codigo_guia = cg.codigo_guia " +
                            "WHERE t.codigo_guia IS NULL " +
                            "ORDER BY cg.fecha_emision DESC"
                    );
                    break;

                case 6: // Bultos por cliente 90d
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT d.nombre AS cliente, SUM(cg.numero_bultos) AS bultos_90d " +
                            "FROM cuerpo_guia cg " +
                            "JOIN destinatario d ON d.ruc = cg.ruc_destinatario " +
                            "JOIN cabecera_guia cab ON cab.codigo_guia = cg.codigo_guia " +
                            "WHERE cab.fecha_emision >= DATE_SUB(CURDATE(), INTERVAL 90 DAY) " +
                            "GROUP BY d.nombre " +
                            "ORDER BY bultos_90d DESC"
                    );
                    break;

                case 7: // KPI diario
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT cab.fecha_emision, " +
                            "SUM(cab.estado_guia = 'emitida') AS emitidas, " +
                            "SUM(t.estado_traslado = 'en tránsito') AS en_transito, " +
                            "SUM(t.estado_traslado = 'entregado') AS entregadas " +
                            "FROM cabecera_guia cab " +
                            "LEFT JOIN traslado t ON t.codigo_guia = cab.codigo_guia " +
                            "GROUP BY cab.fecha_emision " +
                            "ORDER BY cab.fecha_emision DESC"
                    );
                    break;

                case 8: // Clientes sin compras 60 días
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

                case 9: // Traslados con vehículo y guía
                    tm = reporteDAO.ejecutarConsulta(
                            "SELECT t.codigo_traslado, t.codigo_guia, v.placa, v.tipo_vehiculo, " +
                            "t.fecha_inicio, t.fecha_fin, t.estado_traslado " +
                            "FROM traslado t " +
                            "JOIN vehiculo v ON v.placa = t.placa"
                    );
                    break;

                default:
                    tm = new DefaultTableModel();
                    break;
            }

            tabla.setModel(tm);
        });

        return panel;
    }

    // =========================
    // MAIN
    // =========================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp app = new MainApp();
            app.setVisible(true);
        });
    }
}
