import javax.swing.*;

public class PopMenu extends  JPopupMenu
{
    JPopupMenu popupMenu=new JPopupMenu();
    JMenuItem rename,open,delete,cut,copy,properties;

    public PopMenu(boolean singleChoice)
    {
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

        popupMenu.show(null,200,200);

    }

}
