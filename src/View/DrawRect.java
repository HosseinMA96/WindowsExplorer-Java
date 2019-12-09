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
    private ArrayList<GridIcon> gridIcons;
    private File[] files;
    private DrawRectChild opaqueDrawRectPanel;
    private boolean childAdded=false;


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

    public DrawRect() {
    //    this.add()
    }

    public DrawRect(File[] F) {

        x = y = x2 = y2 = 0; //

        files = F;

        addButtons();
        handleMouseListeners();
        opaqueDrawRectPanel=new DrawRectChild();
        opaqueDrawRectPanel.setOpaque(false);
        opaqueDrawRectPanel.setBackground(new Color(0,0,0,0));
        opaqueDrawRectPanel.setForeground(new Color(0,0,0,0));


    }

    void addButtons() {
        int numberOfIcons = files.length;

      //  GridLayout gridLayout=new GridLayout(20,1);
        this.setLayout((new GridLayout(20, 1)));
        gridIcons = new ArrayList<>();
        int temp = 0;


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
                //
                this.add(dummyPanel);
                temp = 0;

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

    public void handleMouseListeners()

    {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    setStartPoint(e.getX(), e.getY());
                    //     JOptionPane.showMessageDialog(null,"click"+e.getX()+" "+e.getY());
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

//            @Override
//            public void mouseDragged(MouseEvent e) {
//
//                //       JOptionPane.showMessageDialog(null,"Dragged");
//                if (leftClicked) {
//                    setEndPoint(e.getX(), e.getY());
//                    repaint();
//                }
//                JOptionPane.showMessageDialog(null,"drag"+e.getX()+" "+e.getY());
//            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //      JOptionPane.showMessageDialog(null,"Released");
                if (leftClicked) {
                    setEndPoint(e.getX(), e.getY());

                    x = 0;
                    y = 0;
                    x2 = 0;
                    y2 = 0;
                    //   JOptionPane.showMessageDialog(null,"rel"+e.getX()+" "+e.getY());
                    repaint();
                    leftClicked = false;



                }

            }
        });

        ///////////////////////////


        this.addMouseMotionListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
//                    setStartPoint(e.getX(), e.getY());
//                    //     JOptionPane.showMessageDialog(null,"click"+e.getX()+" "+e.getY());
//                    leftClicked = true;
//
//                    //       JOptionPane.showMessageDialog(null,"Click on panel");
//
//                    if (popMenu != null) {
//                        popMenu.setVisible(false);
//                        popMenu = null;
//                    }
//                }

//                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
//
//                    //      if(popMenu!=null)
//                    //    {
//                    //        popMenu.setVisible(false);
//                    //         popMenu=null;
//                    //     }
//                    //  singlePopMenu.show(e.getX(), e.getY());
//                    leftClicked = false;
//                    //     popMenu.show(null,e.getX(),e.getY());
//                    //           JOptionPane.showMessageDialog(null, "Right Cl" + e.getX() + "," + e.getY());
//                    //    popMenu.setVisible(true);
//                }
//
//            }

            @Override
            public void mouseDragged(MouseEvent e) {

                //       JOptionPane.showMessageDialog(null,"Dragged");
                if (leftClicked) {
                    setEndPoint(e.getX(), e.getY());
                    repaint();

                    childAdded=true;





                }
                //    JOptionPane.showMessageDialog(null,"drag"+e.getX()+" "+e.getY());
            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                //      JOptionPane.showMessageDialog(null,"Released");
//                if (leftClicked) {
//                    setEndPoint(e.getX(), e.getY());
//
//                    x = 0;
//                    y = 0;
//                    x2 = 0;
//                    y2 = 0;
//                    JOptionPane.showMessageDialog(null,"rel"+e.getX()+" "+e.getY());
//                    repaint();
//                    leftClicked = false;
//
//                }
//
//            }
        });


    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        int alpha = 50; // 50% transparent
        Color myColour = new Color(0, 0, 200, 50);
        g.setColor(myColour);
        drawPerfectRect(g, x, y, x2, y2);
    }


    public void setSingleChoice(boolean singleChoice) {
        this.singleChoice = singleChoice;
    }
}