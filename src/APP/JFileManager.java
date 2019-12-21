/**
 * This class is main class for the program which we initiate it . has instances of model,view and controll
 */

package APP;

import Controller.Controller;
import Model.Model;
import View.View;
import View.MyProgressBar;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static java.lang.System.exit;

public class JFileManager {
    static Model model;
    static View view;


    public static void main(String[] args) {
        model = new Model();
        initializeModelWithDefaultSettings();

        view = new View();
        Controller controller = new Controller(model, view);
        controller.initController();
        initializeTray();
    }

    /**
     * Initialize system tray
     */
    static void initializeTray() {
        SystemTray tray = SystemTray.getSystemTray();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\settingIcon.png");
        PopupMenu menu = new PopupMenu();


        MenuItem closeItem = new MenuItem("Close");
        menu.add(closeItem);
        TrayIcon icon = new TrayIcon(image, "SystemTray Demo", menu);
        icon.setImageAutoSize(true);


        icon.setImageAutoSize(true);

        try {
            tray.add(icon);
        } catch (Exception e) {
            System.err.println("TrayIcon could not be added.");
        }

        closeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit(0);
            }
        });


        icon.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                view.getFrame().repaint();
                view.getFrame().setVisible(true);
            }
        });


    }

    /**
     * This fucntion loads saved information for our program to load
     */

    static void initializeModelWithDefaultSettings() {
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
