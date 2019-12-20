/**
 * Class for representing Model class, which holds solid information of our program and defines method to change those information.
 * Holds current address and allFiles affiliated to current address.
 */
package Model;

import Client.ReceiverClient;
import Client.SenderClient;
import Memento.CareTaker;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Model {

    private File[] allFiles;
    private String currentAddress = "";
    private String syncPath;
    private boolean isGridDisplay;
    private String initialAddress, receivedFileAddress, remoteComputerAddress, remoteComputerPort, lookAndFeel, displayFormat, syncInterval, flashBackNumber;
    private boolean firsTimeAddressLoad = true;
    private String firstTimeAddress;
    private CareTaker careTaker;
    private ArrayList<String> loggedFileNames = new ArrayList<>();
    private ArrayList<String> deletedFileNames = new ArrayList<>();
    private ArrayList<String> addedFileNames = new ArrayList<>();
    private String tag = "none";
    private String nestPath = "C:\\Users\\erfan\\Desktop\\nest1";
    private File nestFile;

    /**
     * Setter for careTaker
     *
     * @param careTaker
     */
    public void setCareTaker(CareTaker careTaker) {
        this.careTaker = careTaker;
    }

    /**
     * Getter for careTaker
     *
     * @return careTaker
     */
    public CareTaker getCareTaker() {
        return careTaker;
    }

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
     * Setter for currentAddress, and records the result in careTaker
     *
     * @param currentAddress
     */
    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;

        if (careTaker != null)
            careTaker.openNewAddress(currentAddress);
    }

    /**
     * Setter for currentAddress, and does not record  the result in careTaker
     *
     * @param s
     */
    public void setCurrentAddressWithoutCareTakerModification(String s) {
        this.currentAddress = s;
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

        if (newName == null || newName.length() == 0)
            return;

        String dummy;
        File oldFile = new File(prevName);

        dummy = oldFile.getParentFile().getAbsolutePath() + "\\" + newName;
        File newFile = new File(dummy);

        if (!oldFile.renameTo(newFile))
            JOptionPane.showMessageDialog(null, "Renaming failed. Either file is secured or current name already exists or new name is invalid.", "Eror", 3);

    }

    /**
     * A Method to delete a file f
     *
     * @param f
     */
    public void deleteFile(File f) {

        if (f.getParentFile().getAbsolutePath().equals(syncPath)) {
            if (deletedFileNames == null)
                deletedFileNames = new ArrayList<>();

            boolean alreadyExists = false;

            for (int i = 0; i < deletedFileNames.size(); i++)
                if (deletedFileNames.get(i).equals(f.getName())) {
                    alreadyExists = true;
                    break;
                }

            if (!alreadyExists)
                deletedFileNames.add(f.getName());
        }

        if (f.isFile()) {
            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error happened in deleting process", "Error", 1);
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
            List<String> allLines = Files.readAllLines(Paths.get("JFileManager_Settings.txt"));

            if (firsTimeAddressLoad) {
                this.setCurrentAddress(allLines.get(0));
                firstTimeAddress = new String(currentAddress);
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

            File f = new File("JFileManager_Settings.txt");


            if (new File(f.getAbsolutePath()).exists())
                deleteFile(new File(f.getAbsolutePath()));

            FileWriter fw = new FileWriter("JFileManager_Settings.txt");

            if (initialAddress == null || initialAddress == "" || initialAddress == "null") {
                if (firstTimeAddress != null)
                    fw.write(firstTimeAddress + "\r\n");

                else
                    fw.write("C:" + "\r\n");

            } else
                fw.write(initialAddress + "\r\n");

            fw.write(receivedFileAddress + "\r\n");

            fw.write(remoteComputerAddress + "\r\n");

            fw.write(remoteComputerPort + "\r\n");

            fw.write(lookAndFeel + "\r\n");


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
     *
     * @param syncPath
     */
    public void setSyncPath(String syncPath) {

        initializeWatching(new File(syncPath));
        this.syncPath = syncPath;
        receivedFileAddress = syncPath;
    }

    /**
     * Setter for initialAddress
     *
     * @param initialAddress
     */
    public void setInitialAddress(String initialAddress) {
        this.initialAddress = initialAddress;
    }

    /**
     * Setter for receivedFileAddress
     *
     * @param receivedFileAddress
     */
    public void setReceivedFileAddress(String receivedFileAddress) {
        this.receivedFileAddress = receivedFileAddress;
        this.syncPath = receivedFileAddress;
    }

    /**
     * Setter for remoteComputerAddress
     *
     * @param remoteComputerAddress
     */
    public void setRemoteComputerAddress(String remoteComputerAddress) {
        this.remoteComputerAddress = remoteComputerAddress;
    }

    /**
     * Getter for firstTimeAddress
     *
     * @return firstTimeAddress
     */
    public String getFirstTimeAddress() {
        return firstTimeAddress;
    }

    /**
     * Setter for remoteComputerPort
     *
     * @param remoteComputerPort
     */
    public void setRemoteComputerPort(String remoteComputerPort) {
        this.remoteComputerPort = remoteComputerPort;
    }

    /**
     * setter for lookAndFeel
     *
     * @param lookAndFeel
     */
    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    /**
     * Setter for syncInterval
     *
     * @param syncInterval
     */
    public void setSyncInterval(String syncInterval) {
        this.syncInterval = syncInterval;
    }

    /**
     * Setter flashBackNumber
     *
     * @param flashBackNumber
     */
    public void setFlashBackNumber(String flashBackNumber) {
        this.flashBackNumber = flashBackNumber;
    }

    /**
     * Getter for initialAddress
     *
     * @return initialAddress
     */
    public String getInitialAddress() {
        return initialAddress;
    }

    /**
     * Getter for receivedFileAddress
     *
     * @return receivedFileAddress
     */
    public String getReceivedFileAddress() {
        return receivedFileAddress;
    }

    /**
     * Getter for remoteComputerAddress
     *
     * @return remoteComputerAddress
     */
    public String getRemoteComputerAddress() {
        return remoteComputerAddress;
    }

    /**
     * Getter for remoteComputerPort
     *
     * @return remoteComputerPort
     */
    public String getRemoteComputerPort() {
        return remoteComputerPort;
    }

    /**
     * Getter for lookAndFeel
     *
     * @return lookAndFeel
     */
    public String getLookAndFeel() {
        return lookAndFeel;
    }


    /**
     * Getter for syncInterval
     *
     * @return
     */
    public String getSyncInterval() {
        return syncInterval;
    }

    /**
     * Getter for flashBackNumber
     *
     * @return
     */
    public String getFlashBackNumber() {
        return flashBackNumber;
    }

    /**
     * Satic method for pasting a file From address "from" to address "to"
     *
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
        if (newFileName == null || newFileName.length() == 0)
            return;

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
     *
     * @param gridDisplay
     */
    public void setGridDisplay(boolean gridDisplay) {
        isGridDisplay = gridDisplay;
    }

    /**
     * Get isGridDisplay
     *
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
        if (newFolderName == null || newFolderName.length() == 0)
            return;

        upgradeFiles();
        try {
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to create Folder", "Eror", 3);
        }

    }

    /**
     * A method for going to current address parent
     */
    public void goToParent() {
        File f = new File(currentAddress);

        try {

            f = f.getParentFile();

            if (f.exists())
                setCurrentAddress(f.getAbsolutePath());


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No parent directory exists", "Error", 2);
        } finally {
            upgradeFiles();
        }

    }

    /**
     * Given that it is now time to Sync, this method sees what files must be coppied to the nest folder. That is, the files which have been added
     * and have not ever been deleted (with the same name)
     */
    public void logChanges() {
        File F = new File(syncPath);
        addedFileNames = new ArrayList<>();

        File[] files = F.listFiles();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                boolean toBeAdded = true;

                if (loggedFileNames != null)
                    for (int j = 0; j < loggedFileNames.size(); j++)
                        if (loggedFileNames.get(j).equals(files[i].getName())) {
                            toBeAdded = false;
                            break;
                        }


                if (deletedFileNames != null)
                    for (int j = 0; j < deletedFileNames.size(); j++)
                        if (deletedFileNames.get(j).equals(files[i].getName())) {
                            toBeAdded = false;
                            break;
                        }

                if (toBeAdded)
                    addedFileNames.add(files[i].getAbsolutePath());
            }

        }


    }

    /**
     * A File to hold all files which must be broadcasted to other pc
     */
    private void generateNestFile() {
        nestFile = new File(nestPath);

        if (nestFile.exists())
            deleteFile(nestFile);

        nestFile = new File(nestPath);
        nestFile.mkdirs();


        File dummy = new File(syncPath);
//        File[] files = dummy.listFiles();
//
//        if (files != null) {
//            for (int i = 0; i < files.length; i++) {
//                boolean coppy = true;
//
//                if (addedFileNames != null)
//                    for (int j = 0; j < addedFileNames.size(); j++)
//                        if (files[i].getName().equals(addedFileNames.get(j))) {
//                            coppy = false;
//                            break;
//                        }
//
//                if (loggedFileNames != null)
//                    for (int j = 0; j < loggedFileNames.size(); j++)
//                        if (files[i].getName().equals(loggedFileNames.get(j))) {
//                            coppy = false;
//                            break;
//                        }
//
//                if (coppy) {
//                    try {
//                        pasteFile(files[i].getAbsolutePath(), nestPath + "\\" + files[i].getName());
//                        System.out.println("from : " + files[i].getAbsolutePath() + " to : " + nestPath + "\\" + files[i].getName());
//                    } catch (Exception e) {
//                        JOptionPane.showInputDialog(null, "Unable to move files from sync path to nest Folder");
//                    }
//                }
//
//
//            }
//        }

        if (addedFileNames != null) {
            for (int i = 0; i < addedFileNames.size(); i++) {
                try {
                    pasteFile(addedFileNames.get(i), nestPath + "\\" + new File(addedFileNames.get(i)).getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }


    }

    /**
     * Initialize watching on a given file, setting deleted array to null and recording file names
     *
     * @param F
     */
    public void initializeWatching(File F) {
        File[] files = F.listFiles();
        deletedFileNames = new ArrayList<>();
        loggedFileNames = new ArrayList<>();
        addedFileNames = new ArrayList<>();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                loggedFileNames.add(files[i].getName());


    }

    private void preInit(File directory) {
        File[] F = directory.listFiles();

        if (F != null) {
            if (F.length == 0) {
                //      JOptionPane.showMessageDialog(null,directory.getAbsolutePath());
                File divert = new File(directory.getAbsolutePath() + "\\" + "divert");
                try {
                    divert.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < F.length; i++)
                    if (F[i].isDirectory())
                        preInit(F[i]);
            }

        }
    }

    /**
     * A Method to Synchronize
     */
    public void sync() {
        if (syncPath == null || !new File(syncPath).isDirectory()) {
            JOptionPane.showMessageDialog(null, "Error, Specify a valid directory as a Sync path", "Error", 2);
            return;
        }

        logChanges();

        File F = new File(syncPath);

        ///////////////////////////////////////////////////////////
        int port = 22000;


        while (true) {
            try {
                port = Integer.parseInt(remoteComputerPort);
                break;

            } catch (Exception e) {
                remoteComputerPort = JOptionPane.showInputDialog("Error, Inavlid Port. Enter an Integer.");
            }

        }

        while (true) {
            if (new File(receivedFileAddress).isDirectory())
                break;

            else
                receivedFileAddress = JOptionPane.showInputDialog("Error, directory to save files. Enter a valid directory.");


        }

        JOptionPane.showMessageDialog(null, "added length : " + addedFileNames.size());
        JOptionPane.showMessageDialog(null, "dels : " + deletedFileNames.size());
        generateNestFile();
        preInit(nestFile);
        File syncFile = new File(syncPath);
        receivedFileAddress = syncPath;


        new TransferHandler(remoteComputerAddress, port, nestFile, syncFile, tag).start();


    }

    class TransferHandler extends Thread {
        private String host;
        private int port;
        private File synsFile, writtenIn;
        private SenderClient senderClient;
        private ReceiverClient receiverClient;
        private String identity, syncPathLock;
        private File[] syncPathFileLock;
        private ArrayList<String> deletedFileLock;
        ;

        public TransferHandler(String host, int port, File syncFile, File writtenIn, String identity) {
            this.host = host;
            this.port = port;
            this.synsFile = syncFile;
            this.writtenIn = writtenIn;
            this.identity = identity;
            syncPathLock = syncPath;
            deletedFileLock = deletedFileNames;
            syncPathFileLock = new File(syncPathLock).listFiles();

        }

        private void removeFilesDeletedByThisOneFromSyncPath() {
            File[] F = new File(syncPathLock).listFiles();

            if (F != null && deletedFileLock != null)
                for (int i = 0; i < F.length; i++)
                    for (int j = 0; j < deletedFileLock.size(); j++)
                        if (F[i].getName().equals(deletedFileLock.get(j))) {
                            deleteFile(F[i]);
                            break;
                        }

        }

        @Override
        public void run() {

            removeFilesDeletedByThisOneFromSyncPath();
            senderClient = new SenderClient(host, port, synsFile, deletedFileNames);
            senderClient.start();


            try {
                senderClient.join();

                if (identity.equals("none"))
                    tag = senderClient.getIdentity();

                receiverClient = new ReceiverClient(writtenIn, host, port, tag);
                receiverClient.start();
                receiverClient.join();
                postinit(new File(syncPathLock));
                //    trim();
                //    inflict(receiverClient.getDeletedFilesNames());
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        private void inflict(ArrayList<String> revise) {

            if (revise != null && syncPathFileLock != null)
                for (int i = 0; i < syncPathFileLock.length; i++)
                    for (int j = 0; j < revise.size(); j++)
                        if (syncPathFileLock[i].getName().equals(revise.get(j))) {
                            deleteFile(syncPathFileLock[i]);
                            break;
                        }


        }


        private void postinit(File directory) {
            File[] F = directory.listFiles();

            if (F != null)
                for (int i = 0; i < F.length; i++) {
                    if (F[i].isFile())
                        if (F[i].getName().equals("divert"))
                            deleteFile(F[i]);

                    if (F[i].isDirectory())
                        postinit(F[i]);
                }
        }


    }
}





