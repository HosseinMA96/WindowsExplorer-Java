import javafx.scene.control.CheckBox;

import javax.swing.*;
import java.awt.*;

public class Settings
{
    JTextField initialAddress=new JTextField();
    JTextField incomingFileAddress=new JTextField();
    JTextField otherPCAddress=new JTextField();
    JTextField otherPCPort=new JTextField();




    JLabel initialAddressLabel=new JLabel("Initial Address:");
    JLabel incomingFileAddressLabel=new JLabel("Received file Address:");
    JLabel otherPCAddressLabel=new JLabel("Remote computer Address:");
    JLabel otherPCPortLabel=new JLabel("Remote computer port:");
    JLabel lookNFeelLabel=new JLabel("Select look and feel:");
    JLabel initialDisplayFormatLabel=new JLabel("Select initial display format:");
    JLabel syncIntervalLabel=new JLabel("Synchronizing interval:");
    JFrame frame=new JFrame("Settings");
    String []syncIntervals={ "never" ,"1 minute", "5 minutes", "30 minutes", "1 hour"},lookAndFeels={"default","CrossPlatform","Motif","System","WindowsClassic","Nimbus"};

    JComboBox lookNFeel=new JComboBox(lookAndFeels);
    JComboBox syncInterval=new JComboBox(syncIntervals);
    JCheckBox tableDisplayFormat=new JCheckBox("Table");
    JCheckBox gridDisplayFormat=new JCheckBox("Grid");

    public Settings ()
    {
        JPanel checkBoxPanel=new JPanel(new GridLayout(1,2));
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\settingIcon.png").getImage());
        frame.setLayout(new GridLayout(7,2));

        gridDisplayFormat.setSelected(true);
        tableDisplayFormat.setSelected(false);

        frame.setLocation(500,400);
        frame.setSize(350,200);

        frame.add(initialAddressLabel);
        frame.add(initialAddress);

        frame.add(incomingFileAddressLabel);
        frame.add(incomingFileAddress);

        frame.add(otherPCAddressLabel);
        frame.add(otherPCAddress);

        frame.add(otherPCPortLabel);
        frame.add(otherPCPort);

        frame.add(lookNFeelLabel);
        frame.add(lookNFeel);

        frame.add(initialDisplayFormatLabel);
        frame.add(checkBoxPanel);

        checkBoxPanel.add(gridDisplayFormat);
        checkBoxPanel.add(tableDisplayFormat);

        frame.add(syncIntervalLabel);
        frame.add(syncInterval);

        frame.setVisible(true);
    }
}
