package ui;

import dao.GuiaDAO;
import dao.DestinatarioDAO;
import model.CabeceraGuia;
import model.DetalleGuia;
import util.GuiaRemisionGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GuiaWindow extends JFrame {
    private GuiaDAO dao = new GuiaDAO();
    private DefaultTableModel model;

    public GuiaWindow() {
        setTitle("Guías");
        setSize(900,520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyles.BG);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8,8)); root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8)); root.setBackground(UIStyles.BG);
        String[] cols = {"Código","Serie","Número","Orden","Remitente","Fecha","Hora","Estado"};
        model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model); table.setRowHeight(26);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setOpaque(false);
        JButton btnNuevo = new JButton("Nuevo"); UIStyles.styleButton(btnNuevo);
        JButton btnEditar = new JButton("Editar"); UIStyles.styleButton(btnEditar);
        JButton btnEliminar = new JButton("Eliminar"); UIStyles.styleButton(btnEliminar);
        JButton btnRefrescar = new JButton("Actualizar"); UIStyles.styleButton(btnRefrescar);
        JButton btnGenerarPdf = new JButton("Generar PDF"); UIStyles.styleButton(btnGenerarPdf);
        top.add(btnNuevo); top.add(btnEditar); top.add(btnEliminar); top.add(btnRefrescar); top.add(btnGenerarPdf);

        root.add(top, BorderLayout.NORTH); root.add(new JScrollPane(table), BorderLayout.CENTER);
        add(root);

        btnRefrescar.addActionListener(e -> load());
        btnNuevo.addActionListener(e -> new FrmGuia(this, null).setVisible(true));
        btnEditar.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione una fila"); return; }
            String codigo = model.getValueAt(r,0).toString();
            CabeceraGuia g = dao.listarTodas().stream().filter(x->x.getCodigoGuia().equals(codigo)).findFirst().orElse(null);
            if (g!=null) new FrmGuia(this, g).setVisible(true);
        });
        btnEliminar.addActionListener(e -> {
            int r = table.getSelectedRow(); if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione una fila"); return; }
            String codigo = model.getValueAt(r,0).toString();
            int opt = JOptionPane.showConfirmDialog(this, "Confirma eliminar la guía " + codigo + " ?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (opt != JOptionPane.YES_OPTION) return;
            boolean ok = dao.eliminar(codigo);
            if (ok) { JOptionPane.showMessageDialog(this, "Eliminado"); load(); }
            else { JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique dependencias y logs."); }
        });

        // Acción: generar PDF usando datos de la guía seleccionada
        btnGenerarPdf.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione una guía para generar el PDF"); return; }
            String codigo = model.getValueAt(r,0).toString();
            CabeceraGuia g = dao.listarTodas().stream().filter(x->x.getCodigoGuia().equals(codigo)).findFirst().orElse(null);
            if (g == null) { JOptionPane.showMessageDialog(this, "No se pudo cargar la guía seleccionada"); return; }

            // Obtener detalles (líneas)
            List<DetalleGuia> detalles = dao.listarDetallePorGuia(codigo);

            // Mapear a util.GuiaRemisionGenerator.DetalleGuia
            List<GuiaRemisionGenerator.DetalleGuia> itemsPdf = new ArrayList<>();
            int contador = 1;
            for (DetalleGuia d : detalles) {
                GuiaRemisionGenerator.DetalleGuia it = new GuiaRemisionGenerator.DetalleGuia(
                        contador++,
                        d.getBienNormalizado(),
                        d.getCodigoBien(),
                        d.getCodigoProductoSunat(),
                        d.getPartidaArancelaria(),
                        d.getCodigoGtin(),
                        d.getDescripcion(),
                        d.getUnidadMedida(),
                        d.getCantidad()
                );
                itemsPdf.add(it);
            }

            // Datos básicos (ajusta según tu esquema; aquí uso placeholders si no existen columnas)
            String empresaNombre = "RED LIPA ECOLOGICA SOCIEDAD ANONIMA CERRADA";
            String rucEmisor = "20604635943";
            String rucRemitente = g.getRucRemitente();
            String serie = g.getSerie() == null ? "" : g.getSerie();
            String numero = g.getNumero() == null ? "" : g.getNumero();
            String fechaEmision = g.getFechaEmision() == null ? "" : g.getFechaEmision().toString();

            // Pedir ruta de guardado
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new java.io.File("GUIA_" + serie + "_" + numero + ".pdf"));
            int sel = fc.showSaveDialog(this);
            if (sel != JFileChooser.APPROVE_OPTION) return;
            String path = fc.getSelectedFile().getAbsolutePath();

            try {
                // Llamada al generador: (muchos campos no están disponibles en CabeceraGuia; uso placeholders vacíos)
                util.GuiaRemisionGenerator.createPdf(
                        path,
                        empresaNombre,
                        rucEmisor,
                        rucRemitente,
                        serie,
                        numero,
                        fechaEmision,
                        "", // fechaInicioTraslado
                        "", // puntoPartida
                        "", // puntoLlegada
                        "", // motivo
                        "", // destinatarioNombre (si lo tienes, búscalo con DestinatarioDAO)
                        "", // destinatarioRuc
                        itemsPdf,
                        "KGM", // unidadPeso
                        "", // pesoBrutoTotal
                        "", // modalidad
                        "", // indicadorTransbordo
                        "", // indicadorRetornoEnvases
                        "", // placa
                        "", // autorizacionVehiculo
                        "", // nombreConductor
                        "", // licenciaConductor
                        null // qrImageBytes
                );
                JOptionPane.showMessageDialog(this, "PDF generado en:\n" + path);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error generando PDF: " + ex.getMessage());
            }
        });

        load();
    }

    public void load() {
        model.setRowCount(0);
        List<CabeceraGuia> lista = dao.listarTodas();
        for (CabeceraGuia g: lista) model.addRow(new Object[]{g.getCodigoGuia(), g.getSerie(), g.getNumero(), g.getCodOrden(), g.getRucRemitente(), g.getFechaEmision(), g.getHoraEmision(), g.getEstadoGuia()});
    }
}
