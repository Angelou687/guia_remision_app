package ui;

import dao.GuiaDAO;
import model.CabeceraGuia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        top.add(btnNuevo); top.add(btnEditar); top.add(btnEliminar); top.add(btnRefrescar);

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
        btnEliminar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Eliminar desde UI no implementado (use DB)"));

        load();
    }

    public void load() {
        model.setRowCount(0);
        List<CabeceraGuia> lista = dao.listarTodas();
        for (CabeceraGuia g: lista) model.addRow(new Object[]{g.getCodigoGuia(), g.getSerie(), g.getNumero(), g.getCodOrden(), g.getRucRemitente(), g.getFechaEmision(), g.getHoraEmision(), g.getEstadoGuia()});
    }
}
