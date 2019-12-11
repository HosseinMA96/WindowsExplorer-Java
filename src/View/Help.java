/**
 * A class to generate help frame
 */
package View;

import javax.swing.*;
import java.awt.*;

public class Help {
    private JFrame help;
    private JTextArea textArea;

    /**
     * Constructor for this class
     */

    public Help() {
        help = new JFrame("View.View.Help");
        help.setLayout(new FlowLayout(1));
        help.setSize(600, 300);
        help.setLocation(500, 400);

        textArea = new JTextArea("This program works similar to windows explorer.\nIt allows viewing file, sorting them with regard to several features.\nIt allows two computers to synchronize with each other.\n It shows File hierarchy, Shows addresses and allows to search.\nIt can show files in two ways: Grid display and List display");
        help.add(textArea);


        ImageIcon img = new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\questionIcon.png");
        help.setIconImage(img.getImage());
        textArea.setEnabled(false);

        //Deal with the font
        Font font = textArea.getFont();
        float size = font.getSize() + 10.0f;
        textArea.setFont(font.deriveFont(size));

        //Deal with fram color
        help.getContentPane().setBackground(Color.BLUE);
        help.pack();

        textArea.setForeground(Color.orange);

        help.setResizable(false);
        help.setVisible(true);


    }
}
