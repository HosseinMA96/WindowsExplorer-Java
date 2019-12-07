package View;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class DrawRect extends JPanel {

    private int x, y, x2, y2;
    private boolean singleChoice = true, leftClicked = false;
    PopMenu popMenu;
    private PopMenu singlePopMenu, multPopMenu;
    private ArrayList<GridIcon>gridIcons;
    private File[] files;



//    public static void main(String[] args) {
//        JFrame f = new JFrame("Draw Box Mouse 2");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setContentPane(new View.View.DrawRect());
//        f.setSize(300, 300);
//        f.setVisible(true);
//    }

    public void setFiles(File[] files) {
        this.files = files;
    }


    public DrawRect(File[] F) {
     //   singlePopMenu = new PopMenu(true, this);
     //   multPopMenu = new PopMenu(false, this);
        x = y = x2 = y2 = 0; //
        //     JOptionPane.showMessageDialog(null,"This THis");
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        files=F;
       // super.setOpaque(false);
        addButtons();

    }

    void addButtons()
    {
        int numberOfIcons = files.length;
    //    rightSidePanel = new DrawRect();
        this.setLayout((new GridLayout(numberOfIcons / 5 + 1, 1)));

   //     layeredPane = new JLayeredPane();


        //  rightSidePanel.setBackground(new Color(0, 0, 0, 10));
        //rightSidePanel.setOpaque(false);
//add to the code


        gridIcons = new ArrayList<>();

        int temp = 0;

      //  ArrayList<JPanel> panels = new ArrayList<>();
        JPanel dummyPanel = new JPanel(new GridLayout(1, 5));



        for (File f : files) {
            temp++;

            if (f.isFile()) {

                GridFileIcon fileIcon = new GridFileIcon(f.getName(), FileSystemView.getFileSystemView().getSystemIcon(f), f.getAbsolutePath());
                gridIcons.add(fileIcon);
                dummyPanel.add(fileIcon);
            } else {
                gridIcons.add(new GridFolderIcon(f.getName(), FileSystemView.getFileSystemView().getSystemIcon(f), f.getAbsolutePath()));
                dummyPanel.add(gridIcons.get(gridIcons.size() - 1));
            }

            if (temp % 5 == 0) {
        //        buttonsPanel.add(dummyPanel);
                this.add(dummyPanel);
                temp = 0;
             //   panels.add(dummyPanel);
                dummyPanel = new JPanel(new GridLayout(1, 5));
            }

        }
    }

    public void setStartPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setEndPoint(int x, int y) {
        x2 = (x);
        y2 = (y);
    }

    public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
        int px = Math.min(x, x2);
        int py = Math.min(y, y2);
        int pw = Math.abs(x - x2);
        int ph = Math.abs(y - y2);
        g.drawRect(px, py, pw, ph);
        g.fillRect(px, py, pw, ph);

    }

    class MyMouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                setStartPoint(e.getX(), e.getY());
                leftClicked = true;

         //       JOptionPane.showMessageDialog(null,"Click on panel");

                if (popMenu != null) {
                    popMenu.setVisible(false);
                    popMenu = null;
                }
            }

            if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {

                //      if(popMenu!=null)
                //    {
                //        popMenu.setVisible(false);
                //         popMenu=null;
                //     }
              //  singlePopMenu.show(e.getX(), e.getY());
                leftClicked = false;
                //     popMenu.show(null,e.getX(),e.getY());
     //           JOptionPane.showMessageDialog(null, "Right Cl" + e.getX() + "," + e.getY());
                //    popMenu.setVisible(true);
            }

        }
        @Override
        public void mouseDragged(MouseEvent e) {

     //       JOptionPane.showMessageDialog(null,"Dragged");
            if (leftClicked) {
                setEndPoint(e.getX(), e.getY());
                repaint();
            }

        }
        @Override
        public void mouseReleased(MouseEvent e) {
      //      JOptionPane.showMessageDialog(null,"Released");
            if (leftClicked) {
                setEndPoint(e.getX(), e.getY());

                x=0;
                y=0;
                x2=0;
                y2=0;

                repaint();
                leftClicked = false;

            }

        }
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        int alpha = 50; // 50% transparent
        Color myColour = new Color(0, 0, 200, 50);
        g.setColor(myColour);
        drawPerfectRect(g, x, y, x2, y2);








    }


//    class rightClickListener implements org.w3c.dom.events.MouseEvent{
//         public void mouseClicked(MouseEvent e) {
//       int x=e.getX();
//       int y=e.getY();
//
//        if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
//            if(singleChoice)
//            {
//                new View.View.PopMenu(true,x,y);
//            }
//
//            else
//            {
//                new View.View.PopMenu(true,x,y);
//            }
//            // whatever
//        }
//    }

    public void setSingleChoice(boolean singleChoice) {
        this.singleChoice = singleChoice;
    }
}