package Controller;

import Model.Model;
import View.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private Model model;
    private View view;
    private ArrayList<File> coppy;
    boolean cutPressed = false, coppyPressed = false;
    private ArrayList<File> selectedFiles;
    private int currentStatus = 0;


    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        model.upgradeFiles();
    }

    public void initController() {
        view.setAddressTextField(model.getCurrentAddress());
        view.getGridDisplay().addActionListener(new GridListener());
        view.getTableDisplay().addActionListener(new ListListener());
        view.getFile_NewFile().addActionListener(new NewFileListener());
        view.getFile_SetCurrentForSync().addActionListener(new SetAsSyncPathListener());
        view.getFile_Delete().addActionListener(new Delete());
        view.getFile_NewFolder().addActionListener(new NewFolderListener());
        view.getEdit_Rename().addActionListener(new RenameListener());
        view.getUppArrow().addActionListener(new GoUpListener());
        view.getEdit_Copy().addActionListener(new CopyListener());
        view.getEdit_Paste().addActionListener(new PasteListener());
        view.getEdit_Cut().addActionListener(new CutListener());
        view.getHelp_Settings().addActionListener(new SettingsListener());
        view.getHelp_AboutMe().addActionListener(new AboutMeListener());
        view.getHelp_Help().addActionListener(new HelpListener());
        view.getTable().addMouseListener(new TableListener());
        view.getTable().getTableHeader().addMouseListener(new TableHeaderListener());


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
            upgradeView();
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

                    //  super.mousePressed((MouseEvent)e);

//            view.getTable().getTableHeader().addMouseListener(new MouseAdapter() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//
//                    if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
//                        int col = view.getTable().columnAtPoint(e.getPoint());
//                        String name = view.getTable().getColumnName(col);
//                        JOptionPane.showMessageDialog(null, "Single click " + col + " " + name);
//                        //  currentStatus=col;
//                        //      sort(currentStatus );
//
//                    }
//
//                    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && !e.isConsumed()) {
//                        e.consume();
//                        int col = view.getTable().columnAtPoint(e.getPoint());
//                        String name = view.getTable().getColumnName(col);
//                        JOptionPane.showMessageDialog(null, "Double click " + col + " " + name);
//                        //  currentStatus=col;
//                        //      sort(currentStatus);
//
//
//                        for (int i = 0; i < model.getAllFiles().length / 2; i++) {
//                            File temp = model.getAllFiles()[i];
//                            model.getAllFiles()[i] = model.getAllFiles()[model.getAllFiles().length - i - 1];
//                            model.getAllFiles()[model.getAllFiles().length - i - 1] = temp;
//                        }
//
//                    }
//
//                    view.setCurrentDirectoryFiles(model.getAllFiles());
//                    upgradeView();
//
//
//                }
//            });
                    //  view.getTable().addMouseListener(new MouseAdapter()

                            model.setGridDisplay(false);
                            upgradeView();
        }
    }

    class HelpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Help help = new Help();
        }
    }

    class AboutMeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AboutMe aboutMe = new AboutMe();
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
                view.getTable().addMouseListener(new TableListener());
                view.getTable().getTableHeader().addMouseListener(new TableListener());

            }

            view.setAddressTextField(model.getCurrentAddress());
          //  view.getTable().addMouseListener(new TableListener());



    }

    class NewFolderListener implements ActionListener {
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


    class NewFileListener implements ActionListener {
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
            Settings settings = new Settings();

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
                return;
            }
            coppyPressed = true;


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

                        System.out.println(j);
                        if (temp[j].getName().equals(newName)) {

                            if (coppy.get(i).isDirectory()) {
                                newName += "_copy";
                                j = 0;
                                continue;
                            } else {
                                int dotOccurance = newName.length();
                                boolean hasDot = false;

                                for (int k = 0; k < temp[j].getName().length(); k++)
                                    if (temp[j].getName().charAt(k) == '.') {
                                        dotOccurance = k;
                                        hasDot = true;
                                        break;
                                    }

                                String dummy;

                                if (hasDot)
                                    dummy = newName.substring(0, dotOccurance) + "_copy" + newName.substring(dotOccurance, newName.length());

                                else
                                    dummy = newName.substring(0, dotOccurance) + "_copy";

                                newName = dummy;
                                j = 0;
                                continue;


                            }
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
                    coppyPressed = false;

                    for (int i = 0; i < coppy.size(); i++) {
                        model.deleteFile(coppy.get(i));
                    }
                    upgradeView();
                }

            }


        }
    }

    class TableListener implements MouseListener {

        public void mousePressed(MouseEvent mouseEvent) {
            Point point = mouseEvent.getPoint();
            int row = view.getTable().rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && view.getTable().getSelectedRow() != -1 && SwingUtilities.isLeftMouseButton(mouseEvent) && !mouseEvent.isConsumed()) {
                mouseEvent.isConsumed();
                int[] selectedRow = view.getTable().getSelectedRows();


                if (selectedRow.length == 1)
                    open(model.getAllFiles()[selectedRow[0]]);

            }

            if (mouseEvent.getClickCount() == 1 && view.getTable().getSelectedRow() != -1 && SwingUtilities.isRightMouseButton(mouseEvent)) {
                selectedFiles = new ArrayList<>();

                for (int j = 0; j < view.getGridIconArrayList().size(); j++)
                    if (view.getGridIconArrayList().get(j).isSetSelected() == true)
                        selectedFiles.add(new File(view.getGridIconArrayList().get(j).getPath()));

                PopMenu popMenu = null;
                if (selectedFiles.size() == 1)
                    popMenu = new PopMenu(true, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), view.getTable());


                if (selectedFiles.size() > 1)

                    popMenu = new PopMenu(true, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), view.getTable());

                if (popMenu != null) {
                    popMenu.getCopy().addActionListener(new CopyListener());
                    popMenu.getCut().addActionListener(new CutListener());
                    popMenu.getDelete().addActionListener(new Delete());
                    popMenu.getRename().addActionListener(new RenameListener());
                    popMenu.getOpen().addActionListener(new OpenListener());
                    popMenu.getProperties().addActionListener(new PropertiesListener());
                    popMenu.getNewFile().addActionListener(new NewFileListener());
                    popMenu.getNewFolder().addActionListener(new NewFolderListener());
                    popMenu.getPaste().addActionListener(new PasteListener());

                    //popMenu.getProperties()
                }

            }
        }


        public void mouseClicked(MouseEvent event) {
        }

        public void mouseReleased(MouseEvent event) {
        }

        public void mouseEntered(MouseEvent event) {
        }

        public void mouseExited(MouseEvent event) {
        }


    }

    class TableHeaderListener extends MouseAdapter {



        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {

                view.addStatus();
                int col = view.getTable().columnAtPoint(e.getPoint());
                String name = view.getTable().getColumnName(col);

                JOptionPane.showMessageDialog(null, " click " + col + " " + name);

                if (view.getStatus() == 1)
                    JOptionPane.showMessageDialog(null, "Single click " + col + " " + name);


                else
                    JOptionPane.showMessageDialog(null, "double click " + col + " " + name);

                //  currentStatus=col;
                //      sort(currentStatus );

                view.setFeature(col);


                view.setListDisplay(model.getCurrentAddress());

            }


//                view.setCurrentDirectoryFiles(model.getAllFiles());
//                upgradeView();


        }
    }


    class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedFiles != null)
                for (int i = 0; i < selectedFiles.size(); i++)
                    open(selectedFiles.get(i));

            selectedFiles = null;


        }
    }

    class PropertiesListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedFiles != null && selectedFiles.size() == 1) {
                File f = selectedFiles.get(0);
                if (selectedFiles.get(0).isFile())
                    new OptionPane("File", f.getAbsolutePath(), f.length(), new Date(f.lastModified()), 0, 0);

                else {
                    File[] temp = f.listFiles();
                    int filesContained = 0;
                    int foldersContained = 0;

                    if (temp != null)
                        for (int i = 0; i < temp.length; i++)
                            if (temp[i].isFile())
                                filesContained++;
                            else
                                foldersContained++;

                    new OptionPane("File", f.getAbsolutePath(), f.length(), new Date(f.lastModified()), filesContained, foldersContained);

                }

            }


        }
    }

    void open(File f) {

        if (f.isFile()) {
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(null, "Desktop is not available now", "Error", 1);
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (f.exists()) {
                try {
                    desktop.open(f);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Unable to open file", "Error", 1);
                }
            }


            //let's try to open PDF file
//                            file = new File("/Users/pankaj/java.pdf");
//                            if(file.exists()) desktop.open(file);
        } else {

            model.setCurrentAddress(f.getAbsolutePath());
            upgradeView();

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


