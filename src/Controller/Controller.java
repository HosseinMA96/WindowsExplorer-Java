/**
 * A Class to Represent controller. Action listeners are added in this class. When user interacts with the GUI (the myView class) it triggers listeners in this class
 * So that we are able to change the model and the myView accordingly
 */

package Controller;

import Memento.CareTaker;
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
    private Model myModel;
    private View myView;
    private ArrayList<File> coppy;
    boolean cutPressed = false, coppyPressed = false;
    private ArrayList<File> selectedFiles;
    private int currentGridIconRow, currentGridIconColumn;
    private ArrayList<File> search = new ArrayList<>();
    private int headerCol = 0;
    private FrameKeyListener frameKeyListener;
    private boolean controlPressed = false;
    private DrawRect drawRect;
    private ArrayList<GridIcon> gridIcons;


    /**
     * Constructor for this class
     *
     * @param myModel
     * @param myView
     */
    public Controller(Model myModel, View myView) {
        this.myModel = myModel;

        this.myView = myView;

        myModel.setCareTaker(new CareTaker(myModel, myView, this, myModel.getCurrentAddress(), 1));

        myModel.upgradeFiles();

    }

    /**
     * A Method to initialize controller and add add action listeners to view files, buttons and menus
     */
    public void initController() {


        handleFrameKeyListener();


        myView.setAddressTextField(myModel.getCurrentAddress());
        myView.getGridDisplay().addActionListener(new GridListener());
        myView.getTableDisplay().addActionListener(new ListListener());
        myView.getFile_NewFile().addActionListener(new NewFileListener());
        myView.getFile_SetCurrentForSync().addActionListener(new SetAsSyncPathListener());
        myView.getFile_Delete().addActionListener(new DeleteListener());
        myView.getFile_NewFolder().addActionListener(new NewFolderListener());
        myView.getEdit_Rename().addActionListener(new RenameListener());
        myView.getUppArrow().addActionListener(new GoUpListener());
        myView.getEdit_Copy().addActionListener(new CopyListener());
        myView.getEdit_Paste().addActionListener(new PasteListener());
        myView.getEdit_Cut().addActionListener(new CutListener());
        myView.getHelp_Settings().addActionListener(new SettingsListener());
        myView.getHelp_AboutMe().addActionListener(new AboutMeListener());
        myView.getHelp_Help().addActionListener(new HelpListener());
        myView.getAddressTextField().addActionListener(new ListenToAddressTextField());
        initializeTable();


        handleSearchFieldListener();
        upgradeView();

    }

    /**
     * A Method to initialize table and add Listeners to it
     */
    void initializeTable() {
        myView.getTable().addMouseListener(new TableListener());

        myView.getTable().getTableHeader().addMouseListener(new TableHeaderListener());
        myView.getNumberOfSelectedLabel().setText("Number of items selected: ");
        myView.getTable().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        myView.getTable().addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                selectedFiles = null;
                myView.getNumberOfSelectedLabel().setText("Number of items selected: ");
            }
        });

        myView.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedFiles = new ArrayList<>();
                //           String selectedDate=null;

                int[] selectedRow = myView.getTable().getSelectedRows();
                int[] selectedColumns = myView.getTable().getSelectedColumns();//here has good table.get seelction staff
                myView.getNumberOfSelectedLabel().setText("Number of items selected: " + selectedRow.length);

                for (int i = 0; i < selectedRow.length; i++) {
                    selectedFiles.add(new File(myModel.getAllFiles()[selectedRow[i]].getAbsolutePath()));
                    //      JOptionPane.showMessageDialog(null,currentDirectoryFiles[selectedRow[i]].getAbsolutePath()+"");
                }

            }

        });

        if (myView.getRightScrollPane() == null)
            myView.setRightScrollPane(new JScrollPane());

        myView.getRightScrollPane().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    //   JOptionPane.showMessageDialog(null,"Right click on panel");
                    GridEmptySpacePopMenu gridEmptySpacePopMenu = new GridEmptySpacePopMenu(coppyPressed, e.getXOnScreen(), e.getYOnScreen(), myView.getRightScrollPane());
                    gridEmptySpacePopMenu.getPaste().addActionListener(new PasteListener());
                    gridEmptySpacePopMenu.getNewFolder().addActionListener(new NewFolderListener());
                    gridEmptySpacePopMenu.getNewFile().addActionListener(new NewFileListener());

                    selectedFiles = new ArrayList<>();
                    selectedFiles.add(new File(myModel.getCurrentAddress()));
                    gridEmptySpacePopMenu.getProperties().addActionListener(new PropertiesListener());
                    selectedFiles = null;
                    myView.getNumberOfSelectedLabel().setText("number of items selected:");
                    //Overlord
                } else {
                    selectedFiles = null;
                    myView.getNumberOfSelectedLabel().setText("number of items selected:");

                }


            }
        });
        handleFrameKeyListener();

    }

    /**
     * A method to add keyListenrs to the frame. Also handles drag and drop operation
     */
    void handleFrameKeyListener() {
        myView.getFrame().setFocusable(true);
        myView.getFrame().requestFocus();


        if (frameKeyListener != null) {
            myView.getFrame().removeKeyListener(frameKeyListener);
            frameKeyListener = new FrameKeyListener();
            myView.getFrame().addKeyListener(frameKeyListener);
        } else {
            frameKeyListener = new FrameKeyListener();
            myView.getFrame().addKeyListener(frameKeyListener);
        }


        new DropTarget(myView.getFrame(), new DropTargetListener() {
            @Override
            public void drop(DropTargetDropEvent event) {

                // Accept copy drops
                event.acceptDrop(DnDConstants.ACTION_COPY);


                try {
                    java.util.List list = (java.util.List) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    File file = (File) list.get(0);


                    ArrayList<File> temp = new ArrayList<>();

                    boolean copyisEmpty = (coppy == null || coppy.size() == 0);

                    if (!copyisEmpty)
                        for (int i = 0; i < coppy.size(); i++)
                            temp.add(new File(coppy.get(i).getAbsolutePath()));

                    coppy = new ArrayList<>();
                    coppy.add(new File(file.getAbsolutePath()));

                    ArrayList<String> newName = findNewNames();


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
                upgradeView();

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

    /**
     * An innerclass that represents listener added to gridDisplay button
     */
    class GridListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myView.setCurrentDirectoryFiles(myModel.getAllFiles());
            myView.setCurrentAddress(myModel.getCurrentAddress());
            if (myModel.getGridDisplay() == false) {
                selectedFiles = null;
                myView.getNumberOfSelectedLabel().setText("number of items selected:");
            }
            myModel.setGridDisplay(true);
            upgradeView();
        }
    }

    /**
     * An innerclass that represents listener added to rename button
     */
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

    /**
     * An innerclass that represents listener added to listDisplay button
     */
    class ListListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (myModel.getGridDisplay() == true) {
                selectedFiles = null;
                myView.getNumberOfSelectedLabel().setText("number of items selected:");
            }

            myView.setListDisplay();
            myModel.setGridDisplay(false);
            upgradeView();
        }
    }

    /**
     * An innerclass that represents listener added to help item
     */
    class HelpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Help help = new Help();
        }
    }

    /**
     * An innerclass that represents listener added to abouMe feature
     */
    class AboutMeListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            AboutMe aboutMe = new AboutMe();
        }
    }

    /**
     * An innerclass that represents listener added to setAsSyncPath feature
     */
    class SetAsSyncPathListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.setAsSynchPath();
        }
    }

    /**
     * A Method to upgrade the view, invoked when a change made in model
     */
    public void upgradeView() {


        handleFrameKeyListener();

        if (myModel.getGridDisplay()) {
            myModel.upgradeFiles();
            myView.setCurrentDirectoryFiles(myModel.getAllFiles());
            myView.setCurrentAddress(myModel.getCurrentAddress());
            myView.setGridDisplay();
            drawRect = new DrawRect(myModel.getAllFiles());

            if (myView.hasPreview()) {
                myView.getFrame().remove(myView.getSplitPane());
            }
            myView.setRightScrollPane(new JScrollPane(drawRect));
            myView.setLeftScrollPane(new JScrollPane(myView.getLeftTree()));
            myView.setSplitPane(new JSplitPane(SwingConstants.VERTICAL, myView.getLeftScrollPane(), myView.getRightScrollPane()));
            myView.getFrame().add(myView.getSplitPane());
            myView.getLeftScrollPane().setVisible(true);
            myView.getLeftTree().setVisible(true);
            myView.getRightScrollPane().setVisible(true);
            drawRect.setVisible(true);
            myView.getSplitPane().setVisible(true);
            myView.addMenuBars();
            myView.getRightScrollPane().revalidate();
            myView.getFrame().revalidate();
            myView.getFrame().setVisible(true);


        } else {


            myView.setCurrentAddress(myModel.getCurrentAddress());
            myModel.upgradeFiles();
            myView.setCurrentDirectoryFiles(myModel.getAllFiles());
            myView.setListDisplay();
            initializeTable();

        }

        myView.setAddressTextField(myModel.getCurrentAddress());


        if (selectedFiles == null)
            myView.getNumberOfSelectedLabel().setText("number of items selected: ");

        else
            myView.getNumberOfSelectedLabel().setText("number of items selected: " + selectedFiles.size());

    }

    /**
     * An innerclass that represents listener added to newFolder item
     */
    class NewFolderListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.newFolder();

            if (!myModel.getGridDisplay()) {
                myView.setCurrentDirectoryFiles(myModel.getAllFiles());
                myView.setCurrentAddress(myModel.getCurrentAddress());


            } else {
                myView.setCurrentDirectoryFiles(myModel.getAllFiles());
                myView.setCurrentAddress(myModel.getCurrentAddress());

            }

            upgradeView();
        }
    }

    /**
     * A method to upgrade selectedFiles
     */
    public void upgradeSelectedFiles() {
        if (gridIcons == null)
            return;

        selectedFiles = new ArrayList<>();

        for (int i = 0; i < gridIcons.size(); i++)
            if (gridIcons.get(i).isSetSelected())
                selectedFiles.add(new File(gridIcons.get(i).path));

        if (selectedFiles == null)
            myView.getNumberOfSelectedLabel().setText("number of items selected: ");

        else
            myView.getNumberOfSelectedLabel().setText("number of items selected: " + selectedFiles.size());

    }

    /**
     * An inner class  Listener for Delete feature
     */
    class DeleteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {


            for (int i = 0; i < selectedFiles.size(); i++) {

                myModel.deleteFile(selectedFiles.get(i));
            }


            upgradeView();
        }
    }

    /**
     * An inner class  Listener for New File feature
     */
    class NewFileListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.newFile();

            upgradeView();

        }
    }

    /**
     * An inner class  Listener for Go Up feature
     */
    class GoUpListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            myModel.goToParent();
            upgradeView();
        }
    }

    /**
     * An inner class  Listener for Settings feature
     */
    class SettingsListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            Settings settings = new Settings();
            if (myModel.getFirstTimeAddress() != null) {
                settings.getInitialAddressTextField().setText(myModel.getFirstTimeAddress());
            }


            //       myModel.loadSettings();

            handleSettingsListener(settings);


        }
    }

    /**
     * A Method to add listeners to different items of Settings using anonymous listener classes
     *
     * @param settings
     */
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

                myModel.setLookAndFeel(settings.getLookNFeel().getSelectedItem() + "");
                myView.setLookAndFeel(myModel.getLookAndFeel());

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
                myModel.getCareTaker().setNumberOfFlashBacks(Integer.parseInt(settings.getMaxFlashbacks().getSelectedItem() + ""));
                myModel.writeSettingsToFile();
            }
        });

        if (myModel.getInitialAddress() == null)
            settings.getInitialAddressTextField().setText(myModel.getFirstTimeAddress());

        else
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

    /**
     * An inner class  Listener for Cut feature
     */
    class CutListener extends CopyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e);
            cutPressed = true;

        }
    }

    /**
     * An inner class  Listener for Copy feature
     */
    class CopyListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            if (selectedFiles == null || selectedFiles.size() == 0) {
                JOptionPane.showMessageDialog(null, "At least 1 file must be selected.", "Eror", 2);
                return;
            }

            coppy = new ArrayList<>();

            for (int i = 0; i < selectedFiles.size(); i++)
                coppy.add(new File(selectedFiles.get(i).getAbsolutePath()));

            if (coppy.size() == 0) {
                JOptionPane.showMessageDialog(null, "At least 1 file must be selected.", "Eror", 2);
                coppy = null;
                return;
            }

            coppyPressed = true;


        }
    }

    /**
     * An inner class  Listener for Paste feature
     */
    class PasteListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (coppy == null || coppy.size() == 0)
                JOptionPane.showMessageDialog(null, "Nothing to paste", "Eror", 2);

            else {
                ArrayList<String> newNames = findNewNames();
                for (int i = 0; i < newNames.size(); i++)
                    try {

                        Model.pasteFile(coppy.get(i).getAbsolutePath(), myModel.getCurrentAddress() + "\\" + newNames.get(i));
                    } catch (Exception ex) {
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

    /**
     * A Method to change the name of the coppied file or folder if it already exists in the destination
     *
     * @return
     */

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

    /**
     * An inner class to add mouse listenet to the table
     */
    class TableListener implements MouseListener {

        public void mouseClicked(MouseEvent mouseEvent) {

            if (mouseEvent.getClickCount() == 2 && myView.getTable().getSelectedRow() != -1 && SwingUtilities.isLeftMouseButton(mouseEvent) && !mouseEvent.isConsumed()) {
                mouseEvent.isConsumed();
                int[] selectedRow = myView.getTable().getSelectedRows();


                if (selectedRow.length == 1) {
                    int r = selectedRow[0];

                    try {
                        open(myModel.getAllFiles()[r]);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Access is denied to the file", "Eror", 0);
                        myModel.goToParent();
                        upgradeView();


                    }

                }


            }

            if (mouseEvent.getClickCount() == 1 && myView.getTable().getSelectedRow() != -1 && SwingUtilities.isRightMouseButton(mouseEvent)) {


                PopMenu popMenu = null;

                if (selectedFiles != null)
                    if (selectedFiles.size() == 1)
                        popMenu = new PopMenu(true, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), myView.getTable());

                if (selectedFiles != null)
                    if (selectedFiles.size() > 1)
                        popMenu = new PopMenu(false, coppyPressed, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), myView.getTable());

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

    /**
     * An inner class to ad listeners to table header
     */
    class TableHeaderListener extends MouseAdapter {


        @Override
        public void mouseClicked(MouseEvent e) {

            if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {


                myView.addStatus();


                int col = myView.getTable().columnAtPoint(e.getPoint());

                if (headerCol != col)
                    myView.setStatus(0);

                headerCol = col;

                String name = myView.getTable().getColumnName(col);

                if (myView.getStatus() == 1)
                    sort(1, col);


                else
                    sort(0, col);
                try {
                    myView.setListDisplay();
                    initializeTable();
                } catch (Exception b) {

                }


            }


            handleFrameKeyListener();
        }
    }


    /**
     * An iner class for Open feature
     */
    class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (selectedFiles != null)
                for (int i = 0; i < selectedFiles.size(); i++)
                    open(selectedFiles.get(i));

            selectedFiles = null;


        }
    }

    /**
     * An inner class  Listener for Properties feature
     */
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

    /**
     * A Method to open file f
     *
     * @param f
     */
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
            myView.setCurrentDirectoryFiles(myModel.getAllFiles());
            myView.setCurrentAddress(myModel.getCurrentAddress());
            upgradeView();

        }

        selectedFiles = null;
    }

    /**
     * An inner class  Listener for addressTextField
     */
    class ListenToAddressTextField implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            File F = new File(myView.getAddressTextField().getText());


            if (F.exists() && F.isDirectory()) {
                myModel.setCurrentAddress(myView.getAddressTextField().getText());
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
                myView.getAddressTextField().setText(myModel.getCurrentAddress());
                JOptionPane.showMessageDialog(null, "Invalid file or address", "Eror", 0);
            }

        }
    }

    /**
     * A dynamic listener to handle change in the searchfield
     */
    void handleSearchFieldListener() {
        myView.getSearchTextField().getDocument().addDocumentListener(new DocumentListener() {
            private void upgradeLocally() {
                File[] F = null;

                if (myView.getSearchTextField().getText().length() != 0) {
                    F = new File[search.size()];
                    for (int i = 0; i < search.size(); i++)
                        F[i] = search.get(i);


                    myView.setCurrentDirectoryFiles(F);
                }


                if (myView.getSearchTextField().getText().length() == 0) {
                    myView.setCurrentDirectoryFiles(myModel.getAllFiles());
                    upgradeView();
                    return;

                }

                if (myModel.getGridDisplay()) {
                    DrawRect pleaseWork = new DrawRect(F);
                    myView.setRightSidePanel(pleaseWork);
                    myView.setGridDisplay();

                } else
                    myView.setListDisplay();


                //    upgradeView();


            }

            @Override
            public void insertUpdate(DocumentEvent e) {

                search = new ArrayList<>();
                massiveSearch(new File(myModel.getCurrentAddress()), myView.getSearchTextField().getText());
                upgradeLocally();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                search = new ArrayList<>();
                massiveSearch(new File(myModel.getCurrentAddress()), myView.getSearchTextField().getText());
                upgradeLocally();

            }

            @Override
            public void changedUpdate(DocumentEvent e) {

                search = new ArrayList<>();
                massiveSearch(new File(myModel.getCurrentAddress()), myView.getSearchTextField().getText());
                upgradeLocally();

            }
        });
    }

    /**
     * Given a directory dir, this function puts all files whom have String name in them in an arraylist called search
     *
     * @param dir
     * @param name
     */
    private void massiveSearch(File dir, String name) {
        if (name == null || name.length() == 0)
            return;

        if (dir.exists() && dir.isFile()) {
            if (dir.getName().contains(name))
                search.add(new File(dir.getAbsolutePath()));
        }

        if (dir.exists() && dir.isDirectory()) {
            File[] temp = dir.listFiles();

            if (temp != null)
                for (int i = 0; i < temp.length; i++)
                    massiveSearch(temp[i], name);

            if (temp != null)
                if (dir.getName().contains(name))
                    search.add(new File(dir.getAbsolutePath()));
        }


    }

    /**
     * Given the feature (column number, 1 to 3) and the order (0 for ascending and 1 for descending) this function sorts the table accordingly
     *
     * @param order
     * @param feature
     */
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

        myView.setCurrentDirectoryFiles(F);
    }

    /**
     * In order to handle reaction of grid displayed icons to the arrow keys, we need to calculate current row and column of the current selected item
     * in the grid display
     */
    void calcRowAndColumn() {
        upgradeSelectedFiles();

        if (selectedFiles == null || selectedFiles.size() == 0) {
            currentGridIconRow = 0;
            currentGridIconColumn = 0;
            return;
        }

        File[] temp = myModel.getAllFiles();


        int index = 0;
        File f = selectedFiles.get(0);

        for (int i = 0; i < temp.length; i++)
            if (temp[i].getAbsolutePath().equals(f.getAbsolutePath())) {
                index = i;
                break;
            }

        currentGridIconColumn = index % 5;
        currentGridIconRow = index / 5;

        return;


    }

    /**
     * Compare two files A,B according to the feature (feature = column name)
     * status=1:sort By name
     * status=2:sort By size
     * status=3:sort By Date
     *
     * @param A
     * @param B
     * @param feature
     * @return
     */
    private boolean compare(File A, File B, int feature) {


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

    /**
     * This function adds key listener to the frame. We need to add listeners to the frame. We also need to make sure that frame is currently in the focus
     */
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

                    upgradeView();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER && !controlPressed) {
                    for (int i = 0; i < selectedFiles.size(); i++)
                        if (selectedFiles.get(i).isFile())
                            open(selectedFiles.get(i));


                    if (selectedFiles.size() == 1 && selectedFiles.get(0).isDirectory())
                        open(selectedFiles.get(0));
                }

                if (e.getKeyCode() == KeyEvent.VK_UP && !controlPressed) {
                    if (myModel.getAllFiles() == null)
                        return;


                    if (myModel.getGridDisplay() == false) {
                        int row = myView.getTable().getSelectedRow();


                        if (row != 0)
                            myView.getTable().setRowSelectionInterval(row - 1, row - 1);

                        else
                            myView.getTable().setRowSelectionInterval(myModel.getAllFiles().length - 1, myModel.getAllFiles().length - 1);


                        myView.getTable().scrollRectToVisible(myView.getTable().getCellRect(myView.getTable().getSelectedRow(), 0, true));
                    } else {

                        calcRowAndColumn();

                        if (myModel.getAllFiles() != null) {
                            int l = myModel.getAllFiles().length;
                            if (currentGridIconRow == 0)
                                currentGridIconRow = 0;


                            else
                                currentGridIconRow--;

                            File next = myModel.getAllFiles()[currentGridIconRow * 5 + currentGridIconColumn];

                            for (int i = 0; i < gridIcons.size(); i++)
                                if (!gridIcons.get(i).getPath().equals(next.getAbsolutePath())) {
                                    gridIcons.get(i).setSetSelected(false);
                                    gridIcons.get(i).repaint();
                                } else {
                                    gridIcons.get(i).setSetSelected(true);

                                    gridIcons.get(i).repaint();

                                }

                            upgradeSelectedFiles();


                        }


                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_RIGHT && !controlPressed) {
                    if (myModel.getAllFiles() == null)
                        return;

                    if (myModel.getGridDisplay() && myModel.getAllFiles() != null) {
                        calcRowAndColumn();
                        int l = myModel.getAllFiles().length;
                        if (currentGridIconColumn == 4 || (currentGridIconRow == l / 5 && l % 5 - 1 == currentGridIconColumn))
                            currentGridIconColumn = 0;


                        else
                            currentGridIconColumn++;


                        File next = myModel.getAllFiles()[currentGridIconRow * 5 + currentGridIconColumn];

                        for (int i = 0; i < gridIcons.size(); i++)
                            if (!gridIcons.get(i).getPath().equals(next.getAbsolutePath())) {
                                gridIcons.get(i).setSetSelected(false);
                                gridIcons.get(i).repaint();
                            } else {
                                gridIcons.get(i).setSetSelected(true);

                                gridIcons.get(i).repaint();

                            }

                        upgradeSelectedFiles();

                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT && !controlPressed) {
                    if (myModel.getAllFiles() == null)
                        return;

                    if (myModel.getGridDisplay() && myModel.getAllFiles() != null) {
                        calcRowAndColumn();
                        int l = myModel.getAllFiles().length;
                        if (currentGridIconColumn == 0) {
                            if (currentGridIconRow == l / 5)
                                currentGridIconColumn = l % 5 - 1;

                            else
                                currentGridIconColumn = 4;
                        } else
                            currentGridIconColumn--;


                        File next = myModel.getAllFiles()[currentGridIconRow * 5 + currentGridIconColumn];

                        for (int i = 0; i < gridIcons.size(); i++)
                            if (!gridIcons.get(i).getPath().equals(next.getAbsolutePath())) {
                                gridIcons.get(i).setSetSelected(false);
                                gridIcons.get(i).repaint();
                            } else {
                                gridIcons.get(i).setSetSelected(true);

                                gridIcons.get(i).repaint();

                            }

                        upgradeSelectedFiles();

                    }
                }


                if (e.getKeyCode() == KeyEvent.VK_DOWN && !controlPressed) {
                    if (myModel.getAllFiles() == null)
                        return;

                    if (myModel.getGridDisplay() == false) {


                        int[] selectedRows = myView.getTable().getSelectedRows();
                        int row = selectedRows[selectedRows.length - 1];


                        if (row != myModel.getAllFiles().length - 1)
                            myView.getTable().setRowSelectionInterval(row + 1, row + 1);


                        else
                            myView.getTable().setRowSelectionInterval(0, 0);

                        myView.getTable().scrollRectToVisible(myView.getTable().getCellRect(myView.getTable().getSelectedRow(), 0, true));
                    } else if (myModel.getAllFiles() != null) {
                        calcRowAndColumn();
                        int l = myModel.getAllFiles().length;
                        if (currentGridIconRow == l / 5)
                            return;

                        if (currentGridIconRow == l / 5 - 1) {
                            currentGridIconRow = l / 5;

                            int lastCol = l % 5;
                            if (currentGridIconColumn >= lastCol)
                                currentGridIconColumn = lastCol - 1;
                        } else if (currentGridIconRow != l / 5)
                            currentGridIconRow++;


                        File next = myModel.getAllFiles()[currentGridIconRow * 5 + currentGridIconColumn];

                        for (int i = 0; i < gridIcons.size(); i++)
                            if (!gridIcons.get(i).getPath().equals(next.getAbsolutePath())) {
                                gridIcons.get(i).setSetSelected(false);
                                gridIcons.get(i).repaint();
                            } else {
                                gridIcons.get(i).setSetSelected(true);

                                gridIcons.get(i).repaint();

                            }

                        upgradeSelectedFiles();
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !controlPressed) {
                    myModel.goToParent();
                    upgradeView();

                }

                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    controlPressed = true;


                }

                if (e.getKeyCode() == KeyEvent.VK_C && controlPressed) {
                    coppy = new ArrayList<>();

                    if (selectedFiles != null)
                        for (int i = 0; i < selectedFiles.size(); i++)
                            coppy.add(new File(selectedFiles.get(i).getAbsolutePath()));

                    coppyPressed = true;

                }

                if (e.getKeyCode() == KeyEvent.VK_V && controlPressed) {


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
                    if (selectedFiles == null || selectedFiles.size() > 1 || selectedFiles.size() == 0) {
                        JOptionPane.showMessageDialog(null, "Exactly file must be selected", "Eror", 0);
                        return;
                    }


                    String newName = JOptionPane.showInputDialog(null, "Enter new name:", "Enter name", 2);
                    myModel.renameFile(selectedFiles.get(0).getPath(), newName);
                    upgradeView();


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

    /**
     * An inner class to represent GridIcons, the one that are displayed when frame is set to be in the grid mode.
     * Since we need to add Listenrs to this which is able to change model in case of certain action events, A descision was made to also include this class in the
     * Controller Class as an inner class
     */
    class GridIcon extends JButton {
        private Color pressedBackgroundColor = new Color(0, 0, 255, 100);
        private String shortenedName;
        private String path;
        private boolean setSelected = false;
        private long time;
        private boolean previouslySelected = false;

        /**
         * Constructor for this class
         *
         * @param text
         * @param icon
         * @param path
         */
        public GridIcon(String text, Icon icon, String path) {

            this.path = path;

            if (text.length() > 9) {
                shortenedName = text.substring(0, 9);
                shortenedName += "...";
            } else
                shortenedName = text;

            this.setIcon(icon);
            this.setText(shortenedName);
            this.setFocusable(false);

            generateTooltip();


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

        /**
         * A Method to add tooltip to this GridIcon
         */
        private void generateTooltip() {
            File f = new File(path);


            if (f.isFile()) {
                this.setToolTipText("<html>" + "Date created:\t" + new Date(f.lastModified()) + "<br>" + "Size(Kb):\t" + f.length() / 1024 + "</html>");
            } else {
                String first3Files = "Files contained:\t", first3Folders = "Folders contained:";
                File[] F = f.listFiles();
                int fileCounter = 0, folderCounter = 0;

                if (F != null)
                    for (int i = 0; i < F.length; i++) {
                        if (F[i].isFile() & fileCounter < 3) {
                            first3Files += F[i].getName();
                            fileCounter++;

                            if (fileCounter != 2)
                                first3Files += ",";
                        }

                        if (F[i].isDirectory() && folderCounter < 3) {
                            first3Folders += F[i].getName();
                            folderCounter++;

                            if (folderCounter != 2)
                                first3Folders += ",";
                        }

                    }

                this.setToolTipText("<html>" + "Date created:\t" + new Date(f.lastModified()) + "<br>" + "Size(kb):\t" + f.length() / 1024 + "<br>" + first3Files + "<br>" + first3Folders + "</html>");
            }
        }

        /**
         * Getter for previouslySelected
         *
         * @return previouslySelected
         */
        public boolean isPreviouslySelected() {
            return previouslySelected;
        }


        /**
         * Setter for previouslySelected
         *
         * @param b
         */
        public void setPreviouslySelected(boolean b) {
            previouslySelected = b;
        }


        /**
         * Add listeners to this GridIcon
         */
        void addListener() {
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {

                        if (selectedFiles == null || selectedFiles.size() == 1) {
                            PopMenu popMenu = new PopMenu(true, false, e.getXOnScreen(), e.getYOnScreen(), drawRect);
                            popMenu.getRename().addActionListener(new RenameListener());
                            popMenu.getOpen().addActionListener(new OpenListener());
                            popMenu.getDelete().addActionListener(new DeleteListener());
                            popMenu.getCut().addActionListener(new CutListener());
                            popMenu.getCopy().addActionListener(new CopyListener());
                            popMenu.getProperties().addActionListener(new PropertiesListener());

                        } else {
                            PopMenu popMenu = new PopMenu(false, false, e.getXOnScreen(), e.getYOnScreen(), drawRect);
                            popMenu.getDelete().addActionListener(new DeleteListener());
                            popMenu.getCut().addActionListener(new CutListener());
                            popMenu.getCopy().addActionListener(new CopyListener());
                            popMenu.getProperties().addActionListener(new PropertiesListener());
                        }


                    }

                    if (SwingUtilities.isLeftMouseButton(e)) {

                        if (setSelected) {
                            setSelected = false;

                            long temp = new Date().getTime();

                            if (temp - time < 500) {

                                //  JOptionPane.showMessageDialog(null, "Double click");
                                try {
                                    open(new File(path));
                                } catch (Exception ex) {
                                    JOptionPane.showMessageDialog(null, "Access is denied to the file", "Eror", 0);
                                    myModel.goToParent();
                                    upgradeView();
                                }


                            }


                        } else {


                            if (!controlPressed) {
                                for (int i = 0; i < gridIcons.size(); i++) {
                                    gridIcons.get(i).setSetSelected(false);
                                    gridIcons.get(i).repaint();
                                }
                            }


                            setSelected = true;
                            upgradeSelectedFiles();
                            repaint();


                            time = new Date().getTime();


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
        }

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            if (getModel().isPressed() || setSelected) {
                g.setColor(pressedBackgroundColor);
            } else {
                Color color = new Color(0, 0, 0, 0);

                g.setColor(color);
            }

            g.fillRect(0, 0, getWidth(), getHeight());

        }

        /**
         * We don't want content area to be filled, so we override it and change it to do nothing
         *
         * @param b
         */
        @Override
        public void setContentAreaFilled(boolean b) {
        }

        /**
         * Setter for path
         *
         * @return
         */
        public String getPath() {
            return path;
        }

        /**
         * Setter for isSelected
         *
         * @param isSelected
         */
        public void setSetSelected(boolean isSelected) {
            setSelected = isSelected;


            upgradeSelectedFiles();
        }

        /**
         * Getter for isSelected
         *
         * @return isSelected
         */
        public boolean isSetSelected() {
            return setSelected;
        }

        /**
         * Calculate and return Xmid, The X that must be contained within the selection rectange so this GridIcon is selected
         *
         * @return Xmid
         */
        double getXMid() {
            return this.getLocationOnScreen().getX() + getHeight() / 2;
        }

        /**
         * Calculate and return Ymid, The Y that must be contained within the selection rectange so this GridIcon is selected
         *
         * @return Ymid
         */
        double getYMid() {
            return this.getLocationOnScreen().getY() + getWidth() / 2;
        }
    }

    /**
     * A Class That represents DrawRect, A Jpanel which has the ability to draw a selection rectangle, and hence calculate the GridIcons selected
     * Since Actions performed in instances of this class directly change contents on the Model class or the Controller class, A Decision was
     * Made to add it as an inner Class to the Controller class
     */
    class DrawRect extends JPanel {

        private int x, y, x2, y2, xOnScreen1, yOnScreen1, xOnScreen2, yOnScreen2;
        private boolean leftClicked = false;

        private File[] files;

        /**
         * Setter for files
         *
         * @param files
         */
        public void setFiles(File[] files) {
            this.files = files;
            addButtons();
            handleMouseListeners();
        }

        /**
         * Constructor for this class
         *
         * @param F
         */
        public DrawRect(File[] F) {
            x = y = x2 = y2 = 0;

            files = F;

            gridIcons = new ArrayList<>();
            addButtons();
            handleMouseListeners();
            this.setVisible(true);


        }

        /**
         * A Function to represent Icons in current directory in Grid mode
         */
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

            if (temp % 5 != 0)
                while (temp % 5 > 0) {
                    temp++;
                    dummyPanel.add(new JPanel());
                }
            this.add(dummyPanel);

            this.revalidate();
        }

        /**
         * A Method to set started point where mouse clicked
         *
         * @param x
         * @param y
         */
        public void setStartPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * A Method to set end point where mouse released
         *
         * @param x
         * @param y
         */
        public void setEndPoint(int x, int y) {
            x2 = (x);
            y2 = (y);
        }

        /**
         * Given two points, This function draws selection rectangle accordingly
         *
         * @param g
         * @param x
         * @param y
         * @param x2
         * @param y2
         */
        public void drawPerfectRect(Graphics g, int x, int y, int x2, int y2) {
            int px = Math.min(x, x2);
            int py = Math.min(y, y2);
            int pw = Math.abs(x - x2);
            int ph = Math.abs(y - y2);
            g.drawRect(px, py, pw, ph);
            g.fillRect(px, py, pw, ph);
        }

        /**
         * A Method to add MouseListenres to this panel
         */
        public void handleMouseListeners() {
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!controlPressed) {
                        for (int i = 0; i < gridIcons.size(); i++)
                            gridIcons.get(i).setSetSelected(false);

                        upgradeSelectedFiles();
                        selectedFiles = null;
                        myView.getNumberOfSelectedLabel().setText("number of items selected :");
                        repaint();
                    }

                    if (SwingUtilities.isRightMouseButton((e))) {

                        GridEmptySpacePopMenu gridEmptySpacePopMenu = null;

                        if (coppy == null || coppy.size() == 0)
                            gridEmptySpacePopMenu = new GridEmptySpacePopMenu(false, e.getXOnScreen(), e.getYOnScreen(), drawRect);

                        else
                            gridEmptySpacePopMenu = new GridEmptySpacePopMenu(true, e.getXOnScreen(), e.getYOnScreen(), drawRect);

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
                            PropertiesListener propertiesListener = new PropertiesListener();

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                selectedFiles = new ArrayList<>();
                                selectedFiles.add(new File(myModel.getCurrentAddress()));
                                propertiesListener.actionPerformed(e);
                                selectedFiles = null;
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
                    }

                    if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {


                        leftClicked = false;

                    }


                }


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


                        repaint();
                        leftClicked = false;


                    }

                }
            });


            this.addMouseMotionListener(new MouseAdapter() {

                @Override
                public void mouseDragged(MouseEvent e) {

                    if (leftClicked) {
                        setEndPoint(e.getX(), e.getY());
                        xOnScreen2 = e.getXOnScreen();
                        yOnScreen2 = e.getYOnScreen();


                        repaint();


                        checkButtonsContained(Math.min(xOnScreen1, xOnScreen2), Math.min(yOnScreen1, yOnScreen2), Math.max(xOnScreen1, xOnScreen2), Math.max(yOnScreen1, yOnScreen2));


                        if (selectedFiles == null)
                            myView.getNumberOfSelectedLabel().setText("number of items selected: ");

                        else
                            myView.getNumberOfSelectedLabel().setText("number of items selected: " + selectedFiles.size());
                        repaint();


                    }

                }

            });

        }

        /**
         * We have overriden the paint method so that we can put our selection rectangle above GridICons presented
         *
         * @param g
         */
        @Override
        public void paint(Graphics g) {
            try {
                super.paintChildren(g);
                super.paintComponents(g);
                super.paint(g);
                int alpha = 50; // 50% transparent
                Color myColour = new Color(0, 0, 200, alpha);
                g.setColor(myColour);
                drawPerfectRect(g, x, y, x2, y2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * Given a selection rectangle, this function determines which Grid icons are contained within that selection rectangle and hence they are
         * Set selected and added to the selected files arraylist
         *
         * @param xmin
         * @param ymin
         * @param xmax
         * @param ymax
         */
        void checkButtonsContained(int xmin, int ymin, int xmax, int ymax) {
            if (gridIcons != null) {

                if (!controlPressed)
                    selectedFiles = new ArrayList<>();

                else
                    for (int i = 0; i < gridIcons.size(); i++)
                        if (gridIcons.get(i).isSetSelected())
                            gridIcons.get(i).setPreviouslySelected(true);


                {
                    ArrayList<GridIcon> previouslySelecte = new ArrayList<>();
                    for (int i = 0; i < gridIcons.size(); i++)
                        previouslySelecte.add(gridIcons.get(i));
                }
                for (int i = 0; i < gridIcons.size(); i++) {
                    if ((gridIcons.get(i).getXMid() >= xmin && gridIcons.get(i).getXMid() <= xmax) && (gridIcons.get(i).getYMid() >= ymin && gridIcons.get(i).getYMid() <= ymax)) {
                        gridIcons.get(i).setSetSelected(true);
                        selectedFiles.add(new File(gridIcons.get(i).getPath()));

                    } else {

                        if (!controlPressed || (coppyPressed && gridIcons.get(i).isPreviouslySelected() == false))
                            gridIcons.get(i).setSetSelected(false);

                    }


                }

            }

            for (int i = 0; i < gridIcons.size(); i++)
                gridIcons.get(i).setPreviouslySelected(false);
        }
    }
}
