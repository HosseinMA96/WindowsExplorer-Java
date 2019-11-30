import com.sun.deploy.trace.FileTraceListener;
import com.sun.xml.internal.messaging.saaj.soap.ver1_1.BodyElement1_1Impl;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;


public class MainFrame extends JFrame {

    JFrame frame = new JFrame("JFileManager");


    //Adress and Search Textfiled at top
    JTextField addressTextField = new JTextField("Address", 20);
    JTextField searchTextField = new JTextField("Search", 20);

    //Three arrows
    JButton uppArrow = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png"));
    JButton leftArrow = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\leftArrow.png"));
    JButton rightArrow = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\rightArrow.png"));

    //Two View buttons (Grid and table) located on lower right part of the frame
    JButton gridDisplay = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\gridDisplay.png"));
    JButton tableDisplay = new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\tableDisplay.png"));

    //Upper menu bar
    JMenuBar upperMenuBar = new JMenuBar();

    //Upper Menubar Items
    JMenu file = new JMenu("File");
    JMenu edit = new JMenu("Edit");
    JMenu help = new JMenu("Help");

    //File Menu items
    JMenuItem file_NewFile = new JMenuItem("New File");
    JMenuItem file_NewFolder = new JMenuItem("New Folder");
    JMenuItem file_Delete = new JMenuItem("Delete");
    JMenuItem file_SetCurrentForSync = new JMenuItem("Set as Sync Path");

    //Edit Menu Items
    JMenuItem edit_Rename = new JMenuItem("Rename ");
    JMenuItem edit_Copy = new JMenuItem("Copy");
    JMenuItem edit_Cut = new JMenuItem("Cut");
    JMenuItem edit_Paste = new JMenuItem("Paste");
    JMenuItem edit_Synchronize = new JMenuItem("Sync");

    //Help Menu Items
    JMenuItem help_AboutMe = new JMenuItem("About me ");
    JMenuItem help_Properties = new JMenuItem("Properties");
    JMenuItem help_Help = new JMenuItem("Help");

    //Number of Selected labels
    JLabel numberOfSelectedLabel = new JLabel("Selecteds");


    //Split Pane
    JSplitPane splitPane = null;

    //Jtree for left side hierarchy
    JTree leftTree = null;

    //Right Side panel for folder and file display
    JPanel rightSidePanel = new JPanel();

    //Left scroll pane and right scroll pane for adding leftTree and rightSidePanel
    JScrollPane leftScrollPane = null;
    JScrollPane rightScrollPane = null;

    //Starting pass
    String currentAddress = "C:\\\\Users\\\\erfan\\\\Desktop";

    //current file Holder
    File[] allFiles;

    //current GridIcons
    ArrayList<GridIcon>gridIconArrayList;


    public MainFrame() {
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

        //Add Help Menu Items
        help.add(help_Properties);
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
        makeLeftTree();

    //     setGridDisplay();
        setListDisplay();

        //Initialize scroll panes so that later we can scroll
        leftScrollPane = new JScrollPane(leftTree);
        rightScrollPane = new JScrollPane(rightSidePanel);

        rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        //Make a split pane and put it in the frame's Center
        splitPane = new JSplitPane(SwingConstants.VERTICAL, leftScrollPane, rightScrollPane);
        frame.add(splitPane, BorderLayout.CENTER);




        // rightScrollPane.setVerticalScrollBar();


        //  splitPane.setOrientation(SwingConstants.VERTICAL);


        frame.setLocation(400, 400);
        frame.setSize(1000, 700);
        frame.setVisible(true);

        upgradeFiles();
    }

    void setGridDisplay() {
        upgradeFiles();

        rightSidePanel = new JPanel(new GridLayout(40,6,2,2));
        gridIconArrayList=new ArrayList<>();

        for (File f :allFiles)
        {
            if(f.isFile()) {

                GridFileIcon fileIcon=new GridFileIcon(f.getName(),FileSystemView.getFileSystemView().getSystemIcon( f ));
                fileIcon.setPreferredSize(new Dimension(5,5));
                gridIconArrayList.add(fileIcon);
                rightSidePanel.add(fileIcon);
            }
            else {
                gridIconArrayList.add(new GridFolderIcon(f.getName(),FileSystemView.getFileSystemView().getSystemIcon( f )));
                rightSidePanel.add(gridIconArrayList.get(gridIconArrayList.size()-1));
            }

        }


//        rightScrollPane.setVertiscalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        //  rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    }

    void setListDisplay() {
        FileTableModel model=new FileTableModel(new File(currentAddress));
        JTable  table =new JTable(model);


        rightSidePanel=new JPanel(new BorderLayout());

        rightScrollPane=new JScrollPane(table);
        rightSidePanel.add(rightScrollPane,BorderLayout.CENTER);


    }




    void makeLeftTree() {
        //Temporal nodes for Jtree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
        root.add(vegetableNode);
        root.add(fruitNode);

        //initialize Jtree for the temporal root
        this.leftTree = new JTree(root, true);
        leftScrollPane = new JScrollPane();
        leftScrollPane.add(leftTree);
    }

    void upgradeFiles() {
        try
        {
            File f = new File(currentAddress);
            //    System.out.println(files.exists());

            if(f.isDirectory())
                allFiles = f.listFiles();

            else
                throw new Exception("Address is not a directory");
//
        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Starting address was set to desktop");
            currentAddress="C:\\Users\\erfan\\Desktop";
            upgradeFiles();
        }

//
    }

    public static void main(String[] args) throws Exception {
        new MainFrame();
        //  new AboutMe();
        //  new Help();
        //     new ProgressBar(80,100);
        //      new Settings();

        //    new OptionPane("File","C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png",100000000,"6 Match 2018",0,0);
        //    new OptionPane("Folder","C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png",100000000,"6 Match 2018",2,3);

        //    JPopupMenu popupMenu1= new PopMenu(false);
        //  JPopupMenu popupMenu2 =new PopMenu(true);


        //  popupMenu2.show(null,200,300);
    }

}

class FileTableModel extends AbstractTableModel{
     File dir;
    protected String[] filenames;

    protected String [] columnNames = new String[] {"icon","name","size","last modified","type"};

    protected  Class[] columnClassses= new Class[]{
            String.class,Long.class, Date.class,Icon.class
    };

    public  int getColumnCount(){return 5;};
    public int getRowCount(){return filenames.length;};

    public FileTableModel(File dir){
        this.dir=dir;
        this.filenames=dir.list();
    }

    public String getColumnName(int col){
        return columnNames[col];
    }
    public  Class getColumnsClass(int col){return columnClassses[col];}
    //FileSystemView.getFileSystemView().getSystemIcon( File );
    public Object getValueAt(int row,int col){
        File f=new File(dir,filenames[row]);
    //    {"icon","name","size","last modified","type"};
        switch (col){
            case 0:
               // return FileSystemView.getFileSystemView().getSystemIcon(f);
                return new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\fileIcon.png");
            case 1:
                return filenames[row];

            case 2:
                return f.length();


            case 4:
                if (f.isFile())
                    return"File";

                return "Folder";

            case 3:return new Date(f.lastModified());

            default:
                return null;
        }
    }

}
