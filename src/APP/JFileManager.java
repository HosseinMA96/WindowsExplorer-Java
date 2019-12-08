package APP;

import Controller.Controller;
import Model.Model;
import View.View;
import com.sun.org.apache.xpath.internal.operations.Mod;

import javax.swing.*;
import java.io.File;

public class JFileManager {
    static Model model;
    static View view;

    public static void main(String[] args) {
         model=new Model();
        initializeModelWithDefaultSettings();
         view=new View();
        Controller controller=new Controller(model,view);
        controller.initController();


    }

    static void initializeModelWithDefaultSettings()
    {
        model.loadSettings();
        File F = new File(model.getCurrentAddress());

      //  JOptionPane.showMessageDialog(null,"In main : "+model.getCurrentAddress());

        if (!(F.exists() && F.isDirectory())) {

      //      JOptionPane.showMessageDialog(null, "Default address inserted in settings was invalid. Initial address was set to be Drive C");
            model.setCurrentAddress("C:\\");
            // model.upgradeFiles();
        }
    }

}
