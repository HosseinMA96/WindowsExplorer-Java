package View;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class JLabelCellenderer extends JLabel implements TableCellRenderer {

    public JLabelCellenderer(String path) {
        setOpaque(true);
        setBackground(Color.WHITE);
        java.net.URL imgURL = getClass().getResource(path);
        this.setIcon(new ImageIcon(imgURL));
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //  setText((String) value);
        if(isSelected){setBackground(new Color(184,207,229));}
        else{setBackground(Color.WHITE);}
        return this;
    }
}