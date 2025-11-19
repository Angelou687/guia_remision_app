package ui;

import javax.swing.*;
import java.awt.*;

public class FrmMenu extends JFrame {

    public FrmMenu() {
        setTitle("Guía de Remisión - RED LIPA");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UIStyles.BG);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(12,12));
        root.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        root.setBackground(UIStyles.BG);

        JLabel title = new JLabel("Panel de administración");
        title.setFont(UIStyles.UI_FONT.deriveFont(Font.BOLD, 18f));
    title.setForeground(UIStyles.GREEN_DARK);
        title.setBorder(BorderFactory.createEmptyBorder(6,6,12,6));

    JPanel cards = new JPanel(new GridLayout(1,4,12,12));
        cards.setOpaque(false);
    cards.add(createCard("Destinatarios", () -> openDestinatarios()));
    cards.add(createCard("Guías", () -> openGuias()));
    cards.add(createCard("Traslados", () -> openTraslados()));
    cards.add(createCard("Reportes", () -> openReportes()));

        root.add(title, BorderLayout.NORTH);
        root.add(cards, BorderLayout.CENTER);

        add(root);
    }

    private JPanel createCard(String text, Runnable onClick) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(UIStyles.PANEL);
        p.setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel lab = new JLabel(text, SwingConstants.CENTER);
        lab.setFont(UIStyles.UI_FONT.deriveFont(Font.BOLD, 14f));
    lab.setForeground(UIStyles.GREEN_DARK);

        JButton b = new JButton("Abrir");
        UIStyles.styleButton(b);
        b.addActionListener(e -> onClick.run());

        p.add(lab, BorderLayout.CENTER);
        JPanel south = new JPanel(); south.setOpaque(false); south.add(b);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    private void openDestinatarios() {
        SwingUtilities.invokeLater(() -> new ui.DestinatarioWindow().setVisible(true));
    }

    private void openGuias() {
        SwingUtilities.invokeLater(() -> new ui.GuiaWindow().setVisible(true));
    }

    private void openTraslados() {
        SwingUtilities.invokeLater(() -> new ui.TrasladoWindow().setVisible(true));
    }

    private void openReportes() {
        SwingUtilities.invokeLater(() -> new ui.ReportesWindow().setVisible(true));
    }
}
