package ui;

import dao.GuiaDAO;
import dao.DestinatarioDAO;
import model.CabeceraGuia;
import model.Destinatario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmGuia extends JDialog {
    private GuiaDAO guiaDAO = new GuiaDAO();
    private DestinatarioDAO ddao = new DestinatarioDAO();
    private CabeceraGuia editing;

    public FrmGuia(Frame owner, CabeceraGuia g) {
        super(owner, true);
        this.editing = g;
        setTitle(g==null?"Emitir guía":"Editar guía");
        setSize(600,520);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel p = new JPanel(new GridBagLayout()); p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12)); p.setBackground(UIStyles.BG);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(6,6,6,6); gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx=1.0;

        JTextField txtCodigo = new JTextField();
        JTextField txtSerie  = new JTextField();
        JTextField txtNumero = new JTextField();
        // ahora sí mostramos el campo ORDEN y RUC remitente
        JTextField txtCodOrden = new JTextField();
        JTextField txtRucRemitente = new JTextField();
        JComboBox<String> cbDestinatario = new JComboBox<>(); // lleno con "RUC - NOMBRE"
        JTextField txtDirPartida = new JTextField();
        JTextField txtDirLlegada = new JTextField();
        JTextField txtUbigeoOrigen = new JTextField();
        JTextField txtUbigeoDestino = new JTextField();
        JTextField txtPesoTotal = new JTextField("0.0");
        JTextField txtNumBultos = new JTextField("0");

        // Cambié nombre del botón para evitar conflicto con variable local 'ok' dentro del listener
        JButton btnOk = new JButton(editing==null?"Emitir":"Guardar"); UIStyles.styleButton(btnOk);
        JButton btnCancel  = new JButton("Cancelar"); UIStyles.styleButton(btnCancel);

        int y=0;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Código:"), gbc); gbc.gridx=1; p.add(txtCodigo, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Serie:"), gbc); gbc.gridx=1; p.add(txtSerie, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Número:"), gbc); gbc.gridx=1; p.add(txtNumero, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Código orden:"), gbc); gbc.gridx=1; p.add(txtCodOrden, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("RUC remitente:"), gbc); gbc.gridx=1; p.add(txtRucRemitente, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Destinatario:"), gbc); gbc.gridx=1; p.add(cbDestinatario, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Dir. partida:"), gbc); gbc.gridx=1; p.add(txtDirPartida, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Dir. llegada:"), gbc); gbc.gridx=1; p.add(txtDirLlegada, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Ubigeo origen:"), gbc); gbc.gridx=1; p.add(txtUbigeoOrigen, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Ubigeo destino:"), gbc); gbc.gridx=1; p.add(txtUbigeoDestino, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Peso total:"), gbc); gbc.gridx=1; p.add(txtPesoTotal, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Número bultos:"), gbc); gbc.gridx=1; p.add(txtNumBultos, gbc); y++;

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false); actions.add(btnOk); actions.add(btnCancel);
        gbc.gridx=0; gbc.gridy=y; gbc.gridwidth=2; p.add(actions, gbc);

        // cargar destinatarios en el combo (RUC - NOMBRE)
        List<Destinatario> dests = ddao.listarTodos();
        for (Destinatario d : dests) cbDestinatario.addItem(d.getRuc() + " - " + d.getNombre());

        // Si venimos con 'editing' (objeto pasado desde la lista) recargamos la versión completa desde la BD
        if (editing != null) {
            CabeceraGuia full = guiaDAO.obtenerPorCodigo(editing.getCodigoGuia());
            if (full != null) {
                this.editing = full; // sustituir por la versión completa
            }
        }

        // si estamos editando, poblar los campos desde la CabeceraGuia actualizada
        if (editing != null) {
            txtCodigo.setText(editing.getCodigoGuia());
            txtCodigo.setEnabled(false); // no permitir cambiar PK
            txtSerie.setText(editing.getSerie() == null ? "" : editing.getSerie());
            txtNumero.setText(editing.getNumero() == null ? "" : editing.getNumero());
            txtCodOrden.setText(editing.getCodOrden() == null ? "" : editing.getCodOrden());
            txtRucRemitente.setText(editing.getRucRemitente() == null ? "" : editing.getRucRemitente());
            txtDirPartida.setText(editing.getDirPartida() == null ? "" : editing.getDirPartida());
            txtDirLlegada.setText(editing.getDirLlegada() == null ? "" : editing.getDirLlegada());
            txtUbigeoOrigen.setText(editing.getUbigeoOrigen() == null ? "" : editing.getUbigeoOrigen());
            txtUbigeoDestino.setText(editing.getUbigeoDestino() == null ? "" : editing.getUbigeoDestino());
            txtPesoTotal.setText(editing.getPesoTotal() == null ? "0.0" : String.valueOf(editing.getPesoTotal()));
            txtNumBultos.setText(editing.getNumeroBultos() == null ? "0" : String.valueOf(editing.getNumeroBultos()));
            // seleccionar destinatario en el combo por RUC si existe
            if (editing.getRucDestinatario() != null) {
                for (int i = 0; i < cbDestinatario.getItemCount(); i++) {
                    String item = cbDestinatario.getItemAt(i);
                    if (item != null && item.startsWith(editing.getRucDestinatario())) {
                        cbDestinatario.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }

        btnCancel.addActionListener(e->dispose());
        btnOk.addActionListener(e->{
            // basic validations
            if (txtCodigo.getText().trim().isEmpty() || txtSerie.getText().trim().isEmpty() || txtNumero.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Complete los campos obligatorios"); return; }
            try {
                String codigo = txtCodigo.getText().trim();
                String serie  = txtSerie.getText().trim();
                String numero = txtNumero.getText().trim();
                String codOrden = txtCodOrden.getText().trim();
                String rucRemitente = txtRucRemitente.getText().trim(); // ahora tomado del form

                // destinatario: esperamos "RUC - NOMBRE" en el combo; si no, usar el texto completo
                String rucDest = "";
                Object sel = cbDestinatario.getSelectedItem();
                if (sel != null) {
                    String s = sel.toString();
                    if (s.contains(" - ")) rucDest = s.split(" - ")[0].trim();
                    else rucDest = s.trim();
                }
                String dirPartida = txtDirPartida.getText().trim();
                String dirLlegada = txtDirLlegada.getText().trim();
                String ubigeoOrigen = txtUbigeoOrigen.getText().trim();
                String ubigeoDestino = txtUbigeoDestino.getText().trim();
                double pesoTotal = 0.0;
                int numeroBultos = 0;
                try { pesoTotal = Double.parseDouble(txtPesoTotal.getText().trim()); } catch (Exception ex) { /* deja 0.0 */ }
                try { numeroBultos = Integer.parseInt(txtNumBultos.getText().trim()); } catch (Exception ex) { /* deja 0 */ }

                // llamada a emitirGuia: pasamos codOrden y rucRemitente desde el formulario
                boolean success = guiaDAO.emitirGuia(
                        codigo, serie, numero, codOrden.isEmpty() ? null : codOrden,
                        rucRemitente.isEmpty() ? null : rucRemitente,
                        rucDest.isEmpty() ? null : rucDest,
                        dirPartida, dirLlegada,
                        ubigeoOrigen, ubigeoDestino,
                        /*motivo*/ "", /*modalidad*/ "",
                        pesoTotal, numeroBultos
                );

                if (success) {
                    JOptionPane.showMessageDialog(this, "Guía emitida correctamente");
                    dispose(); // cerrar ventana al terminar
                } else {
                    JOptionPane.showMessageDialog(this, "Error al emitir la guía. Revisa logs.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        setContentPane(p);
    }
}
