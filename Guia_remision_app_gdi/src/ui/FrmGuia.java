package ui;

import dao.GuiaDAO;
import dao.DestinatarioDAO;
import model.CabeceraGuia;
import model.Destinatario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmGuia extends JDialog {
    private GuiaDAO dao = new GuiaDAO();
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

        JTextField txtCodigo = new JTextField(); JTextField txtSerie = new JTextField(); JTextField txtNumero = new JTextField();
        JComboBox<String> cbDest = new JComboBox<>(); List<Destinatario> dests = ddao.listarTodos(); for (Destinatario d: dests) cbDest.addItem(d.getRuc()+" - "+d.getNombre());
        JTextField txtDirPartida = new JTextField(); JTextField txtDirLlegada = new JTextField();
        JTextField txtUbOri = new JTextField(); JTextField txtUbDest = new JTextField(); JTextField txtPeso = new JTextField("0.0"); JTextField txtBultos = new JTextField("0");

        int y=0; gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Código:"), gbc); gbc.gridx=1; p.add(txtCodigo, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Serie:"), gbc); gbc.gridx=1; p.add(txtSerie, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Número:"), gbc); gbc.gridx=1; p.add(txtNumero, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Destinatario:"), gbc); gbc.gridx=1; p.add(cbDest, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Dir. partida:"), gbc); gbc.gridx=1; p.add(txtDirPartida, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Dir. llegada:"), gbc); gbc.gridx=1; p.add(txtDirLlegada, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Ubigeo origen:"), gbc); gbc.gridx=1; p.add(txtUbOri, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Ubigeo destino:"), gbc); gbc.gridx=1; p.add(txtUbDest, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Peso total:"), gbc); gbc.gridx=1; p.add(txtPeso, gbc); y++;
        gbc.gridx=0; gbc.gridy=y; p.add(new JLabel("Número bultos:"), gbc); gbc.gridx=1; p.add(txtBultos, gbc); y++;

        JButton ok = new JButton(editing==null?"Emitir":"Guardar"); UIStyles.styleButton(ok); JButton cancel = new JButton("Cancelar"); UIStyles.styleButton(cancel);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT)); actions.setOpaque(false); actions.add(ok); actions.add(cancel);
        gbc.gridx=0; gbc.gridy=y; gbc.gridwidth=2; p.add(actions, gbc);

        cancel.addActionListener(e->dispose());
        ok.addActionListener(e->{
            // basic validations
            if (txtCodigo.getText().trim().isEmpty() || txtSerie.getText().trim().isEmpty() || txtNumero.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this,"Complete los campos obligatorios"); return; }
            try {
                double peso = Double.parseDouble(txtPeso.getText().trim()); int bult = Integer.parseInt(txtBultos.getText().trim());
                // call DAO
                boolean okr = dao.emitirGuia(txtCodigo.getText().trim(), txtSerie.getText().trim(), txtNumero.getText().trim(), null, "", ((String)cbDest.getSelectedItem()).split(" - ")[0].trim(), txtDirPartida.getText().trim(), txtDirLlegada.getText().trim(), txtUbOri.getText().trim(), txtUbDest.getText().trim(), "", "", peso, bult);
                if (okr) { JOptionPane.showMessageDialog(this,"Emitida"); dispose(); ((GuiaWindow)getOwner()).load(); }
                else JOptionPane.showMessageDialog(this,"Error al emitir");
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Peso o bultos con formato inválido"); }
        });

        setContentPane(p);
    }
}
