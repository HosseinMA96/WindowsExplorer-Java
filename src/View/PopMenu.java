package View;

import javax.swing.*;
import java.awt.*;

public class PopMenu extends  JPopupMenu
{
   JPopupMenu popupMenu=new JPopupMenu();
   private JMenuItem rename,open,delete,cut,copy,properties,paste,newFile,newFolder;
    Component invoker;

    public PopMenu(boolean singleChoice,boolean hasPaste, int x,int y,Component invoker)
    {

        this.invoker=invoker;
        delete=new JMenuItem("Delete");
        paste=new JMenuItem("paste");

        cut=new JMenuItem("Cut");
        copy=new JMenuItem("Copy");
        newFile=new JMenuItem("New File");
        newFolder=new JMenuItem("New Folder");
        properties=new JMenuItem("Properties");
        rename=new JMenuItem("Rename");
        open=new JMenuItem("Open");

        popupMenu.add(copy);
        popupMenu.add(cut);
        popupMenu.add(delete);
        popupMenu.add(newFolder);
        popupMenu.add(newFile);

        if(singleChoice)
        {
            popupMenu.add(open);
            popupMenu.add(rename);
            popupMenu.add(properties);

        }

        if(hasPaste)
            popupMenu.add(paste);


        popupMenu.setVisible(true);
        popupMenu.show(invoker,x,y);
        popupMenu.setLocation(x,y);
      //  System.out.println(x+","+y);
    }

    public JMenuItem getRename() {
        return rename;
    }

    public JMenuItem getOpen() {
        return open;
    }

    public JMenuItem getDelete() {
        return delete;
    }

    public JMenuItem getCut() {
        return cut;
    }

    public JMenuItem getCopy() {
        return copy;
    }

    public JMenuItem getProperties() {
        return properties;
    }

    public JMenuItem getPaste() {
        return paste;
    }

    public JMenuItem getNewFile() {
        return newFile;
    }

    public JMenuItem getNewFolder() {
        return newFolder;
    }
}
