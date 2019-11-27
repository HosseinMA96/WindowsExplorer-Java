import javax.swing.*;

public abstract class ListIcon extends JButton {
    public ListIcon() {
        this.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }

    public ListIcon(String text) {
        this();
        this.setText(text);
    }
}
