/**
 * A Class that represents the pop menu shown where we click in a Grid Icon or a row of table
 */
package View;

import javax.swing.*;
import java.awt.*;

public class PopMenu extends JPopupMenu {
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem rename, open, delete, cut, copy, properties, paste, newFile, newFolder;
    private Component invoker;

    /**
     * Constructor for this class
     * @param singleChoice
     * @param hasPaste
     * @param x
     * @param y
     * @param invoker
     */
    public PopMenu(boolean singleChoice, boolean hasPaste, int x, int y, Component invoker) {

        this.invoker = invoker;
        delete = new JMenuItem("Delete");
        paste = new JMenuItem("paste");

        cut = new JMenuItem("Cut");
        copy = new JMenuItem("Copy");
        newFile = new JMenuItem("New File");
        newFolder = new JMenuItem("New Folder");
        properties = new JMenuItem("Properties");
        rename = new JMenuItem("Rename");
        open = new JMenuItem("Open");

        popupMenu.add(copy);
        popupMenu.add(cut);
        popupMenu.add(delete);
        popupMenu.add(newFolder);
        popupMenu.add(newFile);

        if (singleChoice) {
            popupMenu.add(open);
            popupMenu.add(rename);
            popupMenu.add(properties);

        }

        if (hasPaste)
            popupMenu.add(paste);


        popupMenu.setVisible(true);
        popupMenu.show(invoker, x, y);
        popupMenu.setLocation(x, y);

    }

    /**
     * Getter for rename
     * @return
     */
    public JMenuItem getRename() {
        return rename;
    }

    /**
     * Getter for open
     * @return  open
     */
    public JMenuItem getOpen() {
        return open;
    }

    /**
     * Getter for delete
     * @return
     */
    public JMenuItem getDelete() {
        return delete;
    }

    /**
     * Getter for cut
     * @return cut
     */
    public JMenuItem getCut() {
        return cut;
    }

    /**
     * Getter for copy
     * @return
     */
    public JMenuItem getCopy() {
        return copy;
    }

    /**
     * Getter for properties
     * @return
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
     * @return
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
