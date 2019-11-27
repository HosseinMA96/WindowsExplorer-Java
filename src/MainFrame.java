import com.sun.xml.internal.messaging.saaj.soap.ver1_1.BodyElement1_1Impl;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.Scanner;


public class MainFrame extends JFrame {

    JFrame frame =new JFrame("JFileManager");



    //Adress and Search Textfiled at top
    JTextField addressTextField=new JTextField("Address",20);
    JTextField searchTextField=new JTextField("Search",20);

    //Three arrows
    JButton uppArrow=new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png"));
    JButton leftArrow =new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\leftArrow.png"));
    JButton rightArrow=new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\rightArrow.png"));

    //Two View buttons (Grid and table) located on lower right part of the frame
    JButton gridDisplay=new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\gridDisplay.png"));
    JButton tableDisplay =new JButton(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\tableDisplay.png"));

    //Upper menu bar
    JMenuBar upperMenuBar=new JMenuBar();

    //Upper Menubar Items
    JMenu file=new JMenu("File");
    JMenu edit=new JMenu("Edit");
    JMenu help=new JMenu("Help");

    //File Menu items
    JMenuItem file_NewFile = new JMenuItem("New File");
    JMenuItem  file_NewFolder = new JMenuItem("New Folder");
    JMenuItem  file_Delete = new JMenuItem("Delete");
    JMenuItem file_SetCurrentForSync= new JMenuItem("Set as Sync Path");

    //Edit Menu Items
    JMenuItem edit_Rename = new JMenuItem("Rename ");
    JMenuItem edit_Copy = new JMenuItem("Copy");
    JMenuItem edit_Cut = new JMenuItem("Cut");
    JMenuItem edit_Paste = new JMenuItem("Paste");
    JMenuItem edit_Synchronize = new JMenuItem("Sync");

    //Help Menu Items
    JMenuItem help_AboutMe = new JMenuItem("About me ");
    JMenuItem help_Properties = new JMenuItem("Properties");
    JMenuItem  help_Help= new JMenuItem("Help");

    //Number of Selected labels
    JLabel numberOfSelectedLabel=new JLabel("Selecteds");


    //Split Pane
    JSplitPane splitPane=null;

    //Jtree for left side hierarchy
    JTree leftTree=null;

    //Right Side panel for folder and file display
    JPanel rightSidePanel=new JPanel();

    //Left scroll pane and right scroll pane for adding leftTree and rightSidePanel
    JScrollPane leftScrollPane=null;
    JScrollPane rightScrollPane=null;





    public MainFrame()
    {
        //Set frame's layout
        frame.setLayout(new BorderLayout());

        //set ICon
        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\mainIcon.png").getImage());

        //Two panels for upper Panel
        JPanel upperPanel =new JPanel(new BorderLayout());
        JPanel upperPanel_FlowPanel=new JPanel(new FlowLayout());

        //Panel for lower panel
        JPanel lowerPanel=new JPanel(new BorderLayout());
        JPanel lowerPanel_FlowPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT));

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
        upperPanel.add(upperPanel_FlowPanel,BorderLayout.WEST);

        //Adding Address textField as upper panel's center panel
        upperPanel.add(addressTextField,BorderLayout.CENTER);

        //Adding Address textField as upper panel's east panel
        upperPanel.add(searchTextField,BorderLayout.EAST);

        //Setting arrow button's sizes
        leftArrow.setPreferredSize(new Dimension(17,17));
        rightArrow.setPreferredSize(new Dimension(17,17));
        uppArrow.setPreferredSize(new Dimension(17,17));

        //Add Upper panel to frame's north section
        frame.add(upperPanel,BorderLayout.NORTH);

        //Adding menu bar to frame
        frame.setJMenuBar(upperMenuBar);

        //Setting size of gridDisplay and tableDisplay buttons
        gridDisplay.setPreferredSize(new Dimension(17,17));
        tableDisplay.setPreferredSize(new Dimension(17,17));

        //Add grid Display and table Display to lowerPanel's flowPanel
        lowerPanel_FlowPanel.add(gridDisplay);
        lowerPanel_FlowPanel.add(tableDisplay);

        //Set lowerPanel's Flowpanel as lowerPanel's East
        lowerPanel.add(lowerPanel_FlowPanel,BorderLayout.EAST);

        //Set selectedLable as lowerPanel's East
        lowerPanel.add(numberOfSelectedLabel,BorderLayout.WEST);

        //Set LowerPanel as frame's lowerPanel
        frame.add(lowerPanel,BorderLayout.SOUTH);



        //Temporal nodes for Jtree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode vegetableNode = new DefaultMutableTreeNode("Vegetables");
        DefaultMutableTreeNode fruitNode = new DefaultMutableTreeNode("Fruits");
        root.add(vegetableNode);
        root.add(fruitNode);

        //initialize Jtree for the temporal root
        leftTree=new JTree(root,true);

        //Initialize scroll panes so that later we can scroll
        leftScrollPane=new JScrollPane(leftTree);
       rightScrollPane=new JScrollPane(rightSidePanel);

        rightScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        //Make a split pane and put it in the frame's Center
        splitPane=new JSplitPane(SwingConstants.VERTICAL,leftScrollPane,rightScrollPane);
        frame.add(splitPane,BorderLayout.CENTER);

        setGridDisplay();
       // setListDisplay();
       // rightScrollPane.setVerticalScrollBar();

        
      //  splitPane.setOrientation(SwingConstants.VERTICAL);





        frame.setLocation(400,400);
        frame.setSize(800,500);
        frame.setVisible(true);


    }

    void setGridDisplay()
    {
        rightSidePanel.setLayout(new GridLayout(4,4));

        for (int i=0;i<300;i++)
        {
            if(i%3==0)
                rightSidePanel.add(new GridFileIcon("file"+i));

            else
                rightSidePanel.add(new GridFolderIcon("folder"+i));

        }

    }

    void setListDisplay()
    {
        rightSidePanel.setLayout(new BorderLayout());
        JPanel northPanel=new JPanel(new GridLayout(1,4));

        JLabel name=new JLabel("Name");
        JLabel dateModified=new JLabel("Date modified");
        JLabel type=new JLabel("Type");
        JLabel objectSize=new JLabel("Size");

        northPanel.add(name);
        northPanel.add(dateModified);
        northPanel.add(type);
        northPanel.add(objectSize);

        rightSidePanel.add(northPanel,BorderLayout.NORTH);


        JPanel objectsListPanel=new JPanel(new GridLayout(6,4));

        for (int i=0;i<10;i++)
        {
            if(i%2==0)
            objectsListPanel.add(new ListFileIcon());

            else
                objectsListPanel.add(new ListFolderIcon());


            objectsListPanel.add(new JLabel(2000+i+""));

            if(i%2==0)
                objectsListPanel.add(new JLabel("File"));

            else
            objectsListPanel.add(new JLabel("Folder"));


            objectsListPanel.add(new JLabel(i*10+""));


        }

        rightSidePanel.add(objectsListPanel,BorderLayout.CENTER);


    }

    public static void main(String[] args) {
        new MainFrame();
        new AboutMe();
        new Help();
        new ProgressBar(80,100);
        new Settings();

        new OptionPane("File","C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png",100000000,"6 Match 2018",0,0);
        new OptionPane("Folder","C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\upArrow.png",100000000,"6 Match 2018",2,3);

        JPopupMenu popupMenu1= new PopMenu(false);
        JPopupMenu popupMenu2 =new PopMenu(true);


      //  popupMenu2.show(null,200,300);
    }

}
