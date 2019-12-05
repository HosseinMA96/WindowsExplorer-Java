package Controller;

import Model.Model;
import View.*;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    private Model model;
    private View view;
    private ArrayList<File> coppy;
    boolean cutPressed = false;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        model.upgradeFiles();
    }

    public void initController() {
        view.setAddressTextField(model.getCurrentAddress());
        view.getGridDisplay().addActionListener(new GridListener());
        view.getTableDisplay().addActionListener(new ListListener());
        view.getFile_NewFile().addActionListener(new newFileListener());
        view.getFile_SetCurrentForSync().addActionListener(new SetAsSyncPathListener());
        view.getFile_Delete().addActionListener(new Delete());
        view.getFile_NewFolder().addActionListener(new newFolderListener());
        view.getEdit_Rename().addActionListener(new RenameListener());
        view.getUppArrow().addActionListener(new GoUpListener());
        view.getEdit_Copy().addActionListener(new CopyListener());
        view.getEdit_Paste().addActionListener(new PasteListener());
        view.getEdit_Cut().addActionListener(new CutListener());
        view.getHelp_Settings().addActionListener(new SettingsListener());
        view.getHelp_AboutMe().addActionListener(new AboutMeListener());
        view.getHelp_Help().addActionListener(new HelpListener());

        //implement a function for default start situation
        model.setGridDisplay(true);
        view.setCurrentDirectoryFiles(model.getAllFiles());
        view.setCurrentAddress(model.getCurrentAddress());

        if (model.getGridDisplay()) {
            view.setGridDisplay();
        }
  //      view.makeLeftTree();

    }

    class GridListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setCurrentDirectoryFiles(model.getAllFiles());
            view.setGridDisplay();
            model.setGridDisplay(true);
        }
    }

    class RenameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ArrayList<GridIcon> gridIcons = view.getGridIconArrayList();

            switch (gridIcons.size()) {
                case 1:
                    String newName = JOptionPane.showInputDialog(null, "Enter new name:", "Enter name", 2);
                    model.renameFile(gridIcons.get(0).getPath(), newName);
                    upgradeView();

                    break;


                default:
                    JOptionPane.showMessageDialog(null, "Exactly file must be selected", "Eror", 3);
                    break;
            }
        }
    }

    class ListListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setListDisplay(model.getCurrentAddress());
            model.setGridDisplay(false);
        }
    }

    class HelpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
          Help help=new Help();
        }
    }

    class AboutMeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AboutMe aboutMe=new AboutMe();
        }
    }

    class SetAsSyncPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.setAsSynchPath();
        }
    }

    void upgradeView() {

        if (model.getGridDisplay()) {
            model.upgradeFiles();
            view.setCurrentDirectoryFiles(model.getAllFiles());
            view.setCurrentAddress(model.getCurrentAddress());
            view.setGridDisplay();


        } else {
            model.upgradeFiles();
            view.setCurrentAddress(model.getCurrentAddress());
            view.setListDisplay(model.getCurrentAddress());

        }

        view.setAddressTextField(model.getCurrentAddress());
    }

    class newFolderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.newFolder();

            if (!model.getGridDisplay()) {
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setListDisplay(model.getCurrentAddress());

            } else {
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setGridDisplay();
            }
        }
    }

    ///Only works for grid display!!
    class Delete implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ArrayList<GridIcon> gridIcons = view.getGridIconArrayList();

            for (int i = 0; i < gridIcons.size(); i++) {
                if (gridIcons.get(i).isSetSelected()) {
                    model.deleteFile(new File(gridIcons.get(i).getPath()));
                    gridIcons.get(i).setSetSelected(false);
                    gridIcons.remove(i);
                    i--;


                }
            }
            upgradeView();
        }
    }


    class newFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.newFile();

            upgradeView();

        }
    }


    class GoUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.goToParent();
            upgradeView();
        }
    }


    class SettingsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Settings settings=new Settings();

        }
    }

    class CutListener extends CopyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            cutPressed = true;

        }
    }

    class CopyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (view.getGridIconArrayList() == null || view.getGridIconArrayList().size() == 0) {
                JOptionPane.showMessageDialog(null, "At least 1 file must be selected.", "Eror", 2);
            }

            coppy = new ArrayList<>();

            for (int i = 0; i < view.getGridIconArrayList().size(); i++) {
                if (view.getGridIconArrayList().get(i).isSetSelected())
                    coppy.add(new File(view.getGridIconArrayList().get(i).getPath()));
            }

            if (coppy.size() == 0) {
                JOptionPane.showMessageDialog(null, "At least 1 file must be selected.", "Eror", 2);
                coppy = null;
            }


        }
    }

    class PasteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (coppy == null || coppy.size() == 0)
                JOptionPane.showMessageDialog(null, "Nothing to paste", "Eror", 2);

            else {
                for (int i = 0; i < coppy.size(); i++) {
                    String newName = coppy.get(i).getName();
                    File[] temp = (new File(model.getCurrentAddress())).listFiles();

                    for (int j = 0; j < temp.length; j++) {
                        if (temp[j].getName().equals(newName)) {
                            newName += "_copy";
                            j = -1;
                            continue;
                        }
                    }

                    try {
                        //  System.out.println("From "+coppy.get(i).getAbsolutePath()+" To "+model.getCurrentAddress()+"\\"+coppy.get(i).getName());
                        Model.pasteFile(coppy.get(i).getAbsolutePath(), model.getCurrentAddress() + "\\" + newName);
                    } catch (Exception ex) {
                        //   JOptionPane.showMessageDialog(null,"Pasting failed\n"+ex.,"Error",2);
                        JOptionPane.showMessageDialog(null, "Failed to paste " + coppy.get(i).getAbsolutePath(), "Eror", 1);
                    }

                    upgradeView();

                }

                if (cutPressed) {
                    cutPressed = false;

                    for (int i = 0; i < coppy.size(); i++) {
                        model.deleteFile(coppy.get(i));
                    }
                    upgradeView();
                }

            }


        }
    }
}
//
//    class DrawRectRightClickListener extends MouseAdapter {
//
//        public void mousePressed(MouseEvent e) {
//            if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
//                drawRect.setStartPoint(e.getX(), e.getY());
//                drawRect.setLeftClicked(true);
//
//                if (drawRect.get != null) {
//                    popMenu.setVisible(false);
//                    popMenu = null;
//                }
//            }
//
//            if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
//
//                if (popMenu != null) {
//                    popMenu.setVisible(false);
//
//                }
//                popMenu = new View.View.PopMenu(singleChoice);
//                leftClicked = false;
//                popMenu.show(null, e.getX(), e.getY());
//                popMenu.setVisible(true);
//                //     popMenu.setVisible(true);
//            }
//
//        }
//
//        public void mouseDragged(MouseEvent e) {
//
//            if (leftClicked) {
//                setEndPoint(e.getX(), e.getY());
//                repaint();
//            }
//
//        }
//
//        public void mouseReleased(MouseEvent e) {
//
//            if (leftClicked) {
//                setEndPoint(e.getX(), e.getY());
//
//                repaint();
//                leftClicked = false;
//
//            }
//
//        }
//    }


