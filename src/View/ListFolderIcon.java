package View;

import View.ListIcon;

import javax.swing.*;

public class ListFolderIcon extends ListIcon {
    public ListFolderIcon(String text)
    {
        this();
        this.setText(text);
    }

    public ListFolderIcon()
    {
        super();
        this.setIcon(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\folderIcon.png"));

    }
}
