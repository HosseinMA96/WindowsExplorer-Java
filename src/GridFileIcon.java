import javax.swing.*;

public class GridFileIcon extends GridIcon {
    public GridFileIcon(String text)
    {
        this();
        this.setText(text);
    }

    public GridFileIcon()
    {
        super();
        this.setIcon(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\fileIcon.png"));

    }
}
