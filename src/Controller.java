import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private Model model;
    private  View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    public void initController()
    {
        view.getGridDisplay().addActionListener(new GridListener());
        view.getTableDisplay().addActionListener(new ListListener());
    }

    class GridListener implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            view.setCurrentDirectoryFiles(model.getAllFiles());
            view.setGridDisplay();
        }
    }

    class ListListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            view.setCurrentAddress(model.getCurrentAddress());
            view.setListDisplay();
        }
    }

}
