package View;

import javax.swing.*;
import java.awt.*;

public class PopMenu extends  JPopupMenu
{
    JPopupMenu popupMenu=new JPopupMenu();
    JMenuItem rename,open,delete,cut,copy,properties;
    Component invoker;

    public PopMenu(boolean singleChoice, Component component)
    {
        invoker=component;

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

        //  popupMenu.show(null,x,y);
      //  System.out.println(x+","+y);
    }

    public void show(int x,int y)
    {
        JOptionPane.showMessageDialog(null,"Be shown MF!!!");
        this.show(invoker,x,y);
        this.setVisible(true);
    }

}
