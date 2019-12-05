package View;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;


public class View extends JFrame {

    private boolean hasPrevView = false;
   private boolean localUpdate=false;
   private  FileTableModel model;
   private JTable table=new JTable();


    JFrame frame = new JFrame("APP.JFileManager");
    String currentAddress;

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


    //current file Holder

    JLayeredPane layeredPane;

    //current GridIcons
    private ArrayList<GridIcon> gridIconArrayList;

    private DrawRect drawRect;

    public View() {
        //Handle the tray
        SystemTray tray = SystemTray.getSystemTray();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\settingIcon.png");
        PopupMenu menu = new PopupMenu();
        MenuItem messageItem = new MenuItem("Show Message");
        menu.add(messageItem);

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


        //Temporal nodes for Jtree
//        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
//        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
//        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
//        root.add(vegetableNode);
//        root.add(fruitNode);

        //initialize Jtree for the temporal root
        //   leftTree = new JTree(root, true);
        // makeLeftTree();

        //     setGridDisplay();
        //   setListDisplay();

        //Initialize scroll panes so that later we can scroll
        //  leftScrollPane = new JScrollPane(leftTree);
        // rightScrollPane = new JScrollPane(rightSidePanel);

        //  rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //   rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        //Make a split pane and put it in the frame's Center
        //   splitPane = new JSplitPane(SwingConstants.VERTICAL, leftScrollPane, rightScrollPane);
        //   frame.add(splitPane, BorderLayout.CENTER);


        // rightScrollPane.setVerticalScrollBar();


        //  splitPane.setOrientation(SwingConstants.VERTICAL);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // frame.setSize(screenSize.width, screenSize.height);

        frame.setLocation(0, 0);
        frame.setSize(1500, 700);
        frame.setVisible(true);


        /////Add buttons

    }

    public void setGridDisplay() {
        // JOptionPane.showMessageDialog(null,"Grild Display");

        //    drawRect = new DrawRect();

        makeLeftTree();

        int numberOfIcons = currentDirectoryFiles.length;
        rightSidePanel = new DrawRect();
        JPanel buttonsPanel = new JPanel(new GridLayout(numberOfIcons / 5 + 1, 1));

        layeredPane = new JLayeredPane();


        //  rightSidePanel.setBackground(new Color(0, 0, 0, 10));
        //rightSidePanel.setOpaque(false);
//add to the code


        gridIconArrayList = new ArrayList<>();

        int temp = 0;

        ArrayList<JPanel> panels = new ArrayList<>();
        JPanel dummyPanel = new JPanel(new GridLayout(1, 5));

        for (File f : currentDirectoryFiles) {
            temp++;

            if (f.isFile()) {

                GridFileIcon fileIcon = new GridFileIcon(f.getName(), FileSystemView.getFileSystemView().getSystemIcon(f), f.getAbsolutePath());
                gridIconArrayList.add(fileIcon);
                dummyPanel.add(fileIcon);
            } else {
                gridIconArrayList.add(new GridFolderIcon(f.getName(), FileSystemView.getFileSystemView().getSystemIcon(f), f.getAbsolutePath()));
                dummyPanel.add(gridIconArrayList.get(gridIconArrayList.size() - 1));
            }

            if (temp % 5 == 0) {
                buttonsPanel.add(dummyPanel);
                temp = 0;
                panels.add(dummyPanel);
                dummyPanel = new JPanel(new GridLayout(1, 5));
            }

        }

        if (hasPrevView)
            frame.remove(splitPane);


        dummyPanel = new JPanel();
        layeredPane.setLayout(new BorderLayout());

        leftScrollPane = new JScrollPane(leftTree);
        rightScrollPane = new JScrollPane(buttonsPanel);

        splitPane = new JSplitPane(SwingConstants.VERTICAL, leftScrollPane, rightScrollPane);

        frame.add(splitPane);
        frame.setVisible(true);

        buttonsPanel.setBounds(rightScrollPane.getBounds());
        rightSidePanel.setBounds(rightScrollPane.getBounds());


        layeredPane.add(buttonsPanel, BorderLayout.CENTER, 1);
        layeredPane.add(rightSidePanel, BorderLayout.CENTER, 0);


        rightScrollPane = new JScrollPane(layeredPane);

        frame.remove(splitPane);
        splitPane = new JSplitPane(SwingConstants.VERTICAL, leftScrollPane, rightScrollPane);
        frame.add(splitPane);


        //     rightScrollPane = new JScrollPane(rightSidePanel);


        //  rightScrollPane.add(drawRect);


        rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //   frame.setLocation(400, 400);
        //     frame.setSize(1000, 700);
        frame.setVisible(true);
//        rightScrollPane.setVertiscalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //  rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        hasPrevView = true;
    }

    public JTable getTable() {
        return table;
    }

    public void setListDisplay(String s) {
        currentAddress=s;

        makeLeftTree();
        //    frame.remove(rightScrollPane);
      model = new FileTableModel(new File(currentAddress));
        File f = new File(currentAddress);
        currentDirectoryFiles = f.listFiles();
         table = new JTable(model);
        //table.setCellSelectionEnabled(true);

        table.setRowHeight(25);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.getTableHeader().setReorderingAllowed(false);

//        table.getTableHeader().addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                int col = table.columnAtPoint(e.getPoint());
//                String name = table.getColumnName(col);
//                JOptionPane.showMessageDialog(null,"Column index selected " + col + " " + name);
//            }
//        });

        ListSelectionModel cellSelectionModel = table.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);//change Single selection to see effects


        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                gridIconArrayList = new ArrayList<GridIcon>();
                //           String selectedDate=null;

                int[] selectedRow = table.getSelectedRows();
                int[] selectedColumns = table.getSelectedColumns();//here has good table.get seelction staff
                numberOfSelectedLabel.setText("Number of items selected: " + selectedRow.length);

                for (int i = 0; i < selectedRow.length; i++) {
                    GridIcon gridIcon = new GridFileIcon("abcd", null, currentDirectoryFiles[selectedRow[i]].getAbsolutePath());
                    gridIcon.setSetSelected(true);
                    gridIconArrayList.add(gridIcon);
                    //      JOptionPane.showMessageDialog(null,currentDirectoryFiles[selectedRow[i]].getAbsolutePath()+"");
                }

            }
        });
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

    public void makeLeftTree() {
//        //Temporal nodes for Jtree
//        DefaultMutableTreeNode rootOfroot=new DefaultMutableTreeNode("Root of Root");
//        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
//        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
//       root.add(vegetableNode);
//       root.add(fruitNode);
//
//       rootOfroot.add(root);
//
//        //initialize Jtree for the temporal root
//        this.leftTree = new JTree(rootOfroot, true);
//        leftScrollPane = new JScrollPane();
//        leftScrollPane.add(leftTree);

        String temp = currentAddress;
        boolean exit = false;
        File f = new File(temp);
        //     System.out.println(temp+" , "+f.getName());

        DefaultMutableTreeNode thisDir = new DefaultMutableTreeNode(f.getName(), true);


//        File f = new File(currentAddress);
//        f = f.getParentFile();
//        try {
//            currentAddress = f.getAbsolutePath();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "No parent directory exists", "Error", 2);
//        }

        ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<>();

        //   System.out.println(currentDirectoryFiles.length);




        for (int i = 0; i < currentDirectoryFiles.length; i++) {
            if (currentDirectoryFiles[i].isFile())

                thisDir.add(new DefaultMutableTreeNode(currentDirectoryFiles[i].getName(), false));

            else
                thisDir.add(new DefaultMutableTreeNode(currentDirectoryFiles[i].getName(), true));


        }


        //    System.out.println(thisDir.getChildCount());
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

     //   Icon icon= FileSystemView.getFileSystemView().getSystemIcon(currentDirectoryFiles);


        //    TreePath treePath=new TreePath(nodes.get(nodes.size()-1));

        //    for (int j=nodes.size()-2;j>=0;j--)
        //         treePath.pathByAddingChild(nodes.get(j));

        //    leftTree.expandPath(treePath);

        for (int i = 0; i < leftTree.getRowCount(); i++) {
            leftTree.expandRow(i);
        }

        //      leftTree.setSelectionPath(treePath);

        //   leftTree.select
        //leftTree.scrollPathToVisible(//treePath);
        //     leftTree.setSelectionRow(3);


        leftScrollPane = new JScrollPane(leftTree);
        leftTree.setPreferredSize(new Dimension(200, 800));

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


    public JButton getTableDisplay() {
        return tableDisplay;
    }

    public ArrayList<GridIcon> getGridIconArrayList() {
        return gridIconArrayList;
    }


    /*
    public static void main(String[] args) throws Exception {
        new View.View();
        //  new View.View.AboutMe();
        //  new View.View.Help();
        //     new View.View.ProgressBar(80,100);
             new View.Settings();

        //    new View.View.OptionPane("File","C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png",100000000,"6 Match 2018",0,0);
        //    new View.View.OptionPane("Folder","C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png",100000000,"6 Match 2018",2,3);

        //    JPopupMenu popupMenu1= new View.View.PopMenu(false);
        //  JPopupMenu popupMenu2 =new View.View.PopMenu(true);


        //  popupMenu2.show(null,200,300);
    }
    */
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


    public DrawRect getDrawRect() {
        return drawRect;
    }

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

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

}


