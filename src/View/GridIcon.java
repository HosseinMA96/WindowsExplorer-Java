package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract public class GridIcon extends JButton {
    private Color pressedBackgroundColor = Color.blue;
    private String shortenedName;
    private String path;
    private boolean setSelected = false;


    public GridIcon(String text, Icon icon, String path) {
        this.addActionListener(new ButtonListener());
        this.path = path;

        if (text.length() > 9) {
            shortenedName = text.substring(0, 9);
            shortenedName += "...";
        } else
            shortenedName = text;

        this.setIcon(icon);
        this.setText(shortenedName);
        this.setFocusable(false);


        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        super.setOpaque(false);
        super.setContentAreaFilled(false);
        super.setBorderPainted(false);





    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed() || setSelected) {
            g.setColor(pressedBackgroundColor);
        } else {
            Color color=new Color(0,0,0,0);
         //   g.setColor(getBackground());
            g.setColor(color);
        }
        g.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }


    public String getPath() {
        return path;
    }

    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    public void setSetSelected(boolean isSelected) {
        setSelected = isSelected;
    }

    public boolean isSetSelected() {
        return setSelected;
    }

    class ButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (setSelected)
                setSelected = false;

            else
                setSelected = true;

        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
       // setForeground(Color.WHITE);
    }


}


