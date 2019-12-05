package View;

import javax.swing.*;
import java.awt.*;

public class PopMenu extends  JPopupMenu
{
   JPopupMenu popupMenu=new JPopupMenu();
   private JMenuItem rename,open,delete,cut,copy,properties;
    Component invoker;

    public PopMenu(boolean singleChoice, int x,int y,Component invoker)
    {

        this.invoker=invoker;
        delete=new JMenuItem("Delete");
        cut=new JMenuItem("Cut");
        copy=new JMenuItem("Copy");
        properties=new JMenuItem("Properties");
        rename=new JMenuItem("Rename");
        open=new JMenuItem("Open");

        popupMenu.add(copy);
        popupMenu.add(cut);
        popupMenu.add(delete);

        if(singleChoice)
        {
            popupMenu.add(open);
            popupMenu.add(rename);
            popupMenu.add(properties);

        }


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
}
