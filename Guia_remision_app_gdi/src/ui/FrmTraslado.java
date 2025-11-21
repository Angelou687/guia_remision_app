package ui;

import dao.TrasladoDAO;
import model.Traslado;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FrmTraslado extends JDialog {
    private TrasladoDAO dao = new TrasladoDAO();
    private Traslado editing;

    public FrmTraslado(Frame owner, Traslado t) {
        super(owner, true);
        this.editing = t;
        setTitle(t==null?"Nuevo traslado":"Editar traslado");
        setSize(560,520);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel(new GridBagLayout()); p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12)); p.setBackground(UIStyles.BG);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(6,6,6,6); gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1.0;

        JTextField txtCod = new JTextField(); JTextField txtGuia = new JTextField(); JTextField txtPlaca = new JTextField(); JTextField txtLic = new JTextField();
        // ahora solo fecha (dd/MM/yyyy) — sin hora por defecto
        JTextField txtInicio = new JTextField(); JTextField txtFin = new JTextField();
        JTextField txtEstado = new JTextField("en tránsito"); JTextField txtObs = new JTextField();

        int y=0; gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Código traslado:"), gbc); gbc.gridx=1; p.add(txtCod, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Código guía:"), gbc); gbc.gridx=1; p.add(txtGuia, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Placa:"), gbc); gbc.gridx=1; p.add(txtPlaca, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Licencia:"), gbc); gbc.gridx=1; p.add(txtLic, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Inicio (dd/MM/yyyy):"), gbc); gbc.gridx=1; p.add(txtInicio, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Fin (dd/MM/yyyy):"), gbc); gbc.gridx=1; p.add(txtFin, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Estado:"), gbc); gbc.gridx=1; p.add(txtEstado, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Observaciones:"), gbc); gbc.gridx=1; p.add(txtObs, gbc); y++;

        JButton ok = new JButton(editing==null?"Registrar":"Guardar"); UIStyles.styleButton(ok); JButton cancel = new JButton("Cancelar"); UIStyles.styleButton(cancel);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false); actions.add(ok); actions.add(cancel);
        gbc.gridx=0; gbc.gridy=y; gbc.gridwidth=2; p.add(actions, gbc);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        if (editing != null) {
            txtCod.setText(editing.getCodigoTraslado()); txtGuia.setText(editing.getCodigoGuia()); txtPlaca.setText(editing.getPlaca()); txtLic.setText(editing.getLicencia());
            // mostrar solo fecha en dd/MM/yyyy (si no es null)
            if (editing.getFechaInicio() != null) {
                txtInicio.setText(editing.getFechaInicio().toLocalDateTime().toLocalDate().format(fmt));
            }
            if (editing.getFechaFin() != null) {
                txtFin.setText(editing.getFechaFin().toLocalDateTime().toLocalDate().format(fmt));
            }
            txtEstado.setText(editing.getEstadoTraslado()); txtObs.setText(editing.getObservaciones());
        }

        cancel.addActionListener(e->dispose());
        ok.addActionListener(e->{
            try {
                String cod = txtCod.getText().trim(); String guia = txtGuia.getText().trim(); String placa = txtPlaca.getText().trim(); String lic = txtLic.getText().trim();
                String ini = txtInicio.getText().trim(); String fin = txtFin.getText().trim(); String estado = txtEstado.getText().trim(); String obs = txtObs.getText().trim();
                if (cod.isEmpty() || guia.isEmpty() || placa.isEmpty() || lic.isEmpty()) { JOptionPane.showMessageDialog(this,"Campos obligatorios faltantes"); return; }

                // Parseo: dd/MM/yyyy → Timestamp (inicio = startOfDay)
                LocalDate dIni = LocalDate.parse(ini, fmt);
                LocalDate dFin = LocalDate.parse(fin, fmt);

                Timestamp tIni = Timestamp.valueOf(dIni.atStartOfDay());
                Timestamp tFin = Timestamp.valueOf(dFin.atStartOfDay());

                if (tFin.before(tIni)) { JOptionPane.showMessageDialog(this,"Fecha fin debe ser posterior o igual a inicio"); return; }

                Traslado tt = new Traslado(cod, guia, placa, lic, tIni, tFin, estado, obs);
                if (editing==null) {
                    if (dao.registrarTraslado(tt)) { JOptionPane.showMessageDialog(this,"Registrado"); dispose(); ((TrasladoWindow)getOwner()).load(); }
                    else JOptionPane.showMessageDialog(this,"Error al registrar traslado");
                } else {
                    // edición no implementada en DAO
                    JOptionPane.showMessageDialog(this,"Edición no implementada en DAO");
                }
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,"Formato fecha inválido. Use: dd/MM/yyyy");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,"Error en datos: " + ex.getMessage());
            }
        });

        setContentPane(p);
    }
}
