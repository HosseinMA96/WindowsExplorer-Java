import javax.swing.*;
import java.awt.*;

abstract public class ListIcon extends JButton {
    public ListIcon(String text)
    {
        this();
        this.setText(text);

    }

    public ListIcon()
    {

        this.setFocusable(true);

        this.setVerticalTextPosition(SwingConstants.CENTER);
        this.setHorizontalTextPosition(SwingConstants.RIGHT);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }

}
