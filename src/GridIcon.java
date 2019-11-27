import javax.swing.*;

abstract public class GridIcon extends JButton {
    public GridIcon(String text)
    {
        this();
        this.setText(text);

    }

    public GridIcon()
    {
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }

}
