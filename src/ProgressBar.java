import javax.swing.*;
import java.awt.*;

public class ProgressBar {
    JProgressBar bar;
    JFrame frame;
    int done,total;

    public ProgressBar(int done,int total)
    {
        this.done=done;
        this.total=total;


        frame=new JFrame("Synchronizing");
        bar=new JProgressBar();
        frame.setLayout(new BorderLayout());

        frame.add(bar,BorderLayout.CENTER);
        frame.setSize(300,60);
        frame.setLocation(500,400);
        bar.setValue(20);
        frame.setResizable(false);
        bar.setStringPainted(true);
        bar.setForeground(Color.green);



        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\syncIcon.png").getImage());

        frame.setVisible(true);
    }

   public void setDone(int done)
    {
        this.done=done;
    }

}
