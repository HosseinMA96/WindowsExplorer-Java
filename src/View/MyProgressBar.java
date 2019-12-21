/**
 * A class to make progressBar
 */

package View;

import javax.swing.*;
import java.awt.*;

public class MyProgressBar {
    private JProgressBar bar;
    private JFrame frame;
    private int done, total, totalToBeDeleted, totalToBeAdded, added = 0, deleted = 0;
    private JLabel label=new JLabel();

    /**
     * Constructor for this class
     * @param done
     * @param del
     * @param add
     */
    public MyProgressBar(int done, int del, int add) {
        this.done = done;
        this.totalToBeAdded = add;
        this.totalToBeDeleted = del;
        this.total = totalToBeDeleted + totalToBeAdded;


        frame = new JFrame("Synchronizing");
        bar = new JProgressBar();
        frame.setLayout(new BorderLayout());
        label.setText("0/" + totalToBeAdded + " Files Made " + "0/" + totalToBeDeleted + " Files deleted");
        frame.add(label, BorderLayout.NORTH);
        frame.add(bar, BorderLayout.CENTER);

        frame.setSize(300, 60);
        frame.setLocation(500, 400);

        if(total != 0)
        bar.setValue((done * 100) / total);

        else
            bar.setValue(100);

        frame.setResizable(false);
        bar.setStringPainted(true);
        bar.setForeground(Color.green);


        frame.setIconImage(new ImageIcon("images\\syncIcon.png").getImage());

        frame.setVisible(true);
        bar.setVisible(true);
    }

    /**
     * report deleting one file
     */
    public void oneDelete() {
        deleted++;
        done++;
        upgradeStats();
    }

    /**
     * report adding one file
     */
    public void oneAdd() {
        added++;
        done++;
        upgradeStats();
    }


    /**
     * Upgrade status of this rogress bar
     */
    private void upgradeStats() {
        if(total != 0)
        bar.setValue((done * 100) / total);

        else
            bar.setValue(100);

        label.setText(added + "/" + totalToBeAdded + " Files Made " + deleted + "/" + totalToBeDeleted + " Files deleted");
        frame.repaint();
        bar.repaint();
    }

}
