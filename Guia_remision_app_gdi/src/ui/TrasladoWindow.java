package ui;

import dao.TrasladoDAO;
import model.Traslado;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TrasladoWindow extends JFrame {
    private TrasladoDAO dao = new TrasladoDAO();
    private DefaultTableModel model;

    public TrasladoWindow() {
        setTitle("Traslados"); setSize(900,520); setLocationRelativeTo(null); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyles.BG);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8,8)); root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8)); root.setBackground(UIStyles.BG);
        String[] cols = {"Código traslado","Guía","Placa","Licencia","Inicio","Fin","Estado","Obs"};
        model = new DefaultTableModel(cols,0); JTable table = new JTable(model); table.setRowHeight(26);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT)); top.setOpaque(false);
        JButton btnNuevo = new JButton("Nuevo"); UIStyles.styleButton(btnNuevo);
        JButton btnEditar = new JButton("Editar"); UIStyles.styleButton(btnEditar);
        JButton btnEliminar = new JButton("Eliminar"); UIStyles.styleButton(btnEliminar);
        JButton btnRefrescar = new JButton("Actualizar"); UIStyles.styleButton(btnRefrescar);
        top.add(btnNuevo); top.add(btnEditar); top.add(btnEliminar); top.add(btnRefrescar);

        root.add(top, BorderLayout.NORTH); root.add(new JScrollPane(table), BorderLayout.CENTER); add(root);

        btnRefrescar.addActionListener(e->load());
        btnNuevo.addActionListener(e-> new FrmTraslado(this, null).setVisible(true));
        btnEditar.addActionListener(e->{ int r = table.getSelectedRow(); if (r<0){ JOptionPane.showMessageDialog(this,"Seleccione fila"); return; } String cod = model.getValueAt(r,0).toString(); List<Traslado> lista = dao.listarTodos(); Traslado t = lista.stream().filter(x->x.getCodigoTraslado().equals(cod)).findFirst().orElse(null); if (t!=null) new FrmTraslado(this, t).setVisible(true); });

        load();
    }

    public void load() {
        model.setRowCount(0);
        List<Traslado> lista = dao.listarTodos();
        for (Traslado t: lista) model.addRow(new Object[]{t.getCodigoTraslado(), t.getCodigoGuia(), t.getPlaca(), t.getLicencia(), t.getFechaInicio(), t.getFechaFin(), t.getEstadoTraslado(), t.getObservaciones()});
    }
}
