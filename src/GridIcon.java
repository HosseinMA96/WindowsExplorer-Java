import javax.swing.*;
import java.awt.*;

abstract public class GridIcon extends JButton {
    public GridIcon(String text)
    {
        this();
        this.setText(text);

    }

    public GridIcon()
    {

        this.setFocusable(true);
        this.setContentAreaFilled(true);
        this.setOpaque(false);

        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }

}
