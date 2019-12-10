package Controller;

import Model.Model;
import View.*;


import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class Controller {
    private  Model myModel;
    private View view;
    private ArrayList<File> coppy;
    boolean cutPressed = false, coppyPressed = false;
    private ArrayList<File> selectedFiles;
    private int currentStatus = 0;
    private ArrayList<File> search = new ArrayList<>();
    private int headerCol = 0;
    private FrameKeyListener frameKeyListener;
    private boolean controlPressed = false;
    private DrawRect drawRect;
    private  ArrayList<GridIcon> gridIcons;

    // private MyDragDropListener myDragDropListener;


    public Controller(Model myModel, View view) {
        this.myModel = myModel;

        this.view = view;

        myModel.upgradeFiles();

    }

    public void initController() {


        handleFrameKeyListener();


        view.setAddressTextField(myModel.getCurrentAddress());
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
        //     myModel.setGridDisplay(true);
        //   view.setCurrentDirectoryFiles(myModel.getAllFiles());
        //   view.setCurrentAddress(myModel.getCurrentAddress());


        //      view.makeLeftTree();

        handleSearchFieldListener();
        upgradeView();


        //  MyDragDropListener myDragDropListener=new MyDragDropListener();
        //  new DropTarget(view.getFrame(),myDragDropListener);

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

        view.getTable().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectedFiles = null;
                view.getNumberOfSelectedLabel().setText("Number of items selected: ");
            }
        });

        view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedFiles = new ArrayList<>();
                //           String selectedDate=null;

                int[] selectedRow = view.getTable().getSelectedRows();
                int[] selectedColumns = view.getTable().getSelectedColumns();//here has good table.get seelction staff
                view.getNumberOfSelectedLabel().setText("Number of items selected: " + selectedRow.length);

                for (int i = 0; i < selectedRow.length; i++) {
                    selectedFiles.add(new File(myModel.getAllFiles()[selectedRow[i]].getAbsolutePath()));
                    //      JOptionPane.showMessageDialog(null,currentDirectoryFiles[selectedRow[i]].getAbsolutePath()+"");
                }

            }

        });

        if(view.getRightScrollPane()==null)
            view.setRightScrollPane(new JScrollPane());

        view.getRightScrollPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e))
                {
                 //   JOptionPane.showMessageDialog(null,"Right click on panel");
                    GridEmptySpacePopMenu gridEmptySpacePopMenu=new GridEmptySpacePopMenu(coppyPressed,e.getXOnScreen(),e.getYOnScreen(),view.getRightScrollPane());
                    gridEmptySpacePopMenu.getPaste().addActionListener(new PasteListener());
                    gridEmptySpacePopMenu.getNewFolder().addActionListener(new NewFolderListener());
                    gridEmptySpacePopMenu.getNewFile().addActionListener(new NewFileListener());

                    selectedFiles=new ArrayList<>();
                    selectedFiles.add(new File(myModel.getCurrentAddress()));
                    gridEmptySpacePopMenu.getProperties().addActionListener(new PropertiesListener());
                    selectedFiles=null;
                    view.getNumberOfSelectedLabel().setText("number of items selected:");
                    //Overlord
                }

                else
                {
                    selectedFiles=null;
                    view.getNumberOfSelectedLabel().setText("number of items selected:");

                }


            }
        });
        handleFrameKeyListener();
        //  view.getTable().setCellSelectionEnabled(true);


    }


    void handleFrameKeyListener() {
        view.getFrame().setFocusable(true);
        view.getFrame().requestFocus();


        if (frameKeyListener != null) {
            view.getFrame().removeKeyListener(frameKeyListener);
            frameKeyListener = new FrameKeyListener();
            view.getFrame().addKeyListener(frameKeyListener);
        } else {
            frameKeyListener = new FrameKeyListener();
            view.getFrame().addKeyListener(frameKeyListener);
        }


        new DropTarget(view.getFrame(), new DropTargetListener() {
            @Override
            public void drop(DropTargetDropEvent event) {

                // Accept copy drops
                event.acceptDrop(DnDConstants.ACTION_COPY);


                try {
                    java.util.List list = (java.util.List) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    File file = (File) list.get(0);
                    // JOptionPane.showMessageDialog(null,list.size());
                    // JOptionPane.showMessageDialog(null,file.getAbsolutePath());

                    ArrayList<File> temp = new ArrayList<>();

                    boolean copyisEmpty = (coppy == null || coppy.size() == 0);

                    if (!copyisEmpty)
                        for (int i = 0; i < coppy.size(); i++)
                            temp.add(new File(coppy.get(i).getAbsolutePath()));

                    coppy = new ArrayList<>();
                    coppy.add(new File(file.getAbsolutePath()));

                    ArrayList<String> newName = findNewNames();
                    //    JOptionPane.showMessageDialog(null,newName.get(0));

                    Model.pasteFile(file.getAbsolutePath(), myModel.getCurrentAddress() + "\\" + newName.get(0));

                    coppy = new ArrayList<>();

                    if (!copyisEmpty)
                        for (int i = 0; i < temp.size(); i++)
                            coppy.add(new File(temp.get(i).getAbsolutePath()));

                    else
                        coppy = new ArrayList<>();

                    temp = null;

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Unable to perform drag and drop");
                }


                // Inform that the drop is complete
                event.dropComplete(true);

            }

            @Override
            public void dragEnter(DropTargetDragEvent event) {
            }

            @Override
            public void dragExit(DropTargetEvent event) {
            }

            @Override
            public void dragOver(DropTargetDragEvent event) {
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent event) {
            }
        });


    }

    class GridListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            view.setCurrentDirectoryFiles(myModel.getAllFiles());
            view.setCurrentAddress(myModel.getCurrentAddress());
            if(myModel.getGridDisplay()==false)
            {
                selectedFiles=null;
                view.getNumberOfSelectedLabel().setText("number of items selected:");
            }
            myModel.setGridDisplay(true);
            upgradeView();
        }
    }

    class RenameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {


            switch (selectedFiles.size()) {
                case 1:
                    String newName = JOptionPane.showInputDialog(null, "Enter new name:", "Enter name", 2);
                    myModel.renameFile(selectedFiles.get(0).getPath(), newName);
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
            if(myModel.getGridDisplay()==true)
            {
                selectedFiles=null;
                view.getNumberOfSelectedLabel().setText("number of items selected:");
            }

            view.setListDisplay();
            myModel.setGridDisplay(false);
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
            myModel.setAsSynchPath();
        }
    }

    void upgradeView() {


        handleFrameKeyListener();

        if (myModel.getGridDisplay()) {
            myModel.upgradeFiles();
            view.setCurrentDirectoryFiles(myModel.getAllFiles());
            view.setCurrentAddress(myModel.getCurrentAddress());
            view.setGridDisplay();
            drawRect = new DrawRect(myModel.getAllFiles());

            if (view.hasPreview()) {
                view.getFrame().remove(view.getSplitPane());
            }
            view.setRightScrollPane(new JScrollPane(drawRect));
            view.setLeftScrollPane(new JScrollPane(view.getLeftTree()));
            view.setSplitPane(new JSplitPane(SwingConstants.VERTICAL, view.getLeftScrollPane(), view.getRightScrollPane()));
            view.getFrame().add(view.getSplitPane());
            view.getLeftScrollPane().setVisible(true);
            view.getLeftTree().setVisible(true);
            view.getRightScrollPane().setVisible(true);
            drawRect.setVisible(true);
            view.getSplitPane().setVisible(true);
            view.addMenuBars();
            view.getRightScrollPane().revalidate();
            view.getFrame().revalidate();
            view.getFrame().setVisible(true);




        } else {
//            myModel.setCurrentAddress(f.getAbsolutePath());
//            myModel.upgradeFiles();
//            upgradeView();

            view.setCurrentAddress(myModel.getCurrentAddress());
            myModel.upgradeFiles();
            view.setCurrentDirectoryFiles(myModel.getAllFiles());
            view.setListDisplay();
//            view.getTable().addMouseListener(new TableListener());
//            view.getTable().getTableHeader().addMouseListener(new TableListener());
//            view.getTable().setCellSelectionEnabled(true);
            initializeTable();

        }

        view.setAddressTextField(myModel.getCurrentAddress());
        //  view.getTable().addMouseListener(new TableListener());


    }

    class NewFolderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.newFolder();

            if (!myModel.getGridDisplay()) {
                view.setCurrentDirectoryFiles(myModel.getAllFiles());
                view.setCurrentAddress(myModel.getCurrentAddress());
                //       view.setListDisplay();

            } else {
                view.setCurrentDirectoryFiles(myModel.getAllFiles());
                view.setCurrentAddress(myModel.getCurrentAddress());
                //     view.setGridDisplay();
            }

            upgradeView();
        }
    }

    ///Only works for grid display!!
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {


            for (int i = 0; i < selectedFiles.size(); i++) {
                //  JOptionPane.showMessageDialog(null,"Selected file : "+selectedFiles.get(i).getAbsolutePath());
                myModel.deleteFile(selectedFiles.get(i));
            }


            upgradeView();
        }
    }


    class NewFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.newFile();

            upgradeView();

        }
    }


    class GoUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.goToParent();
            upgradeView();
        }
    }


    class SettingsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //     JOptionPane.showMessageDialog(null,"Before "+myModel.getCurrentAddress());
            Settings settings = new Settings();
            //      JOptionPane.showMessageDialog(null,"mid "+myModel.getCurrentAddress());
            myModel.loadSettings();
            //      JOptionPane.showMessageDialog(null,"after "+myModel.getCurrentAddress());
            handleSettingsListener(settings);


        }
    }

    void handleSettingsListener(Settings settings) {
        settings.getInitialAddressTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setInitialAddress(settings.getInitialAddressTextField().getText());
                myModel.writeSettingsToFile();
            }
        });


        settings.getReceivedFileAddress().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setReceivedFileAddress(settings.getReceivedFileAddress().getText());
                myModel.writeSettingsToFile();
            }
        });


        settings.getRemoteComputerAddressTextField().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setRemoteComputerAddress(settings.getRemoteComputerAddressTextField().getText());
                myModel.writeSettingsToFile();
            }
        });

        settings.getRemoteComputerPort().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setRemoteComputerPort(settings.getRemoteComputerPort().getText());
                myModel.writeSettingsToFile();
            }
        });

        settings.getLookNFeel().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //   JOptionPane.showMessageDialog(null,"address before LF : "+myModel.getCurrentAddress());
                myModel.setLookAndFeel(settings.getLookNFeel().getSelectedItem() + "");
                view.setLookAndFeel(myModel.getLookAndFeel());

                upgradeView();
                myModel.writeSettingsToFile();
            }
        });

        settings.getGridDisplayFormatCheckBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setGridDisplay(true);
                settings.getTableDisplayFormatCheckBox().setSelected(false);
                settings.getGridDisplayFormatCheckBox().setSelected(true);
                myModel.writeSettingsToFile();
            }
        });

        settings.getTableDisplayFormatCheckBox().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setGridDisplay(false);
                settings.getGridDisplayFormatCheckBox().setSelected(false);
                settings.getTableDisplayFormatCheckBox().setSelected(true);
                myModel.writeSettingsToFile();
            }
        });

        settings.getSyncInterval().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setSyncInterval(settings.getSyncInterval().getSelectedItem() + "");
                myModel.writeSettingsToFile();

            }
        });

        settings.getMaxFlashbacks().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myModel.setFlashBackNumber(settings.getMaxFlashbacks().getSelectedItem() + "");
                myModel.writeSettingsToFile();
            }
        });

//        this lousy thing does not work!!
//        myModel.loadSettings();

        settings.getInitialAddressTextField().setText(myModel.getInitialAddress());
        settings.getReceivedFileAddress().setText(myModel.getReceivedFileAddress());
        settings.getRemoteComputerAddressTextField().setText(myModel.getRemoteComputerAddress());
        settings.getRemoteComputerPort().setText(myModel.getRemoteComputerPort());


        if (myModel.getGridDisplay()) {
            settings.getTableDisplayFormatCheckBox().setSelected(false);
            settings.getGridDisplayFormatCheckBox().setSelected(true);
        } else {
            settings.getTableDisplayFormatCheckBox().setSelected(true);
            settings.getGridDisplayFormatCheckBox().setSelected(false);
        }

        settings.getSyncInterval().setSelectedItem(myModel.getSyncInterval());
        settings.getMaxFlashbacks().setSelectedItem(myModel.getSyncInterval());
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
                ArrayList<String> newNames = findNewNames();
                for (int i = 0; i < newNames.size(); i++)
                    try {
                        //  System.out.println("From "+coppy.get(i).getAbsolutePath()+" To "+myModel.getCurrentAddress()+"\\"+coppy.get(i).getName());
                        Model.pasteFile(coppy.get(i).getAbsolutePath(), myModel.getCurrentAddress() + "\\" + newNames.get(i));
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
                    myModel.deleteFile(coppy.get(i));
                }
                upgradeView();
            }

        }


    }


    ArrayList<String> findNewNames() {
        ArrayList<String> ans = new ArrayList<String>();

        for (int i = 0; i < coppy.size(); i++) {
            String newName = coppy.get(i).getName();
            File[] temp = (new File(myModel.getCurrentAddress())).listFiles();

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
            ans.add(newName);
        }
        return ans;
    }

    class TableListener implements MouseListener {

        public void mouseClicked(MouseEvent mouseEvent) {


            //   Point point = mouseEvent.getPoint();
            // int row = view.getTable().rowAtPoint(point);
            if (mouseEvent.getClickCount() == 2 && view.getTable().getSelectedRow() != -1 && SwingUtilities.isLeftMouseButton(mouseEvent) && !mouseEvent.isConsumed()) {
                mouseEvent.isConsumed();
                int[] selectedRow = view.getTable().getSelectedRows();


                if (selectedRow.length == 1) {
                    int r = selectedRow[0];
                    //      JOptionPane.showMessageDialog(null,"Number of selecte drows: "+selectedRow.length+"\nr is: "+r);
                    //  myModel.upgradeFiles();
                    //    JOptionPane.showMessageDialog(null,"myModel files : "+myModel.getAllFiles().length);

                    try {
                        open(myModel.getAllFiles()[r]);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Access is denied to the file", "Eror", 0);
                        myModel.goToParent();
                        upgradeView();


                    }

                }


            }

            if (mouseEvent.getClickCount() == 1 && view.getTable().getSelectedRow() != -1 && SwingUtilities.isRightMouseButton(mouseEvent)) {


                PopMenu popMenu = null;

                if (selectedFiles != null)
                    if (selectedFiles.size() == 1)
                        popMenu = new PopMenu(true, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), view.getTable());

                if (selectedFiles != null)
                    if (selectedFiles.size() > 1)
                        popMenu = new PopMenu(false, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), view.getTable());

                if (selectedFiles != null)
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
            handleFrameKeyListener();
        }


        public void mousePressed(MouseEvent event) {
            handleFrameKeyListener();
        }

        public void mouseReleased(MouseEvent event) {
            handleFrameKeyListener();
        }

        public void mouseEntered(MouseEvent event) {
            handleFrameKeyListener();
        }

        public void mouseExited(MouseEvent event) {
            handleFrameKeyListener();
        }


    }

    class TableHeaderListener extends MouseAdapter {


        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {


                view.addStatus();


                int col = view.getTable().columnAtPoint(e.getPoint());

                if (headerCol != col)
                    view.setStatus(0);

                headerCol = col;

                String name = view.getTable().getColumnName(col);

                //   JOptionPane.showMessageDialog(null,"status : "+view.getStatus()+" , col : "+col);


                if (view.getStatus() == 1)
                    sort(1, col);


                else
                    sort(0, col);

                //  currentStatus=col;
                //      sort(currentStatus );


                view.setListDisplay();
                initializeTable();


            }


//                view.setCurrentDirectoryFiles(myModel.getAllFiles());
//                upgradeView();

            handleFrameKeyListener();
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

                    new OptionPane("Folder", f.getAbsolutePath(), f.length(), new Date(f.lastModified()), filesContained, foldersContained);

                }

            }


        }
    }

    public void open(File f) {

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
            myModel.setCurrentAddress(f.getAbsolutePath());
            myModel.upgradeFiles();
            view.setCurrentDirectoryFiles(myModel.getAllFiles());
            view.setCurrentAddress(myModel.getCurrentAddress());
            upgradeView();

        }

        selectedFiles=null;
    }


    class ListenToAddressTextField implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            File F = new File(view.getAddressTextField().getText());


            if (F.exists() && F.isDirectory()) {
                myModel.setCurrentAddress(view.getAddressTextField().getText());
                upgradeView();
            }

            if (F.exists() && F.isFile()) {
                open(F);

                try {
                    F = F.getParentFile();

                    myModel.setCurrentAddress(F.getAbsolutePath());
                    upgradeView();
                } catch (Exception b) {

                }
            }

            if (!F.exists()) {
                view.getAddressTextField().setText(myModel.getCurrentAddress());
                JOptionPane.showMessageDialog(null, "Invalid file or address", "Eror", 0);
            }

        }
    }

    void handleDrawRect() {

    }

    void addGridIConListener(GridIcon gridIcon) {

    }

    void handleSearchFieldListener() {
        view.getSearchTextField().getDocument().addDocumentListener(new DocumentListener() {
            private void upgradeLocally() {

                if (view.getSearchTextField().getText().length() != 0) {
                    File[] F = new File[search.size()];
                    for (int i = 0; i < search.size(); i++)
                        F[i] = search.get(i);


                    view.setCurrentDirectoryFiles(F);
                }


                //    JOptionPane.showMessageDialog(null,F.length);

                if (view.getSearchTextField().getText().length() == 0) {
                    view.setCurrentDirectoryFiles(myModel.getAllFiles());
                    //       view.getSearchTextField().setText("Search");
                }

                if (myModel.getGridDisplay())
                    view.setGridDisplay();

                else
                    view.setListDisplay();


            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                // JOptionPane.showMessageDialog(null,view.getSearchTextField().getDocument()+"");
                //       JOptionPane.showMessageDialog(null,view.getSearchTextField().getDocument() + "");
                search = new ArrayList<>();
                massiveSearch(new File(myModel.getCurrentAddress()), view.getSearchTextField().getText());
                upgradeLocally();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //         JOptionPane.showMessageDialog(null,view.getSearchTextField().getText());
                search = new ArrayList<>();
                massiveSearch(new File(myModel.getCurrentAddress()), view.getSearchTextField().getText());
                upgradeLocally();
                //      JOptionPane.showMessageDialog(null,view.getSearchTextField().getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // JOptionPane.showMessageDialog(null,view.getSearchTextField().getDocument()+"");
                search = new ArrayList<>();
                massiveSearch(new File(myModel.getCurrentAddress()), view.getSearchTextField().getText());
                upgradeLocally();
                //      JOptionPane.showMessageDialog(null,view.getSearchTextField().getText());


            }
        });
    }


    private void massiveSearch(File dir, String name) {
        if (name == null || name.length() == 0)
            return;

        if (dir.exists() && dir.isFile()) {
            if (dir.getName().contains(name))
                search.add(new File(dir.getAbsolutePath()));
        }

        if (dir.exists() && dir.isDirectory()) {
            File[] temp = dir.listFiles();

            for (int i = 0; i < temp.length; i++)
                massiveSearch(temp[i], name);

            if (dir.getName().contains(name))
                search.add(new File(dir.getAbsolutePath()));
        }


    }

    private void sort(int order, int feature) {

        if (feature == 0)
            return;

        int index = 0;

        File[] F = new File[myModel.getAllFiles().length];

        for (int i = 0; i < myModel.getAllFiles().length; i++)
            F[i] = new File(myModel.getAllFiles()[i].getAbsolutePath());


        for (int i = 0; i < F.length; i++) {
            for (int j = 1; j < F.length - i; j++) {
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

    class FrameKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            {
                if (e.getKeyCode() == KeyEvent.VK_DELETE && !controlPressed) {
                    if (selectedFiles != null)
                        for (int i = 0; i < selectedFiles.size(); i++)
                            myModel.deleteFile(selectedFiles.get(i));

                    selectedFiles = null;

                    //       JOptionPane.showMessageDialog(null, "DELL");
                    upgradeView();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER && !controlPressed) {
                    for (int i = 0; i < selectedFiles.size(); i++)
                        if (selectedFiles.get(i).isFile())
                            open(selectedFiles.get(i));


                    if (selectedFiles.size() == 1 && selectedFiles.get(0).isDirectory())
                        open(selectedFiles.get(0));
                    //      JOptionPane.showMessageDialog(null, "Enter");
                }

                if (e.getKeyCode() == KeyEvent.VK_UP && !controlPressed) {
                    ///////////IMPEMENT FOR GRID

                    if (myModel.getGridDisplay() == false) {
                        int row = view.getTable().getSelectedRow();


                        if (row != 0)
                            view.getTable().setRowSelectionInterval(row - 1, row - 1);

                        else
                            view.getTable().setRowSelectionInterval(myModel.getAllFiles().length - 1, myModel.getAllFiles().length - 1);

                        //  JOptionPane.showMessageDialog(null, "Arrow UP");
                        view.getTable().scrollRectToVisible(view.getTable().getCellRect(view.getTable().getSelectedRow(), 0, true));
                    }

                    else
                    {

                    }
                }


                if (e.getKeyCode() == KeyEvent.VK_DOWN && !controlPressed) {
                    ///////////IMPEMENT FOR GRID

                    if (myModel.getGridDisplay() == false) {

                        int[] selectedRows = view.getTable().getSelectedRows();
                        int row = selectedRows[selectedRows.length - 1];


                        if (row != myModel.getAllFiles().length - 1)
                            view.getTable().setRowSelectionInterval(row + 1, row + 1);


                        else
                            view.getTable().setRowSelectionInterval(0, 0);

                        //       JOptionPane.showMessageDialog(null, "Arrow Down");
                        view.getTable().scrollRectToVisible(view.getTable().getCellRect(view.getTable().getSelectedRow(), 0, true));
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !controlPressed) {
                    myModel.goToParent();
                    upgradeView();

                }

                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    controlPressed = true;
                    //   JOptionPane.showMessageDialog(null,"control pressed");

                }

                if (e.getKeyCode() == KeyEvent.VK_C && controlPressed) {
                    coppy = new ArrayList<>();
                    //    JOptionPane.showMessageDialog(null, "C pressed");
                    for (int i = 0; i < selectedFiles.size(); i++)
                        coppy.add(selectedFiles.get(i));


                }

                if (e.getKeyCode() == KeyEvent.VK_V && controlPressed) {

                    //   JOptionPane.showMessageDialog(null, "V pressed");

                    if (coppy != null) {

                        ArrayList<String> newNames = findNewNames();
                        for (int i = 0; i < newNames.size(); i++) {
                            try {
                                Model.pasteFile(coppy.get(i).getAbsolutePath(), myModel.getCurrentAddress() + "\\" + newNames.get(i));
                            } catch (Exception f) {
                                JOptionPane.showMessageDialog(null, "Unable to paste");
                            }


                        }

                        if (cutPressed) {
                            for (int i = 0; i < coppy.size(); i++)
                                myModel.deleteFile(coppy.get(i));

                            coppy = null;

                        }
                        upgradeView();
                    }


                }


                if (e.getKeyCode() == KeyEvent.VK_X && controlPressed) {
                    coppy = new ArrayList<>();
                    cutPressed = true;

                    for (int i = 0; i < selectedFiles.size(); i++)
                        coppy.add(selectedFiles.get(i));


                }

                if (e.getKeyCode() == KeyEvent.VK_F2 && !controlPressed) {

                    switch (selectedFiles.size()) {
                        default:
                            JOptionPane.showMessageDialog(null, "Exactly file must be selected", "Eror", 3);
                            break;


                        case 1:
                            String newName = JOptionPane.showInputDialog(null, "Enter new name:", "Enter name", 2);
                            myModel.renameFile(selectedFiles.get(0).getPath(), newName);
                            upgradeView();

                            break;

                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_F && controlPressed) {
                    myModel.newFile();
                    upgradeView();
                }

                if (e.getKeyCode() == KeyEvent.VK_N && controlPressed) {
                    myModel.newFolder();
                    upgradeView();
                }


            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                controlPressed = false;
                //     JOptionPane.showMessageDialog(null, "control released");

            }

        }

    }

    class GridIcon extends JButton {
        private Color pressedBackgroundColor = new Color(0, 0, 255, 100);
        private String shortenedName;
        private String path;
        private boolean setSelected = false;
        private long time;


        public GridIcon(String text, Icon icon, String path) {
            //    addListener();
            this.path = path;

            if (text.length() > 9) {
                shortenedName = text.substring(0, 9);
                shortenedName += "...";
            } else
                shortenedName = text;

            this.setIcon(icon);
            this.setText(shortenedName);
            this.setFocusable(false);


            this.setVerticalTextPosition(SwingConstants.BOTTOM);
            this.setHorizontalTextPosition(SwingConstants.CENTER);
            super.setOpaque(false);
            super.setContentAreaFilled(false);
            super.setBorderPainted(false);


            this.setPreferredSize(new Dimension(40, 40));
            this.setMaximumSize(new Dimension(40, 40));
           this.setVisible(true);
            addListener();


        }

        void addListener() {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setSelected=true;

                    if(selectedFiles ==null)
                    selectedFiles=new ArrayList<>();

                    else
                        selectedFiles.add(new File(path));

                    repaint();

                    if(SwingUtilities.isRightMouseButton(e))
                    {

                        if(selectedFiles.size()==1)
                        {
                            PopMenu popMenu=new PopMenu(true,false,e.getXOnScreen(),e.getYOnScreen(),drawRect);
                            popMenu.getRename().addActionListener(new RenameListener());
                            popMenu.getOpen().addActionListener(new OpenListener());
                            popMenu.getDelete().addActionListener(new DeleteListener());
                            popMenu.getCut().addActionListener(new CutListener());
                            popMenu.getCopy().addActionListener(new CopyListener());
                            popMenu.getProperties().addActionListener(new PropertiesListener());

                        }

                        else
                        {
                            PopMenu popMenu=new PopMenu(false,false,e.getXOnScreen(),e.getYOnScreen(),drawRect);
                            popMenu.getDelete().addActionListener(new DeleteListener());
                            popMenu.getCut().addActionListener(new CutListener());
                            popMenu.getCopy().addActionListener(new CopyListener());
                            popMenu.getProperties().addActionListener(new PropertiesListener());
                        }



                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                }
            });
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (setSelected) {
                        setSelected = false;

                        long temp = new Date().getTime();

                        if (temp - time < 500) {

                          //  JOptionPane.showMessageDialog(null, "Double click");
                            open(new File(path));

                        }


                    } else {


                        if(!controlPressed)
                        {
                            for (int i=0;i<gridIcons.size();i++)
                            {
                                gridIcons.get(i).setSetSelected(false);
                                gridIcons.get(i).repaint();
                            }


                 //           drawRect.repaint();

                       //     JOptionPane.showMessageDialog(null,"Control not selected");
                      //      JOptionPane.showMessageDialog(null,gridIcons.size());

                        }


                     //   drawRect.repaint();

                        setSelected = true;
                        repaint();

                        if(selectedFiles!=null && selectedFiles.size()!=0)
                        selectedFiles.add(new File(path));

                        else
                        {
                            selectedFiles=new ArrayList<>();
                            selectedFiles.add(new File(path));
                        }


                        time = new Date().getTime();



                        //     JOptionPane.showMessageDialog(null,"button cord :  x= "+getXMid()+" ,y= "+getYMid());
                    }
                }
            });
        }


        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            if (getModel().isPressed() || setSelected) {
                g.setColor(pressedBackgroundColor);
            } else {
                Color color = new Color(0, 0, 0, 0);
                //   g.setColor(getBackground());
                g.setColor(color);
            }
            //  g.fillRect(0 + getWidth() / 2 - 2 * this.getIcon().getIconWidth() - 10, 0 + getHeight() / 2 - this.getIcon().getIconHeight() - 20, this.getWidth() / 2, this.getHeight() + 20);
            g.fillRect(0, 0, getWidth(), getHeight());
            // super.paintComponent(g);
        }

        @Override
        public void setContentAreaFilled(boolean b) {
        }


        public String getPath() {
            return path;
        }

        public Color getPressedBackgroundColor() {
            return pressedBackgroundColor;
        }

        public void setPressedBackgroundColor(Color pressedBackgroundColor) {
            this.pressedBackgroundColor = pressedBackgroundColor;
        }

        public void setSetSelected(boolean isSelected) {
            setSelected = isSelected;

            if(isSelected)
            {
                if(selectedFiles!= null && selectedFiles.size()>0)
                {
                    selectedFiles.add(new File(path));
                }

                else
                {
                    selectedFiles=new ArrayList<>();
                    selectedFiles.add(new File(path));
                }
            }

            else
            {
                if(selectedFiles != null)
                {
                    for (int i=0;i<selectedFiles.size();i++)
                        if(selectedFiles.get(i).getAbsolutePath().equals(path))
                        {
                            selectedFiles.remove(i);
                            return;
                        }
                }
            }
        }


        public boolean isSetSelected() {
            return setSelected;
        }


        double getXMid() {
            //  return this.getX()+this.getWidth()/2;

            return this.getLocationOnScreen().getX() + getHeight() / 2;
//        return this.getLocationOnScreen().getX();
            //  return getX();

        }

        double getYMid() {
            //  return this.getX()+this.getHeight()/2;
            return this.getLocationOnScreen().getY() + getWidth() / 2;
            // return this.getLocationOnScreen().getY();
            // return getY();
        }


    }


    class DrawRect extends JPanel {

        private int x, y, x2, y2, xOnScreen1, yOnScreen1, xOnScreen2, yOnScreen2;
        private boolean singleChoice = true, leftClicked = false;
        PopMenu popMenu;

        private File[] files;


        public void setFiles(File[] files) {
            this.files = files;
        }


        public DrawRect(File[] F) {

       //     JOptionPane.showMessageDialog(null, F.length);
            x = y = x2 = y2 = 0; //

            files = F;

            gridIcons = new ArrayList<>();
            addButtons();
            handleMouseListeners();
            this.setVisible(true);


        }

        void addButtons() {

            int numberOfFiles = files.length;
            int numberOfRows = numberOfFiles * 2 / 5 + 4;
            this.setLayout((new GridLayout(numberOfRows + 5, 1, 70, 20)));
            gridIcons = new ArrayList<>();
            int temp = 0, helper = 1;


            JPanel dummyPanel = new JPanel(new GridLayout(1, 5, 70, 20));


            for (File f : files) {
                if (helper == 1 && temp % 5 == 0) {
                    this.add(dummyPanel);
                    temp = 0;

                    dummyPanel = new JPanel(new GridLayout(1, 5, 70, 20));
                    helper = 0;
                }
                temp++;

                if (f.isFile()) {

                    GridIcon fileIcon = new GridIcon(f.getName(), FileSystemView.getFileSystemView().getSystemIcon(f), f.getAbsolutePath());
                    gridIcons.add(fileIcon);
                    dummyPanel.add(fileIcon);
                } else {
                    gridIcons.add(new GridIcon(f.getName(), FileSystemView.getFileSystemView().getSystemIcon(f), f.getAbsolutePath()));
                    dummyPanel.add(gridIcons.get(gridIcons.size() - 1));
                }

                gridIcons.get(gridIcons.size() - 1).repaint();

                if (temp % 5 == 0 && temp != 0 && helper == 0) {
                    //
                    this.add(dummyPanel);

                    dummyPanel = new JPanel(new GridLayout(1, 5, 70, 20));


                    helper = 1;
                }

            }

            if(temp % 5 !=0)
                while(temp %5 >0)
                {
                    temp++;
                    dummyPanel.add(new JPanel());
                }
                this.add(dummyPanel);

                this.revalidate();
        }

        public void setStartPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setEndPoint(int x, int y) {
            x2 = (x);
            y2 = (y);
        }

        public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
            int px = Math.min(x, x2);
            int py = Math.min(y, y2);
            int pw = Math.abs(x - x2);
            int ph = Math.abs(y - y2);
            g.drawRect(px, py, pw, ph);
            g.fillRect(px, py, pw, ph);
            //   checkButtonsContained(px,py,px+pw,py+ph);

        }

        public void handleMouseListeners() {
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    //  JOptionPane.showMessageDialog(null,"click");
                    for (int i = 0; i < gridIcons.size(); i++)
                        gridIcons.get(i).setSetSelected(false);
                    repaint();

                    if(SwingUtilities.isRightMouseButton((e)))
                    {

                        GridEmptySpacePopMenu gridEmptySpacePopMenu=null;

                        if(coppyPressed)
                            gridEmptySpacePopMenu=new GridEmptySpacePopMenu(true,e.getXOnScreen(),e.getYOnScreen(),drawRect);

                        else
                            gridEmptySpacePopMenu=new GridEmptySpacePopMenu(false,e.getXOnScreen(),e.getYOnScreen(),drawRect);

                        gridEmptySpacePopMenu.getNewFile().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                myModel.newFile();
                            }
                        });

                        gridEmptySpacePopMenu.getNewFolder().addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                myModel.newFolder();
                            }
                        });

                        gridEmptySpacePopMenu.getPaste().addActionListener(new PasteListener());
                        gridEmptySpacePopMenu.getProperties().addActionListener(new ActionListener() {
                            PropertiesListener propertiesListener=new PropertiesListener();

                            @Override
                            public void actionPerformed(ActionEvent e)
                            {
                                selectedFiles=new ArrayList<>();
                                selectedFiles.add(new File(myModel.getCurrentAddress()));
                                propertiesListener.actionPerformed(e);
                                selectedFiles=null;
                            }
                        });

                    }


                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                        setStartPoint(e.getX(), e.getY());
                        xOnScreen1 = e.getXOnScreen();
                        yOnScreen1 = e.getYOnScreen();

                        leftClicked = true;


                        if (popMenu != null) {
                            popMenu.setVisible(false);
                            popMenu = null;
                        }

                        //        JOptionPane.showMessageDialog(null,"Boom in press");


                    }

                    if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {


                        leftClicked = false;

                    }


                }

//            }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (leftClicked) {
                        setEndPoint(e.getX(), e.getY());
                        xOnScreen2 = e.getXOnScreen();
                        yOnScreen2 = e.getYOnScreen();

                        x = 0;
                        y = 0;
                        x2 = 0;
                        y2 = 0;

//                    xOnScreen1=0;
//                    xOnScreen2=0;
//                    yOnScreen1=0;
//                    yOnScreen2=0;


                        repaint();
                        leftClicked = false;


                    }

                }
            });


            this.addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {

                    //       JOptionPane.showMessageDialog(null,"Dragged");
                    if (leftClicked) {
                        setEndPoint(e.getX(), e.getY());
                        xOnScreen2 = e.getXOnScreen();
                        yOnScreen2 = e.getYOnScreen();


                        repaint();


                        checkButtonsContained(Math.min(xOnScreen1, xOnScreen2), Math.min(yOnScreen1, yOnScreen2), Math.max(xOnScreen1, xOnScreen2), Math.max(yOnScreen1, yOnScreen2));
                        repaint();


                    }

                }

            });

        }

        public ArrayList<GridIcon> getGridIcons() {
            return gridIcons;
        }

        @Override
        public void paint(Graphics g) {
            try {
                super.paintChildren(g);
                super.paintComponents(g);
                super.paint(g);
                int alpha = 50; // 50% transparent
                Color myColour = new Color(0, 0, 200, 50);
                g.setColor(myColour);
                drawPerfectRect(g, x, y, x2, y2);
                //      checkButtonsContained(Math.min(xOnScreen1, xOnScreen2), Math.min(yOnScreen1, yOnScreen2), Math.max(xOnScreen1, xOnScreen2), Math.max(yOnScreen1, yOnScreen2));
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Bingo");
            }

        }

        void checkButtonsContained(int xmin, int ymin, int xmax, int ymax) {
            if (gridIcons != null) {
                selectedFiles=null;
                for (int i = 0; i < gridIcons.size(); i++) {
                    if ((gridIcons.get(i).getXMid() >= xmin && gridIcons.get(i).getXMid() <= xmax) && (gridIcons.get(i).getYMid() >= ymin && gridIcons.get(i).getYMid() <= ymax)) {
                        gridIcons.get(i).setSetSelected(true);
                        selectedFiles.add(new File(gridIcons.get(i).getPath()));

                    }

                    else
                        gridIcons.get(i).setSetSelected(false);

                }
                view.setNumberOfSelectedLabel(new JLabel("number of items selected: "+selectedFiles.size()));
                //  repaint();
            }

        }

        public void setSingleChoice(boolean singleChoice) {
            this.singleChoice = singleChoice;
        }
    }


}
//package View;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Date;

