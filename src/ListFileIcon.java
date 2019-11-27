import javax.swing.*;

public class ListFileIcon extends ListIcon {
    public ListFileIcon(String text)
    {
        this();
        this.setText(text);
    }

    public ListFileIcon()
    {
        super();
        this.setIcon(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\fileIcon.png"));

    }
}
