package ui;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class UIStyles {

    // Requested palette: dark-green (#1F4D2E) and grey-black (#222222)
    public static final Color TEXT_MAIN = new Color(0x22,0x22,0x22); // #222222
    public static final Color GREEN_DARK = new Color(0x1F,0x4D,0x2E); // #1F4D2E
    // Soft background tones (remain light but not identical to text)
    public static final Color BG = new Color(247, 249, 246); // very light soft green
    public static final Color PANEL = new Color(240, 244, 239);
    public static final Font UI_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    // Title style helper values
    public static final float TITLE_SIZE = 18f;
    public static final float SUBTITLE_SIZE = 14f;

    public static void applyDefaultFont(Component root) {
        if (root == null) return;
        root.setFont(UI_FONT);
    }

    public static void applyThemeDefaults() {
        // Set general UI defaults for better contrast
        UIManager.put("Label.foreground", TEXT_MAIN);
        UIManager.put("Button.foreground", GREEN_DARK);
        UIManager.put("Table.foreground", TEXT_MAIN);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.selectionBackground", new Color(220,240,230));
        UIManager.put("Table.selectionForeground", TEXT_MAIN);
        UIManager.put("Panel.background", BG);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT_MAIN);
    }

    public static void styleButton(JButton b) {
    // Buttons: white background, green dark border and text
    b.setBackground(Color.WHITE);
    b.setForeground(GREEN_DARK);
        b.setFocusPainted(false);
    b.setBorder(new RoundedBorder(8, GREEN_DARK));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(110, 30));
    b.setFont(UI_FONT.deriveFont(Font.BOLD, 12f));
    // add some padding
    b.setMargin(new Insets(6,12,6,12));
    }

    // Simple rounded border
    public static class RoundedBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x+1, y+1, width-3, height-3, radius, radius);
            g2.dispose();
        }
    }
}
