/**
 * A Class that represents Pop menu clicked in the empty space of right side panel
 */

package View;

import javax.swing.*;
import java.awt.*;

public class GridEmptySpacePopMenu extends JPopupMenu {

    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem properties, paste, newFile, newFolder;
    private Component invoker;

    /**
     * Constructor for current class
     * @param copyPressed
     * @param x
     * @param y
     * @param invoker
     */
    public GridEmptySpacePopMenu(boolean copyPressed, int x, int y, Component invoker) {

        this.invoker = invoker;
        paste = new JMenuItem("paste");
        newFile = new JMenuItem("New File");
        newFolder = new JMenuItem("New Folder");
        properties = new JMenuItem("Properties");


        popupMenu.add(newFolder);
        popupMenu.add(newFile);
        popupMenu.add(properties);

        if (copyPressed)
            popupMenu.add(paste);


        popupMenu.setVisible(true);
        popupMenu.show(invoker, x, y);
        popupMenu.setLocation(x, y);
    }

    /**
     * Getter for Properties
     * @return properties
     */
    public JMenuItem getProperties() {
        return properties;
    }

    /**
     * Getter for paste
     * @return paste
     */
    public JMenuItem getPaste() {
        return paste;
    }

    /**
     * Getter for newFile
     * @return paste
     */
    public JMenuItem getNewFile() {
        return newFile;
    }

    /**
     * Getter for newFolder
     * @return newFolder
     */
    public JMenuItem getNewFolder() {
        return newFolder;
    }
}
