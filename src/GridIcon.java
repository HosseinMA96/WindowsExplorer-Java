import javax.swing.*;
import java.awt.*;

abstract public class GridIcon extends JButton {
    private  Color pressedBackgroundColor=Color.blue;
    private String shortenedName;


    public GridIcon(String text,Icon icon)
    {
        if(text.length()>9)
        {
            shortenedName=text.substring(0,9);
            shortenedName+="...";
        }

        else
            shortenedName=text;

        this.setIcon(icon);
        this.setText(shortenedName);
        this.setFocusable(false);


        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
          super.setOpaque(false);
        super.setContentAreaFilled(false);
       super.setBorderPainted(false);

      //  super.setPreferredSize(new Dimension(60,60));

    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isPressed()) {
            g.setColor(pressedBackgroundColor);
        }  else {
            g.setColor(getBackground());
        }
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }




    public Color getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public void setPressedBackgroundColor(Color pressedBackgroundColor) {
        this.pressedBackgroundColor = pressedBackgroundColor;
    }
}


