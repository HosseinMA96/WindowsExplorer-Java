package View;

import javax.swing.*;
import java.awt.*;

public class GridEmptySpacePopMenu extends  JPopupMenu
{
    //شده باشد(، New Folder ، New File و Properties ظاهر میشود. با انتخاب
    JPopupMenu popupMenu=new JPopupMenu();
    private JMenuItem properties,paste,newFile,newFolder;
    Component invoker;

    public GridEmptySpacePopMenu(boolean copyPressed, int x,int y,Component invoker)
    {

        this.invoker=invoker;
        paste=new JMenuItem("paste");
        newFile=new JMenuItem("New File");
        newFolder=new JMenuItem("New Folder");
        properties=new JMenuItem("Properties");


        popupMenu.add(newFolder);
        popupMenu.add(newFile);
        popupMenu.add(properties);

        if(copyPressed)
            popupMenu.add(paste);




        popupMenu.setVisible(true);
        popupMenu.show(invoker,x,y);
        popupMenu.setLocation(x,y);
        //  System.out.println(x+","+y);
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
