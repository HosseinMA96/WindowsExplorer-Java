package View;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Spliterator;

import static java.lang.System.exit;


public class View extends JFrame implements FocusListener {

    @Override
    public void focusLost(FocusEvent fe) {
        this.setAutoRequestFocus(true);
        this.requestFocus();
    }

    @Override
    public void focusGained(FocusEvent fe) {
    }





    private boolean hasPrevView = false;
    private FileTableModel model;
    private JTable table = new JTable();
    private int status = 0;


    private  JFrame frame = new JFrame("APP.JFileManager");
    private String currentAddress;

    //Adress and Search Textfiled at top
    private JTextField addressTextField = new JTextField("Address", 20);
    private JTextField searchTextField = new JTextField("Search", 20);

    //Three arrows
    private JButton uppArrow = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png"));
    private JButton leftArrow = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\leftArrow.png"));
    private JButton rightArrow = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\rightArrow.png"));

    //Two View.View buttons (Grid and table) located on lower right part of the frame
    private JButton gridDisplay = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\gridDisplay.png"));
    private JButton tableDisplay = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\tableDisplay.png"));

    //Upper menu bar
    private JMenuBar upperMenuBar = new JMenuBar();

    //Upper Menubar Items
    private JMenu file = new JMenu("File");
    private JMenu edit = new JMenu("Edit");
    private JMenu help = new JMenu("Help");

    //File Menu items
    private JMenuItem file_NewFile = new JMenuItem("New File");
    private JMenuItem file_NewFolder = new JMenuItem("New Folder");
    private JMenuItem file_Delete = new JMenuItem("Delete");
    private JMenuItem file_SetCurrentForSync = new JMenuItem("Set as Sync Path");

    //Edit Menu Items
    private JMenuItem edit_Rename = new JMenuItem("Rename ");
    private JMenuItem edit_Copy = new JMenuItem("Copy");
    private JMenuItem edit_Cut = new JMenuItem("Cut");
    private JMenuItem edit_Paste = new JMenuItem("Paste");
    private JMenuItem edit_Synchronize = new JMenuItem("Sync");

    //View.View.Help Menu Items
    private JMenuItem help_AboutMe = new JMenuItem("About me ");
    private JMenuItem help_Settings = new JMenuItem("Settings");
    private JMenuItem help_Help = new JMenuItem("Help");

    //Number of Selected labels
    private JLabel numberOfSelectedLabel = new JLabel("Number of items selected:");


    //Split Pane
    private JSplitPane splitPane = null;

    //Jtree for left side hierarchy
    private JTree leftTree = null;

    //Right Side panel for folder and file display
    private JPanel rightSidePanel = new JPanel();

    //Left scroll pane and right scroll pane for adding leftTree and rightSidePanel
    private JScrollPane leftScrollPane = null;
    private JScrollPane rightScrollPane = null;

    File[] currentDirectoryFiles;
    //   String currentAddress;

    //Starting pass
    private Robot val;

    //current file Holder

    JLayeredPane layeredPane;

    //current GridIcons
   // private ArrayList<GridIcon> gridIconArrayList;
   // private DrawRect drawRect;


    public View() {

        this.setAlwaysOnTop(true);
        this.setAutoRequestFocus(true);

        addMenuBars();


        //Set frame's layout
        frame.setLayout(new BorderLayout());

        //set ICon
        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\mainIcon.png").getImage());

        //Two panels for upper Panel
          JPanel upperPanel = new JPanel(new BorderLayout());
        JPanel upperPanel_FlowPanel = new JPanel(new FlowLayout());

        //Panel for lower panel
        JPanel lowerPanel = new JPanel(new BorderLayout());
        JPanel lowerPanel_FlowPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        //Add file Menu Items
//        file.add(file_NewFile);
//        file.add(file_SetCurrentForSync);
//        file.add(file_Delete);
//        file.add(file_NewFolder);
//
//
//        //Add Edit Menu Items
//        edit.add(edit_Rename);
//        edit.add(edit_Copy);
//        edit.add(edit_Cut);
//        edit.add(edit_Paste);
//        edit.add(edit_Synchronize);
//
//        //Add View.View.Help Menu Items
//        help.add(help_Settings);
//        help.add(help_AboutMe);
//        help.add(help_Help);
//
//        //Add Menus To Menubar
//        upperMenuBar.add(file);
//        upperMenuBar.add(edit);
//        upperMenuBar.add(help);

        //Adding Arows to upperPanel's Left panel
        upperPanel_FlowPanel.add(leftArrow);
        upperPanel_FlowPanel.add(rightArrow);
        upperPanel_FlowPanel.add(uppArrow);

        //Adding upperPanel's Flow panel as Upper panel's west Panel
        upperPanel.add(upperPanel_FlowPanel, BorderLayout.WEST);

        //Adding Address textField as upper panel's center panel
        upperPanel.add(addressTextField, BorderLayout.CENTER);

        //Adding Address textField as upper panel's east panel
        upperPanel.add(searchTextField, BorderLayout.EAST);

        //Setting arrow button's sizes
        leftArrow.setPreferredSize(new Dimension(17, 17));
        rightArrow.setPreferredSize(new Dimension(17, 17));
        uppArrow.setPreferredSize(new Dimension(17, 17));

        //Add Upper panel to frame's north section
        frame.add(upperPanel, BorderLayout.NORTH);

        //Adding menu bar to frame
        frame.setJMenuBar(upperMenuBar);

        //Setting size of gridDisplay and tableDisplay buttons
        gridDisplay.setPreferredSize(new Dimension(17, 17));
        tableDisplay.setPreferredSize(new Dimension(17, 17));


        //Add grid Display and table Display to lowerPanel's flowPanel
        lowerPanel_FlowPanel.add(gridDisplay);
        lowerPanel_FlowPanel.add(tableDisplay);

        //Set lowerPanel's Flowpanel as lowerPanel's East
        lowerPanel.add(lowerPanel_FlowPanel, BorderLayout.EAST);

        //Set selectedLable as lowerPanel's East
        lowerPanel.add(numberOfSelectedLabel, BorderLayout.WEST);

        //Set LowerPanel as frame's lowerPanel
        frame.add(lowerPanel, BorderLayout.SOUTH);





        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // frame.setSize(screenSize.width, screenSize.height);

        frame.setLocation(0, 0);
        frame.setSize(1500, 700);
        frame.setVisible(true);
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);


        /////Add buttons

    }

    public void addMenuBars()
    {
        file.add(file_NewFile);
        file.add(file_SetCurrentForSync);
        file.add(file_Delete);
        file.add(file_NewFolder);


        //Add Edit Menu Items
        edit.add(edit_Rename);
        edit.add(edit_Copy);
        edit.add(edit_Cut);
        edit.add(edit_Paste);
        edit.add(edit_Synchronize);

        //Add View.View.Help Menu Items
        help.add(help_Settings);
        help.add(help_AboutMe);
        help.add(help_Help);

        //Add Menus To Menubar
        upperMenuBar.add(file);
        upperMenuBar.add(edit);
        upperMenuBar.add(help);
    }

    public void setGridDisplay() {

        if (hasPrevView)
            frame.remove(splitPane);

        makeLeftTree();

      //   drawRect = new DrawRect(currentDirectoryFiles);
       // drawRect.repaint();
        JPanel dummyPannel=new JPanel(new BorderLayout());
     //   dummyPannel.add(drawRect,BorderLayout.CENTER);

        rightScrollPane = new JScrollPane(dummyPannel);

        leftScrollPane = new JScrollPane(leftTree);

        rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        splitPane = new JSplitPane(SwingConstants.VERTICAL, leftScrollPane, rightScrollPane);

        frame.add(splitPane);
 //       drawRect.repaint();
        frame.setVisible(true);

        hasPrevView = true;
    }



    public JTable getTable() {
        return table;
    }

    public void setListDisplay() {

        makeLeftTree();

        model = new FileTableModel(currentDirectoryFiles);
        table = new JTable(model);


        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.getTableHeader().setReorderingAllowed(false);

        rightSidePanel = new JPanel(new BorderLayout());

        rightScrollPane = new JScrollPane(table);
        rightSidePanel.add(rightScrollPane, BorderLayout.CENTER);


        leftScrollPane = new JScrollPane(leftTree);

        if (hasPrevView)
            frame.remove(splitPane);

        splitPane = new JSplitPane(SwingConstants.VERTICAL, leftScrollPane, rightScrollPane);


        frame.add(splitPane);

        rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.setVisible(true);

        hasPrevView = true;
    }

    public JTextField getAddressTextField() {
        return addressTextField;
    }

    public JMenuItem getHelp_AboutMe() {
        return help_AboutMe;
    }

    public JMenuItem getHelp_Settings() {
        return help_Settings;
    }

    public JMenuItem getHelp_Help() {
        return help_Help;
    }

//    public DrawRect getDrawrect()
//    {
//        return this.drawRect;
//    }

    public void makeLeftTree() {


        String temp = currentAddress;
        boolean exit = false;
        File f = new File(temp);


        DefaultMutableTreeNode thisDir = new DefaultMutableTreeNode(f.getName(), true);



        ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();




        for (int i = 0; i < currentDirectoryFiles.length; i++) {
            if (currentDirectoryFiles[i].isFile())

                thisDir.add(new DefaultMutableTreeNode(currentDirectoryFiles[i].getName(), false));

            else
                thisDir.add(new DefaultMutableTreeNode(currentDirectoryFiles[i].getName(), true));


        }


        nodes.add(thisDir);

        leftTree = new JTree(thisDir, true);
        leftScrollPane = new JScrollPane(leftTree);


        while (!exit) {

            try {
                f = new File(temp);
                f = f.getParentFile();
                temp = f.getAbsolutePath();

                DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(f.getName() + "", true);
                tempNode.add(nodes.get(nodes.size() - 1));

                nodes.add(tempNode);


            } catch (Exception e) {
                exit = true;
                break;
            }
        }

        leftTree = new JTree(nodes.get(nodes.size() - 1), true);


        for (int i = 0; i < leftTree.getRowCount(); i++) {
            leftTree.expandRow(i);
        }




        leftScrollPane = new JScrollPane(leftTree);
        leftTree.setPreferredSize(new Dimension(200, 800));

    }

    public JTextField getSearchTextField() {
        return searchTextField;
    }

    public JLabel getNumberOfSelectedLabel() {
        return numberOfSelectedLabel;
    }

    public void setNumberOfSelectedLabel(JLabel numberOfSelectedLabel) {
        this.numberOfSelectedLabel = numberOfSelectedLabel;
    }

    public JButton getGridDisplay() {
        return gridDisplay;
    }

    public boolean isHasPrevView() {
        return hasPrevView;
    }

    public JMenuItem getEdit_Rename() {
        return edit_Rename;
    }

    public boolean hasPreview()
    {
        return this.hasPrevView;
    }


    public JMenuItem getEdit_Copy() {
        return edit_Copy;
    }


    public JMenuItem getEdit_Cut() {
        return edit_Cut;
    }

    public JMenuItem getEdit_Paste() {
        return edit_Paste;
    }

    public JMenuItem getEdit_Synchronize() {
        return edit_Synchronize;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public JButton getTableDisplay() {
        return tableDisplay;
    }

  //  public ArrayList<GridIcon> getGridIconArrayList() {
     //   return gridIconArrayList;
 //   }



    public JMenuItem getFile_NewFolder() {
        return file_NewFolder;
    }

    public JMenuItem getFile_NewFile() {
        return file_NewFile;
    }

    public JMenuItem getFile_Delete() {
        return file_Delete;
    }

    public JMenuItem getFile_SetCurrentForSync() {
        return file_SetCurrentForSync;
    }

    public void addStatus() {
        status++;
        status %= 2;
    }

    public int getStatus() {
        return status;
    }


  //  public DrawRect getDrawRect() {
   //     return drawRect;
   // }

    public JFrame getFrame() {
        return frame;
    }

    public void setAddressTextField(String text) {
        this.addressTextField.setText(text);
    }

    public JButton getUppArrow() {
        return uppArrow;
    }

    public JButton getLeftArrow() {
        return leftArrow;
    }

    public JButton getRightArrow() {
        return rightArrow;
    }

    public JPanel getRightSidePanel() {
        return rightSidePanel;
    }

    public void setCurrentDirectoryFiles(File[] currentDirectoryFiles) {
        this.currentDirectoryFiles = currentDirectoryFiles;
        //System.out.println(this.currentDirectoryFiles.length);
    }

   public void setSplitPane(JSplitPane splitPane)
    {
        this.splitPane=splitPane;
    }

    public JTree getLeftTree() {
        return leftTree;
    }

    public JScrollPane getLeftScrollPane() {
        return leftScrollPane;
    }

    public void setLeftScrollPane(JScrollPane leftScrollPane) {
        this.leftScrollPane = leftScrollPane;
    }


    public void setRightScrollPane(JScrollPane rightScrollPane) {
        this.rightScrollPane = rightScrollPane;
    }

    public JMenuBar getUpperMenuBar() {
        return upperMenuBar;
    }



    public JScrollPane getRightScrollPane() {
        return rightScrollPane;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public void setLookAndFeel(String LF) {

        switch (LF) {

            case "Motif":
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, LF + " Look and feel is not available");
                }
                break;

            case "System":
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, LF + " Look and feel is not available");
                }
                break;

            case "WindowsClassic":
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, LF + " Look and feel is not available");
                }
                break;

            case "Nimbus":
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, LF + " Look and feel is not available");
                }
                break;


        }


    }



}




