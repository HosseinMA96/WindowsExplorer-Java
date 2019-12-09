package View;

import com.sun.org.apache.regexp.internal.REProgram;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

public class DrawRect extends JPanel {

    private int x, y, x2, y2,xOnScreen1,yOnScreen1,xOnScreen2,yOnScreen2;
    private boolean singleChoice = true, leftClicked = false;
    PopMenu popMenu;
    private PopMenu singlePopMenu, multPopMenu;
    private ArrayList<GridIcon> gridIcons;
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

        x = y = x2 = y2 = 0; //

        files = F;

        addButtons();
        handleMouseListeners();


    }

    void addButtons() {
        int numberOfIcons = files.length;

        //  GridLayout gridLayout=new GridLayout(20,1);
        this.setLayout((new GridLayout(20, 1)));
        gridIcons = new ArrayList<>();
        int temp = 0, helper = 0;


        JPanel dummyPanel = new JPanel(new GridLayout(1, 5, 50, 0));


        for (File f : files) {
            if (helper == 1) {
                this.add(dummyPanel);
                temp = 0;

                dummyPanel = new JPanel(new GridLayout(1, 5, 50, 0));
                helper = 0;
            }
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
                //
                this.add(dummyPanel);
                temp = 0;

                dummyPanel = new JPanel(new GridLayout(1, 5, 50, 0));

                helper = 1;
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
        //   checkButtonsContained(px,py,px+pw,py+ph);

    }

    public void handleMouseListeners() {
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
            //  JOptionPane.showMessageDialog(null,"click");
                for(int i=0;i<gridIcons.size();i++)
                    gridIcons.get(i).setSetSelected(false);
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    setStartPoint(e.getX(), e.getY());
                    xOnScreen1=e.getXOnScreen();
                    yOnScreen1=e.getYOnScreen();

                    leftClicked = true;


                    if (popMenu != null) {
                        popMenu.setVisible(false);
                        popMenu = null;
                    }

            //        JOptionPane.showMessageDialog(null,"Boom in press");


                }

                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {


                    leftClicked = false;

                }


            }

//            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (leftClicked) {
                    setEndPoint(e.getX(), e.getY());
                    xOnScreen2=e.getXOnScreen();
                    yOnScreen2=e.getYOnScreen();

                    x = 0;
                    y = 0;
                    x2 = 0;
                    y2 = 0;

//                    xOnScreen1=0;
//                    xOnScreen2=0;
//                    yOnScreen1=0;
//                    yOnScreen2=0;



                    repaint();
                    leftClicked = false;


                }

            }
        });


        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {

                //       JOptionPane.showMessageDialog(null,"Dragged");
                if (leftClicked) {
                    setEndPoint(e.getX(), e.getY());
                    xOnScreen2=e.getXOnScreen();
                    yOnScreen2=e.getYOnScreen();


                    repaint();


                    checkButtonsContained(Math.min(xOnScreen1, xOnScreen2), Math.min(yOnScreen1, yOnScreen2), Math.max(xOnScreen1, xOnScreen2), Math.max(yOnScreen1, yOnScreen2));
                    repaint();


                }

            }

        });

    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);
        int alpha = 50; // 50% transparent
        Color myColour = new Color(0, 0, 200, 50);
        g.setColor(myColour);
        drawPerfectRect(g, x, y, x2, y2);
        checkButtonsContained(Math.min(xOnScreen1, xOnScreen2), Math.min(yOnScreen1, yOnScreen2), Math.max(xOnScreen1, xOnScreen2), Math.max(yOnScreen1, yOnScreen2));

    }

    void checkButtonsContained(int xmin, int ymin, int xmax, int ymax) {
        for (int i = 0; i < gridIcons.size(); i++) {
            if ((gridIcons.get(i).getXMid() >= xmin && gridIcons.get(i).getXMid() <= xmax) && (gridIcons.get(i).getYMid() >= ymin && gridIcons.get(i).getYMid() <= ymax))
                gridIcons.get(i).setSetSelected(true);

            else
                gridIcons.get(i).setSetSelected(false);

        }
      //  repaint();

    }

    public void setSingleChoice(boolean singleChoice) {
        this.singleChoice = singleChoice;
    }
}