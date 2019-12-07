package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Settings {
    private JTextField initialAddressTextField = new JTextField();
    private JTextField receivedFileAddress = new JTextField();
    private JTextField remoteComputerAddressTextField = new JTextField();
    private JTextField remoteComputerPort = new JTextField();

    //Place to save
    private String initialAddressString;
    private String receivedFileAddressString;
    private String remoteComputerAddressString;
    private String remoteComputerPortString;
    private String intervalString;
    private String lookAndFeelString;
    private String path = "C:\\Users\\erfan\\Desktop";
    private String[] details;


    private JLabel initialAddressLabel = new JLabel("Initial Address:");
    private JLabel incomingFileAddressLabel = new JLabel("Received file Address:");
    private JLabel otherPCAddressLabel = new JLabel("Remote computer Address:");
    private JLabel otherPCPortLabel = new JLabel("Remote computer port:");
    private JLabel lookNFeelLabel = new JLabel("Select look and feel:");
    private JLabel initialDisplayFormatLabel = new JLabel("Select initial display format:");
    private JLabel syncIntervalLabel = new JLabel("Synchronizing interval:");
    private JLabel flashBackLabel = new JLabel("Maximum number of flashbacks:");
    private JFrame frame = new JFrame("View.Settings");
    private String[] syncIntervals = {"never", "1 minute", "5 minutes", "30 minutes", "1 hour"}, lookAndFeels = {"default", "CrossPlatform", "Motif", "System", "WindowsClassic", "Nimbus"};
    private String[] flashBacks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private JComboBox lookNFeel = new JComboBox(lookAndFeels);
    private JComboBox syncInterval = new JComboBox(syncIntervals);
    private JComboBox maxFlashbacks = new JComboBox(flashBacks);
    private JCheckBox tableDisplayFormatCheckBox = new JCheckBox("Table");
    private JCheckBox gridDisplayFormatCheckBox = new JCheckBox("Grid");
    String dummy;



    public Settings() {
        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 2));
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\settingIcon.png").getImage());
        frame.setLayout(new GridLayout(8, 2));

        gridDisplayFormatCheckBox.setSelected(true);
        tableDisplayFormatCheckBox.setSelected(false);

        frame.setLocation(500, 400);
        frame.setSize(350, 200);

        frame.add(initialAddressLabel);
        frame.add(initialAddressTextField);

        frame.add(incomingFileAddressLabel);
        frame.add(receivedFileAddress);

        frame.add(otherPCAddressLabel);
        frame.add(remoteComputerAddressTextField);

        frame.add(otherPCPortLabel);
        frame.add(remoteComputerPort);

        frame.add(lookNFeelLabel);
        frame.add(lookNFeel);

        frame.add(initialDisplayFormatLabel);
        frame.add(checkBoxPanel);

        checkBoxPanel.add(gridDisplayFormatCheckBox);
        checkBoxPanel.add(tableDisplayFormatCheckBox);

        frame.add(syncIntervalLabel);
        frame.add(syncInterval);

        frame.add(flashBackLabel);
        frame.add(maxFlashbacks);

        //Add listeners
        initialAddressTextField.addActionListener(new InitialAddressListener());
        gridDisplayFormatCheckBox.addActionListener(new GridTick());
        tableDisplayFormatCheckBox.addActionListener(new TableTick());
        receivedFileAddress.addActionListener(new ReceivedFileAddressListener());
        remoteComputerAddressTextField.addActionListener(new RemoteComputerAddressListener());
        remoteComputerPort.addActionListener(new RemoteComputerPortListener());

        frame.setVisible(true);
    }

    public JTextField getRemoteComputerAddressTextField() {
        return remoteComputerAddressTextField;
    }

    public JTextField getInitialAddressTextField() {
        return initialAddressTextField;
    }

    public JTextField getReceivedFileAddress() {
        return receivedFileAddress;
    }

    public JTextField getOtherPCAddress() {
        return remoteComputerAddressTextField;
    }

    public JTextField getRemoteComputerPort() {
        return remoteComputerPort;
    }

    public JComboBox getLookNFeel() {
        return lookNFeel;
    }

    public JComboBox getSyncInterval() {
        return syncInterval;
    }

    public JComboBox getMaxFlashbacks() {
        return maxFlashbacks;
    }


    public JCheckBox getTableDisplayFormatCheckBox() {
        return tableDisplayFormatCheckBox;
    }

    public JCheckBox getGridDisplayFormatCheckBox() {
        return gridDisplayFormatCheckBox;
    }

    class InitialAddressListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            initialAddressString = initialAddressTextField.getText();
        }
    }

    class GridTick implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            tableDisplayFormatCheckBox.setSelected(false);
            gridDisplayFormatCheckBox.setSelected(true);
        }
    }

    class TableTick implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            tableDisplayFormatCheckBox.setSelected(true);
            gridDisplayFormatCheckBox.setSelected(false);
        }
    }

    class ReceivedFileAddressListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            receivedFileAddressString = receivedFileAddress.getText();
        }
    }

    class RemoteComputerAddressListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            remoteComputerAddressString = remoteComputerAddressTextField.getText();
        }
    }

    class RemoteComputerPortListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            remoteComputerPortString = remoteComputerPort.getText();
        }
    }

    class LookAndFeelListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            lookAndFeelString = lookNFeel.getName();
        }
    }

    class SyncListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            intervalString = syncInterval.getName();
        }
    }

    void writeUpdate() {
        String initialAddressString;
        String receivedFileAddressString;
        String remoteComputerAddressString;
        String remoteComputerPortString;
        String intervalString;
        String lookAndFeelString;
    }

    void read() {

    }

}
