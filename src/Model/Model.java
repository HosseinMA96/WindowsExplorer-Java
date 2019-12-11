/**
 * Class for representing Model class, which holds solid information of our program and defines method to change those information.
 * Holds current address and allFiles affiliated to current address.
 */
package Model;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


public class Model {

    private File[] allFiles;
    private String currentAddress = "";
    private String syncPath;
    private boolean isGridDisplay;
    private String initialAddress, receivedFileAddress, remoteComputerAddress, remoteComputerPort, lookAndFeel, displayFormat, syncInterval, flashBackNumber;
    private boolean firsTimeAddressLoad = true;
   private String firstTimeAddress;

    /**
     * Getter for
     *
     * @return allFiles
     */
    public File[] getAllFiles() {
        return allFiles;
    }

    /**
     * Getter for current address
     *
     * @return allFiles
     */
    public String getCurrentAddress() {
        return currentAddress;
    }

    /**
     * Setter for currentAddress
     *
     * @param currentAddress
     */
    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    /**
     * A method to upgrade allFiles according to currentAddress
     */
    public void upgradeFiles() {
        try {

            File f = new File(currentAddress);
            if (f.isDirectory()) {
                allFiles = f.listFiles();

            } else
                throw new Exception("Address is not a directory");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Current address is not a directory");
            upgradeFiles();
        }


    }

    /**
     * A Method to rename a file
     *
     * @param prevName
     * @param newName
     */
    public void renameFile(String prevName, String newName) {

        String dummy;
        File oldFile = new File(prevName);

        dummy = oldFile.getParentFile().getAbsolutePath() + "\\" + newName;
        File newFile = new File(dummy);

        if (!oldFile.renameTo(newFile))
            JOptionPane.showMessageDialog(null, "Renaming failed. Either file is secured or current name already exists or new name is invalid.", "Eror", 3);

    }

    /**
     * A Method to delete a file f
     * @param f
     */
    public void deleteFile(File f) {

        if (f.isFile()) {
            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {

            }
        }

        if (f.isDirectory()) {
            File[] temp = f.listFiles();

            for (int i = 0; i < temp.length; i++)
                deleteFile(temp[i]);

            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {

            }
        }


    }

    /**
     * A Method to load saved information such as initial address or initial display type
     */
    public void loadSettings() {

        try {
            List<String> allLines = Files.readAllLines(Paths.get("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\JFileManager_Settings.txt"));

            if (firsTimeAddressLoad)
                {
                    this.setCurrentAddress(allLines.get(0));
                    firstTimeAddress=new String(currentAddress);
                }


                try {
                    isGridDisplay = Boolean.parseBoolean(allLines.get(5));

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error in finding the initial display mode. Was set to list display");
                    isGridDisplay = false;
                }

                firsTimeAddressLoad = false;




        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A Method to save settings to the file, so it can be loaded next time
     */
    public void writeSettingsToFile() {
        try {

            File f = new File("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\JFileManager_Settings.txt");


            if (new File(f.getAbsolutePath()).exists())
                deleteFile(new File(f.getAbsolutePath()));

            FileWriter fw = new FileWriter("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\JFileManager_Settings.txt");


            fw.write(initialAddress + "\r\n");

            fw.write(receivedFileAddress + "\r\n");

            fw.write(remoteComputerAddress + "\r\n");

            fw.write(remoteComputerPort + "\r\n");

            fw.write(lookAndFeel + "\r\n");;

            fw.write(isGridDisplay + "\r\n");

            fw.write(syncInterval + "\r\n");

            fw.write(flashBackNumber + "\r\n");

            fw.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to save settings.", "Eror", 0);
            ex.printStackTrace();
        }


    }

    /**
     * Setter for syncPath
     * @param syncPath
     */
    public void setSyncPath(String syncPath) {
        this.syncPath = syncPath;
    }

    /**
     * Setter for initialAddress
     * @param initialAddress
     */
    public void setInitialAddress(String initialAddress) {
        this.initialAddress = initialAddress;
    }

    /**
     * Setter for receivedFileAddress
     * @param receivedFileAddress
     */
    public void setReceivedFileAddress(String receivedFileAddress) {
        this.receivedFileAddress = receivedFileAddress;
    }

    /**
     * Setter for remoteComputerAddress
     * @param remoteComputerAddress
     */
    public void setRemoteComputerAddress(String remoteComputerAddress) {
        this.remoteComputerAddress = remoteComputerAddress;
    }

    /**
     * Getter for firstTimeAddress
     * @return firstTimeAddress
     */
    public String getFirstTimeAddress() {
        return firstTimeAddress;
    }

    /**
     * Setter for remoteComputerPort
     * @param remoteComputerPort
     */
    public void setRemoteComputerPort(String remoteComputerPort) {
        this.remoteComputerPort = remoteComputerPort;
    }

    /**
     * setter for lookAndFeel
     * @param lookAndFeel
     */
    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    /**
     * Setter for syncInterval
     * @param syncInterval
     */
    public void setSyncInterval(String syncInterval) {
        this.syncInterval = syncInterval;
    }

    /**
     * Setter flashBackNumber
     * @param flashBackNumber
     */
    public void setFlashBackNumber(String flashBackNumber) {
        this.flashBackNumber = flashBackNumber;
    }

    /**
     * Getter for initialAddress
     * @return initialAddress
     */
    public String getInitialAddress() {
        return initialAddress;
    }

    /**
     * Getter for receivedFileAddress
     * @return  receivedFileAddress
     */
    public String getReceivedFileAddress() {
        return receivedFileAddress;
    }

    /**
     * Getter for remoteComputerAddress
     * @return remoteComputerAddress
     */
    public String getRemoteComputerAddress() {
        return remoteComputerAddress;
    }

    /**
     * Getter for remoteComputerPort
     * @return remoteComputerPort
     */
    public String getRemoteComputerPort() {
        return remoteComputerPort;
    }

    /**
     * Getter for lookAndFeel
     * @return lookAndFeel
     */
    public String getLookAndFeel() {
        return lookAndFeel;
    }


    /**
     * Getter for syncInterval
     * @return
     */
    public String getSyncInterval() {
        return syncInterval;
    }

    /**
     * Getter for flashBackNumber
     * @return
     */
    public String getFlashBackNumber() {
        return flashBackNumber;
    }

    /**
     * Satic method for pasting a file From address "from" to address "to"
     * @param from
     * @param to
     * @throws IOException
     */
    public static void pasteFile(String from, String to) throws IOException {

        File sourceFolder = new File(from);

        File destinationFolder = new File(to);

        if (sourceFolder.isDirectory()) {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();

            }

            //Get all files from source directory
            String files[] = sourceFolder.list();

            //Iterate over all files and copy them to destinationFolder one by one
            for (String file : files) {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);

                //Recursive function call
                pasteFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            }
        } else {
            //Copy the file content from one place to another;
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);

        }


    }


    /**
     * Set currentAddress as syncPath
     */
    public void setAsSynchPath() {
        syncPath = currentAddress;
    }

    /**
     * A method to make a new file
     */
    public void newFile() {

        String newFileName = JOptionPane.showInputDialog(null, "Enter new file name");
        upgradeFiles();

        for (int i = 0; i < allFiles.length; i++) {
            File f = allFiles[i];

            if (f.isFile() && f.getName().equals(newFileName)) {
                newFileName += "_copy";
                i = 0;
                continue;
            }

        }

        File F = new File(currentAddress + "\\" + newFileName);

        try {
            F.createNewFile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to create File");
        }
    }

    /**
     * Set display as gridDisplay
     * @param gridDisplay
     */
    public void setGridDisplay(boolean gridDisplay) {
        isGridDisplay = gridDisplay;
    }

    /**
     * Get isGridDisplay
     * @return isGridDisplay
     */
    public boolean getGridDisplay() {
        return isGridDisplay;
    }

    /**
     * A method to make a new folder
     */
    public void newFolder() {
        String newFolderName = JOptionPane.showInputDialog(null, "Enter New Folder name");
        upgradeFiles();

        for (int i = 0; i < allFiles.length; i++) {
            File f = allFiles[i];

            if (!f.isFile() && f.getName().equals(newFolderName)) {
                newFolderName += "_copy";
                i = 0;
                continue;
            }

        }

        File F = new File(currentAddress + "\\" + newFolderName);

        //try {
        if (!F.mkdir())
            JOptionPane.showMessageDialog(null, "Unable to create Folder", "Eror", 3);

    }

    /**
     * A method for going to current address parent
     */
    public void goToParent() {
        File f = new File(currentAddress);

        try {

            f = f.getParentFile();

            if (f.exists())
                currentAddress = f.getAbsolutePath();


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No parent directory exists", "Error", 2);
        } finally {
            upgradeFiles();
        }

    }


}
