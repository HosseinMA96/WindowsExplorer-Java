package View;

import javafx.scene.control.CheckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings
{
    JTextField initialAddress=new JTextField();
    JTextField incomingFileAddress=new JTextField();
    JTextField otherPCAddress=new JTextField();
    JTextField otherPCPort=new JTextField();

    //Place to save
    String initialAddressString;
    String receivedFileAddressString;
    String remoteComputerAddressString;
    String remoteComputerPortString;
    String intervalString;
    String lookAndFeelString;
    String path="C:\\Users\\erfan\\Desktop";
    String []details;


    JLabel initialAddressLabel=new JLabel("Initial Address:");
    JLabel incomingFileAddressLabel=new JLabel("Received file Address:");
    JLabel otherPCAddressLabel=new JLabel("Remote computer Address:");
    JLabel otherPCPortLabel=new JLabel("Remote computer port:");
    JLabel lookNFeelLabel=new JLabel("Select look and feel:");
    JLabel initialDisplayFormatLabel=new JLabel("Select initial display format:");
    JLabel syncIntervalLabel=new JLabel("Synchronizing interval:");
    JLabel flashBackLabel=new JLabel("Maximum number of flashbacks:");
    JFrame frame=new JFrame("View.Settings");
    String []syncIntervals={ "never" ,"1 minute", "5 minutes", "30 minutes", "1 hour"},lookAndFeels={"default","CrossPlatform","Motif","System","WindowsClassic","Nimbus"};
    String [] flashBacks={"1","2","3","4","5","6","7","8","9","10"};

    JComboBox lookNFeel=new JComboBox(lookAndFeels);
    JComboBox syncInterval=new JComboBox(syncIntervals);
    JComboBox maxFlashbacks=new JComboBox(flashBacks);
    JCheckBox tableDisplayFormat=new JCheckBox("Table");
    JCheckBox gridDisplayFormat=new JCheckBox("Grid");

    public Settings ()
    {
        JPanel checkBoxPanel=new JPanel(new GridLayout(1,2));
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\settingIcon.png").getImage());
        frame.setLayout(new GridLayout(8,2));

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

        frame.add(flashBackLabel);
        frame.add(maxFlashbacks);

        //Add listeners
        initialAddress.addActionListener(new InitialAddressListener());
        gridDisplayFormat.addActionListener(new GridTick());
        tableDisplayFormat.addActionListener(new TableTick());
        incomingFileAddress.addActionListener(new ReceivedFileAddressListener());
        otherPCAddress.addActionListener(new RemoteComputerAddressListener());
        otherPCPort.addActionListener(new RemoteComputerPortListener());
        frame.setVisible(true);
    }

    class InitialAddressListener implements ActionListener{
        public void actionPerformed(ActionEvent event)
        {
            initialAddressString=initialAddress.getText();
        }
    }

    class GridTick implements ActionListener{
        public void actionPerformed(ActionEvent event)
        {
            tableDisplayFormat.setSelected(false);
            gridDisplayFormat.setSelected(true);
        }
    }

    class TableTick implements ActionListener{
        public void actionPerformed(ActionEvent event)
        {
            tableDisplayFormat.setSelected(true);
            gridDisplayFormat.setSelected(false);
        }
    }

    class ReceivedFileAddressListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            receivedFileAddressString=incomingFileAddress.getText();
        }
    }

    class RemoteComputerAddressListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            remoteComputerAddressString=otherPCAddress.getText();
        }
    }

    class RemoteComputerPortListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            remoteComputerPortString=otherPCPort.getText();
        }
    }

    class LookAndFeelListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            lookAndFeelString=lookNFeel.getName();
        }
    }

    class SyncListener implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            intervalString=syncInterval.getName();
        }
    }

    void writeUpdate()
    {
        String initialAddressString;
        String receivedFileAddressString;
        String remoteComputerAddressString;
        String remoteComputerPortString;
        String intervalString;
        String lookAndFeelString;
    }

    void read()
    {

    }

}
