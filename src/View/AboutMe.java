/**
 * A class to represent about me frame
 */
package View;

import javax.swing.*;
import java.awt.*;

public class AboutMe {
    private JFrame aboutMeFrame;

    /**
     * Constructor for this class
     */
    public AboutMe() {

        aboutMeFrame = new JFrame("About Me");
        aboutMeFrame.setLayout(new GridLayout(3, 1));
        aboutMeFrame.setSize(300, 200);
        aboutMeFrame.setLocation(500, 300);
        aboutMeFrame.add(new JLabel("          Implemented by Hossein Mohassel"));
        aboutMeFrame.add(new JLabel("          9424059-9431703"));
        aboutMeFrame.add(new JLabel("          Fall 1398"));
        aboutMeFrame.setVisible(true);
        aboutMeFrame.setResizable(false);
        aboutMeFrame.getContentPane().setBackground(Color.LIGHT_GRAY);


        ImageIcon img = new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\informationIcon.png");
        aboutMeFrame.setIconImage(img.getImage());
    }
}
