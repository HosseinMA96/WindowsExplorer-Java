package View;

import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

public class DrawRect extends JPanel {

    private int x, y, x2, y2;
    private boolean singleChoice = true, leftClicked = false;
    PopMenu popMenu;
    private PopMenu singlePopMenu, multPopMenu;

//    public static void main(String[] args) {
//        JFrame f = new JFrame("Draw Box Mouse 2");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setContentPane(new View.View.DrawRect());
//        f.setSize(300, 300);
//        f.setVisible(true);
//    }

    public DrawRect() {
        singlePopMenu = new PopMenu(true, this);
        multPopMenu = new PopMenu(false, this);
        x = y = x2 = y2 = 0; //
   //     JOptionPane.showMessageDialog(null,"This THis");
        MyMouseListener listener = new MyMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
        super.setOpaque(false);

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

        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                setStartPoint(e.getX(), e.getY());
                leftClicked = true;

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
                singlePopMenu.show(e.getX(), e.getY());
                leftClicked = false;
                //     popMenu.show(null,e.getX(),e.getY());
                JOptionPane.showMessageDialog(null, "Right Cl" + e.getX() + "," + e.getY());
                //    popMenu.setVisible(true);
            }

        }

        public void mouseDragged(MouseEvent e) {

            if (leftClicked) {
                setEndPoint(e.getX(), e.getY());
                repaint();
            }

        }

        public void mouseReleased(MouseEvent e) {

            if (leftClicked) {
                setEndPoint(e.getX(), e.getY());

                repaint();
                leftClicked = false;

            }

        }
    }

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