package Controller;

import Model.Model;
import View.View;
import View.GridIcon;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class Controller {
    private Model model;
    private View view;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;


    }

    public void initController() {
        view.getGridDisplay().addActionListener(new GridListener());
        view.getTableDisplay().addActionListener(new ListListener());
        view.getFile_NewFile().addActionListener(new newFileListener());
        view.getFile_SetCurrentForSync().addActionListener(new SetAsSyncPathListener());
        view.getFile_Delete().addActionListener(new Delete());
        view.getFile_NewFolder().addActionListener(new newFolderListener());

        //implement a function for default start situation
        model.setGridDisplay(true);
        view.setCurrentDirectoryFiles(model.getAllFiles());

        if (model.getGridDisplay()) {
            view.setGridDisplay();
        }


    }

    class GridListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setCurrentDirectoryFiles(model.getAllFiles());
            view.setGridDisplay();
            model.setGridDisplay(true);
        }
    }

    class ListListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setListDisplay(model.getCurrentAddress());
            model.setGridDisplay(false);
        }
    }

    class SetAsSyncPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.setAsSynchPath();
        }
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

            if (model.getGridDisplay()) {
                model.upgradeFiles();
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setGridDisplay();
            } else {
                model.upgradeFiles();
                view.setListDisplay(model.getCurrentAddress());

            }

        }
    }


    class newFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.newFile();

            if (model.getGridDisplay()) {
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setGridDisplay();
            } else {
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setListDisplay(model.getCurrentAddress());
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

}
