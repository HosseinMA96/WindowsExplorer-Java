/**
 * A class to make progressBar
 */

package View;

import javax.swing.*;
import java.awt.*;

public class ProgressBar {
    private JProgressBar bar;
    private JFrame frame;
    private int done, total;

    /**
     * Cunstructor for this class
     *
     * @param done
     * @param total
     */
    public ProgressBar(int done, int total) {
        this.done = done;
        this.total = total;


        frame = new JFrame("Synchronizing");
        bar = new JProgressBar();
        frame.setLayout(new BorderLayout());

        frame.add(bar, BorderLayout.CENTER);
        frame.setSize(300, 60);
        frame.setLocation(500, 400);
        bar.setValue((done * 100) / total);
        frame.setResizable(false);
        bar.setStringPainted(true);
        bar.setForeground(Color.green);


        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\syncIcon.png").getImage());

        frame.setVisible(true);
    }

    /**
     * Setter for done percentage
     *
     * @param done
     */
    public void setDone(int done) {
        this.done = done;

    }

}
