/**
 * A class to make Settings Jframe
 */
package View;

import javax.swing.*;
import java.awt.*;


public class Settings {
    private JTextField initialAddressTextField = new JTextField();
    private JTextField receivedFileAddress = new JTextField();
    private JTextField remoteComputerAddressTextField = new JTextField();
    private JTextField remoteComputerPort = new JTextField();


    private JLabel initialAddressLabel = new JLabel("Initial Address:");
    private JLabel incomingFileAddressLabel = new JLabel("Received file Address:");
    private JLabel otherPCAddressLabel = new JLabel("Remote computer Address:");
    private JLabel otherPCPortLabel = new JLabel("Remote computer port:");
    private JLabel lookNFeelLabel = new JLabel("Select look and feel:");
    private JLabel initialDisplayFormatLabel = new JLabel("Select initial display format:");
    private JLabel syncIntervalLabel = new JLabel("Synchronizing interval:");
    private JLabel flashBackLabel = new JLabel("Maximum number of flashbacks:");
    private JFrame frame = new JFrame("View.Settings");
    private String[] syncIntervals = {"never", "1 minute", "5 minutes", "30 minutes", "1 hour"}, lookAndFeels = { "Motif", "System", "WindowsClassic", "Nimbus"};
    private String[] flashBacks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

    private JComboBox lookNFeel = new JComboBox(lookAndFeels);
    private JComboBox syncInterval = new JComboBox(syncIntervals);
    private JComboBox maxFlashbacks = new JComboBox(flashBacks);
    private JCheckBox tableDisplayFormatCheckBox = new JCheckBox("Table");
    private JCheckBox gridDisplayFormatCheckBox = new JCheckBox("Grid");
    private String firstTimeAddress;


    /**
     * Cusntructor for this class
     */
    public Settings() {
        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 2));
        frame.setResizable(false);
        frame.setIconImage(new ImageIcon("images\\settingIcon.png").getImage());
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

        frame.setVisible(true);
    }

    /**
     * Setter for setFirstTimeAddress
     *
     * @param firstTimeAddress
     */
    public void setFirstTimeAddress(String firstTimeAddress) {
        this.firstTimeAddress = firstTimeAddress;
    }

    /**
     * Getter for remoteComputerAddressTextField
     *
     * @return
     */
    public JTextField getRemoteComputerAddressTextField() {
        return remoteComputerAddressTextField;
    }

    /**
     * Getter for initialAddressTextField
     *
     * @return initialAddressTextField
     */
    public JTextField getInitialAddressTextField() {
        return initialAddressTextField;
    }

    /**
     * Getter for receivedFileAddress
     *
     * @return receivedFileAddress
     */
    public JTextField getReceivedFileAddress() {
        return receivedFileAddress;
    }

    /**
     * Getter for getOtherPCAddress
     *
     * @return getOtherPCAddress
     */
    public JTextField getOtherPCAddress() {
        return remoteComputerAddressTextField;
    }

    /**
     * Getter for remoteComputerPort
     *
     * @return remoteComputerPort
     */
    public JTextField getRemoteComputerPort() {
        return remoteComputerPort;
    }

    /**
     * Getter for lookNFeel
     *
     * @return lookNFeel
     */
    public JComboBox getLookNFeel() {
        return lookNFeel;
    }

    /**
     * Getter for syncInterval
     *
     * @return syncInterval
     */
    public JComboBox getSyncInterval() {
        return syncInterval;
    }

    /**
     * Getter for maxFlashbacks
     *
     * @return maxFlashbacks
     */
    public JComboBox getMaxFlashbacks() {
        return maxFlashbacks;
    }

    /**
     * Getter for tableDisplayFormatCheckBox
     *
     * @return tableDisplayFormatCheckBox
     */
    public JCheckBox getTableDisplayFormatCheckBox() {
        return tableDisplayFormatCheckBox;
    }

    /**
     * Getter for gridDisplayFormatCheckBox
     *
     * @return gridDisplayFormatCheckBox
     */
    public JCheckBox getGridDisplayFormatCheckBox() {
        return gridDisplayFormatCheckBox;
    }

}
