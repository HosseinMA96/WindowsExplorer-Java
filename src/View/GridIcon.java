//package View;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Date;
//
//abstract public class GridIcon extends JButton {
//    private Color pressedBackgroundColor = new Color(0,0,255,100);
//    private String shortenedName;
//    private String path;
//    private boolean setSelected = false;
//    private long time;
//
//
//    public GridIcon(String text, Icon icon, String path) {
//        addListener();
//        this.path = path;
//
//        if (text.length() > 9) {
//            shortenedName = text.substring(0, 9);
//            shortenedName += "...";
//        } else
//            shortenedName = text;
//
//        this.setIcon(icon);
//        this.setText(shortenedName);
//        this.setFocusable(false);
//
//
//        this.setVerticalTextPosition(SwingConstants.BOTTOM);
//        this.setHorizontalTextPosition(SwingConstants.CENTER);
//        super.setOpaque(false);
//        super.setContentAreaFilled(false);
//        super.setBorderPainted(false);
//
//
//        this.setPreferredSize(new Dimension(40, 40));
//        this.setMaximumSize(new Dimension(40, 40));
//        this.setVisible(true);
//
//
//    }
//
//    @Override
//    public void paint(Graphics g) {
//        super.paintComponent(g);
//        if (getModel().isPressed() || setSelected) {
//            g.setColor(pressedBackgroundColor);
//        } else {
//            Color color = new Color(0, 0, 0, 0);
//            //   g.setColor(getBackground());
//            g.setColor(color);
//        }
//      //  g.fillRect(0 + getWidth() / 2 - 2 * this.getIcon().getIconWidth() - 10, 0 + getHeight() / 2 - this.getIcon().getIconHeight() - 20, this.getWidth() / 2, this.getHeight() + 20);
//            g.fillRect(0,0,getWidth(),getHeight());
//       // super.paintComponent(g);
//    }
//
//    @Override
//    public void setContentAreaFilled(boolean b) {
//    }
//
//
//    public String getPath() {
//        return path;
//    }
//
//    public Color getPressedBackgroundColor() {
//        return pressedBackgroundColor;
//    }
//
//    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
//        this.pressedBackgroundColor = pressedBackgroundColor;
//    }
//
//    public void setSetSelected(boolean isSelected) {
//        setSelected = isSelected;
//    }
//
//
//    public boolean isSetSelected() {
//        return setSelected;
//    }
//
////
////    void addListener() {
////        this.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                if (setSelected)
////                {
////                    setSelected = false;
////
////                    long temp=new Date().getTime();
////
////                    if(temp-time<500)
////                        JOptionPane.showMessageDialog(null,"Double click");
////                }
////
////
////                else {
////
////                    setSelected = true;
////                    time=new Date().getTime();
////
////                    //     JOptionPane.showMessageDialog(null,"button cord :  x= "+getXMid()+" ,y= "+getYMid());
////                }
////            }
////        });
////    }
//
//
//    double getXMid() {
//        //  return this.getX()+this.getWidth()/2;
//     return this.getLocationOnScreen().getX() + getHeight() / 2;
////        return this.getLocationOnScreen().getX();
//      //  return getX();
//
//    }
//
//    double getYMid() {
//        //  return this.getX()+this.getHeight()/2;
//      return this.getLocationOnScreen().getY() + getWidth() / 2;
//       // return this.getLocationOnScreen().getY();
//       // return getY();
//    }
//
//
//}
//
//
