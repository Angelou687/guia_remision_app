package ui;

import dao.DestinatarioDAO;
import model.Destinatario;

import javax.swing.*;
import java.awt.*;

public class FrmDestinatario extends JDialog {

    private DestinatarioDAO dao = new DestinatarioDAO();
    private Destinatario editing;

    public FrmDestinatario(Frame owner, Destinatario d) {
        super(owner, true);
        this.editing = d;
        setTitle(d == null ? "Nuevo destinatario" : "Editar destinatario");
        setSize(520, 420);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        p.setBackground(UIStyles.BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtRuc = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtTel = new JTextField();
        JTextField txtDir = new JTextField();
        JTextField txtUbigeo = new JTextField();
        JTextField txtGmail = new JTextField();

        int y=0;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("RUC*:"), gbc);
        gbc.gridx=1; gbc.gridy=y++; p.add(txtRuc, gbc);
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Nombre*:"), gbc);
        gbc.gridx=1; gbc.gridy=y++; p.add(txtNombre, gbc);
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx=1; gbc.gridy=y++; p.add(txtTel, gbc);
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Dirección*:"), gbc);
        gbc.gridx=1; gbc.gridy=y++; p.add(txtDir, gbc);
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Ubigeo*:"), gbc);
        gbc.gridx=1; gbc.gridy=y++; p.add(txtUbigeo, gbc);
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Gmail:"), gbc);
        gbc.gridx=1; gbc.gridy=y++; p.add(txtGmail, gbc);

        JButton btnOk = new JButton(editing==null?"Crear":"Guardar"); UIStyles.styleButton(btnOk);
        JButton btnCancel = new JButton("Cancelar"); UIStyles.styleButton(btnCancel);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false);
        actions.add(btnOk); actions.add(btnCancel);
        gbc.gridx=0; gbc.gridy=y; gbc.gridwidth=2; p.add(actions, gbc);

        if (editing != null) {
            txtRuc.setText(editing.getRuc()); txtRuc.setEnabled(false);
            txtNombre.setText(editing.getNombre()); txtTel.setText(editing.getNumeroTelefono());
            txtDir.setText(editing.getCalleDireccion()); txtUbigeo.setText(editing.getCodigoUbigeo());
            txtGmail.setText(editing.getGmail());
        }

        btnCancel.addActionListener(e -> dispose());
        btnOk.addActionListener(e -> {
            String ruc = txtRuc.getText().trim();
            String nombre = txtNombre.getText().trim();
            String dir = txtDir.getText().trim();
            String ub = txtUbigeo.getText().trim();
            String tel = txtTel.getText().trim();
            String gm = txtGmail.getText().trim();

            if (ruc.isEmpty() || nombre.isEmpty() || dir.isEmpty() || ub.isEmpty()) {
                JOptionPane.showMessageDialog(this, "RUC, Nombre, Dirección y Ubigeo son obligatorios"); return;
            }
            // RUC: exactamente 11 dígitos
            if (!ruc.matches("\\d{11}")) {
                JOptionPane.showMessageDialog(this, "RUC debe tener 11 dígitos numéricos"); return;
            }
            // Ubigeo: exactamente 6 dígitos
            if (!ub.matches("\\d{6}")) { JOptionPane.showMessageDialog(this, "Ubigeo debe tener 6 dígitos"); return; }
            // Email simple RFC-like check
            if (!gm.isEmpty() && !gm.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                JOptionPane.showMessageDialog(this, "Email inválido"); return;
            }

            if (editing == null) {
                Destinatario d = new Destinatario(ruc, nombre, tel, dir, ub, gm);
                if (dao.insertar(d)) { JOptionPane.showMessageDialog(this, "Creado"); dispose(); ((DestinatarioWindow)getOwner()).load(); }
                else JOptionPane.showMessageDialog(this, "Error al crear");
            } else {
                editing.setNombre(nombre); editing.setCalleDireccion(dir); editing.setCodigoUbigeo(ub);
                editing.setNumeroTelefono(tel); editing.setGmail(gm);
                if (dao.actualizar(editing)) { JOptionPane.showMessageDialog(this, "Actualizado"); dispose(); ((DestinatarioWindow)getOwner()).load(); }
                else JOptionPane.showMessageDialog(this, "Error al actualizar");
            }
        });

        setContentPane(p);
    }
}
