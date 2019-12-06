package Controller;

import Model.Model;
import View.*;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
    private ArrayList<File> search = new ArrayList<>();
    private int headerCol=0;


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
        view.getFile_Delete().addActionListener(new DeleteListener());
        view.getFile_NewFolder().addActionListener(new NewFolderListener());
        view.getEdit_Rename().addActionListener(new RenameListener());
        view.getUppArrow().addActionListener(new GoUpListener());
        view.getEdit_Copy().addActionListener(new CopyListener());
        view.getEdit_Paste().addActionListener(new PasteListener());
        view.getEdit_Cut().addActionListener(new CutListener());
        view.getHelp_Settings().addActionListener(new SettingsListener());
        view.getHelp_AboutMe().addActionListener(new AboutMeListener());
        view.getHelp_Help().addActionListener(new HelpListener());
        //    view.getTable().addMouseListener(new TableListener());
        //     view.getTable().getTableHeader().addMouseListener(new TableHeaderListener());
        view.getAddressTextField().addActionListener(new ListenToAddressTextField());
        //    view.getTable().setCellSelectionEnabled(true);
        initializeTable();


        //implement a function for default start situation
        model.setGridDisplay(true);
        view.setCurrentDirectoryFiles(model.getAllFiles());
        view.setCurrentAddress(model.getCurrentAddress());

        if (model.getGridDisplay()) {
            view.setGridDisplay();
        }
        //      view.makeLeftTree();

        handleSearchFieldListener();

    }

    void initializeTable() {
        view.getTable().addMouseListener(new TableListener());
        view.getTable().getTableHeader().addMouseListener(new TableHeaderListener());
        view.getNumberOfSelectedLabel().setText("Number of items selected: ");
        //  view.getTable().setCellSelectionEnabled(true);
        // ListSelectionModel cellSelectionModel = view.getTable().getSelectionModel();
        //   cellSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //   view.getTable().setSelectionModel(view.getTable().getSelectionModel().MULTIPLE_INTERVAL_SELECTION)
        view.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedFiles = new ArrayList<>();
                //           String selectedDate=null;

                int[] selectedRow = view.getTable().getSelectedRows();
                int[] selectedColumns = view.getTable().getSelectedColumns();//here has good table.get seelction staff
                view.getNumberOfSelectedLabel().setText("Number of items selected: " + selectedRow.length);

                for (int i = 0; i < selectedRow.length; i++) {
                    selectedFiles.add(new File(model.getAllFiles()[selectedRow[i]].getAbsolutePath()));
                    //      JOptionPane.showMessageDialog(null,currentDirectoryFiles[selectedRow[i]].getAbsolutePath()+"");
                }

            }
        });

        //  view.getTable().setCellSelectionEnabled(true);


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


            switch (selectedFiles.size()) {
                case 1:
                    String newName = JOptionPane.showInputDialog(null, "Enter new name:", "Enter name", 2);
                    model.renameFile(selectedFiles.get(0).getPath(), newName);
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
            view.setListDisplay();

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
//            model.setCurrentAddress(f.getAbsolutePath());
//            model.upgradeFiles();
//            upgradeView();

            view.setCurrentAddress(model.getCurrentAddress());
            model.upgradeFiles();
            view.setCurrentDirectoryFiles(model.getAllFiles());
            view.setListDisplay();
//            view.getTable().addMouseListener(new TableListener());
//            view.getTable().getTableHeader().addMouseListener(new TableListener());
//            view.getTable().setCellSelectionEnabled(true);
            initializeTable();

        }

        view.setAddressTextField(model.getCurrentAddress());
        //  view.getTable().addMouseListener(new TableListener());


    }

    class NewFolderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            model.newFolder();

            if (!model.getGridDisplay()) {
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setListDisplay();

            } else {
                view.setCurrentDirectoryFiles(model.getAllFiles());
                view.setGridDisplay();
            }

            upgradeView();
        }
    }

    ///Only works for grid display!!
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {


            for (int i = 0; i < selectedFiles.size(); i++) {
                //  JOptionPane.showMessageDialog(null,"Selected file : "+selectedFiles.get(i).getAbsolutePath());
                model.deleteFile(selectedFiles.get(i));
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

            if (selectedFiles == null || selectedFiles.size() == 0) {
                JOptionPane.showMessageDialog(null, "At least 1 file must be selected.", "Eror", 2);
            }

            coppy = new ArrayList<>();

            for (int i = 0; i < selectedFiles.size(); i++)
                coppy.add(new File(selectedFiles.get(i).getAbsolutePath()));

            if (coppy.size() == 0) {
                JOptionPane.showMessageDialog(null, "At least 1 file must be selected.", "Eror", 2);
                coppy = null;
                return;
            }

            // JOptionPane.showMessageDialog(null,"Number of coppied : "+selectedFiles.size());
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

                        //     System.out.println(j);
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


            //   Point point = mouseEvent.getPoint();
            // int row = view.getTable().rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && view.getTable().getSelectedRow() != -1 && SwingUtilities.isLeftMouseButton(mouseEvent) && !mouseEvent.isConsumed()) {
                mouseEvent.isConsumed();
                int[] selectedRow = view.getTable().getSelectedRows();


                if (selectedRow.length == 1) {
                    int r = selectedRow[0];
                    //      JOptionPane.showMessageDialog(null,"Number of selecte drows: "+selectedRow.length+"\nr is: "+r);
                    //  model.upgradeFiles();
                    //    JOptionPane.showMessageDialog(null,"model files : "+model.getAllFiles().length);

                    open(model.getAllFiles()[r]);
                }


            }

            if (mouseEvent.getClickCount() == 1 && view.getTable().getSelectedRow() != -1 && SwingUtilities.isRightMouseButton(mouseEvent)) {


                PopMenu popMenu = null;
                if (selectedFiles.size() == 1)
                    popMenu = new PopMenu(true, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), view.getTable());


                if (selectedFiles.size() > 1)
                    popMenu = new PopMenu(false, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), view.getTable());

                if (popMenu != null) {
                    popMenu.getCopy().addActionListener(new CopyListener());
                    popMenu.getCut().addActionListener(new CutListener());
                    popMenu.getDelete().addActionListener(new DeleteListener());
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

                if(headerCol!=col)
                    view.setStatus(0);

                headerCol=col;

                String name = view.getTable().getColumnName(col);

             //   JOptionPane.showMessageDialog(null,"status : "+view.getStatus()+" , col : "+col);


                if (view.getStatus() == 1)
                sort(1,col);


                else
                    sort(0,col);

                //  currentStatus=col;
                //      sort(currentStatus );


                view.setListDisplay();
                initializeTable();


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

        if (f.exists() && f.isFile()) {
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
        }
        if (f.exists() && f.isDirectory()) {
            //        JOptionPane.showMessageDialog(null,"Must be here");
            model.setCurrentAddress(f.getAbsolutePath());
            model.upgradeFiles();
            upgradeView();

        }
    }


    class ListenToAddressTextField implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            File F = new File(view.getAddressTextField().getText());


            if (F.exists() && F.isDirectory()) {
                model.setCurrentAddress(view.getAddressTextField().getText());
                upgradeView();
            }

            if (F.exists() && F.isFile()) {
                open(F);

                try {
                    F = F.getParentFile();

                    model.setCurrentAddress(F.getAbsolutePath());
                    upgradeView();
                } catch (Exception b) {

                }
            }

            if (!F.exists()) {
                view.getAddressTextField().setText(model.getCurrentAddress());
                JOptionPane.showMessageDialog(null, "Invalid file or address", "Eror", 0);
            }

        }
    }

    void handleSearchFieldListener() {
        view.getSearchTextField().getDocument().addDocumentListener(new DocumentListener() {
            private void upgradeLocally() {

                if(view.getSearchTextField().getText().length()!=0)
                {
                    File[] F = new File[search.size()];
                    for (int i = 0; i < search.size(); i++)
                        F[i] = search.get(i);


                    view.setCurrentDirectoryFiles(F);
                }


            //    JOptionPane.showMessageDialog(null,F.length);

                if(view.getSearchTextField().getText().length()==0)
                {
                    view.setCurrentDirectoryFiles(model.getAllFiles());
             //       view.getSearchTextField().setText("Search");
                }

                if(model.getGridDisplay())
                    view.setGridDisplay();

                else
                    view.setListDisplay();


            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                // JOptionPane.showMessageDialog(null,view.getSearchTextField().getDocument()+"");
         //       JOptionPane.showMessageDialog(null,view.getSearchTextField().getDocument() + "");
                search = new ArrayList<>();
                massiveSearch(new File(model.getCurrentAddress()), view.getSearchTextField().getText());
                upgradeLocally();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
             //         JOptionPane.showMessageDialog(null,view.getSearchTextField().getText());
                search = new ArrayList<>();
                massiveSearch(new File(model.getCurrentAddress()),  view.getSearchTextField().getText());
                upgradeLocally();
          //      JOptionPane.showMessageDialog(null,view.getSearchTextField().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // JOptionPane.showMessageDialog(null,view.getSearchTextField().getDocument()+"");
                search = new ArrayList<>();
                massiveSearch(new File(model.getCurrentAddress()),  view.getSearchTextField().getText());
                upgradeLocally();
         //      JOptionPane.showMessageDialog(null,view.getSearchTextField().getText());


            }
        });
    }


    private void massiveSearch(File dir, String name) {
        if(name==null || name.length()==0)
            return;

        if(dir.exists() && dir.isFile())
        {
            if(dir.getName().contains(name))
                search.add(new File(dir.getAbsolutePath()));
        }

        if(dir.exists() && dir.isDirectory())
        {
            File [] temp=dir.listFiles();

            for (int i=0;i<temp.length;i++)
                massiveSearch(temp[i],name);

            if(dir.getName().contains(name))
                search.add(new File(dir.getAbsolutePath()));
        }


    }

    private void sort(int order, int feature) {

        if(feature==0 )
            return;

        int index = 0;

        File [] F=new File[model.getAllFiles().length];

        for (int i=0;i<model.getAllFiles().length;i++)
            F[i]=new File(model.getAllFiles()[i].getAbsolutePath());


        for (int i = 0; i < F.length; i++) {
            for (int j = 1; j < F.length- i; j++) {
                if (compare(F[j - 1], F[j], feature)) {
                    String S = F[j - 1].getAbsolutePath();

                    F[j - 1] = new File(F[j].getAbsolutePath());
                    F[j] = new File(S);
                }

            }
        }

        if (order == 0) {

            for (int i = 0; i < F.length / 2; i++) {
                String temp = F[i].getAbsolutePath();
                F[i] = new File(F[F.length - i - 1].getAbsolutePath());
                F[F.length - i - 1] = new File(temp);
            }
        }

        view.setCurrentDirectoryFiles(F);
    }

    private boolean compare(File A, File B, int feature) {
        /*
        status=1:sort By name
        status=2:sort By size
        status=3:sort By Date
        status=column number
         */


        switch (feature) {
            case 1:
                //if string a is les than b, a.copmareTo(b)is negative
                String a = A.getName(), b = B.getName();


                return (a.compareTo(b) > 0);


            case 3:
                //For date, it is same. if A occurs before B A.compareTo(B) is <0
                Date aa = new Date(A.lastModified()), bb = new Date(B.lastModified());
                return (aa.compareTo(bb) > 0);


            case 2:
                return (A.length() < B.length());

        }
        return false;

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


