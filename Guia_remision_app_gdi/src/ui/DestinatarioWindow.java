package ui;

import dao.DestinatarioDAO;
import model.Destinatario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DestinatarioWindow extends JFrame {

    private DestinatarioDAO dao = new DestinatarioDAO();
    private DefaultTableModel model;

    public DestinatarioWindow() {
        setTitle("Destinatarios");
        setSize(900, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(UIStyles.BG);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(8,8));
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        root.setBackground(UIStyles.BG);

        String[] cols = {"RUC","Nombre","Teléfono","Dirección","Ubigeo","Gmail"};
        model = new DefaultTableModel(cols,0);
        JTable table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(26);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);
        JButton btnNuevo = new JButton("Nuevo"); UIStyles.styleButton(btnNuevo);
        JButton btnEditar = new JButton("Editar"); UIStyles.styleButton(btnEditar);
        JButton btnEliminar = new JButton("Eliminar"); UIStyles.styleButton(btnEliminar);
        JButton btnRefrescar = new JButton("Actualizar"); UIStyles.styleButton(btnRefrescar);

        top.add(btnNuevo); top.add(btnEditar); top.add(btnEliminar); top.add(btnRefrescar);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        add(root);

        // Actions
        btnRefrescar.addActionListener(e -> load());
        btnNuevo.addActionListener(e -> new FrmDestinatario(this, null).setVisible(true));
        btnEditar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione una fila"); return; }
            String ruc = model.getValueAt(r,0).toString();
            Destinatario d = dao.listarTodos().stream().filter(x->x.getRuc().equals(ruc)).findFirst().orElse(null);
            if (d != null) new FrmDestinatario(this, d).setVisible(true);
        });
        btnEliminar.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r < 0) { JOptionPane.showMessageDialog(this, "Seleccione una fila"); return; }
            String ruc = model.getValueAt(r,0).toString();
            int resp = JOptionPane.showConfirmDialog(this, "Eliminar RUC " + ruc + "?","Confirmar",JOptionPane.YES_NO_OPTION);
            if (resp == JOptionPane.YES_OPTION) {
                if (dao.eliminar(ruc)) { JOptionPane.showMessageDialog(this, "Eliminado"); load(); }
                else JOptionPane.showMessageDialog(this, "Error al eliminar");
            }
        });

        // initial load
        load();
    }

    public void load() {
        model.setRowCount(0);
        List<Destinatario> lista = dao.listarTodos();
        for (Destinatario d : lista) {
            model.addRow(new Object[]{d.getRuc(), d.getNombre(), d.getNumeroTelefono(), d.getCalleDireccion(), d.getCodigoUbigeo(), d.getGmail()});
        }
    }
}
