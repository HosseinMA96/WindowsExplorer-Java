package Model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Model {

    private File[] allFiles;
    private String currentAddress = "C:\\Users\\erfan\\Desktop";
    private String syncPath;
    private boolean isGridDisplay;
    private String initialAddress, receivedFileAddress, remoteComputerAddress, remoteComputerPort, lookAndFeel, displayFormat, syncInterval, flashBackNumber;


    public void setAllFiles(File[] allFiles) {
        this.allFiles = allFiles;
    }

    public File[] getAllFiles() {
        return allFiles;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public void upgradeFiles() {
        try {

            File f = new File(currentAddress);
            //    System.out.println(files.exists());

            if (f.isDirectory()) {
                allFiles = f.listFiles();

            } else
                throw new Exception("Address is not a directory");
//
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Current address is not a directory.\nCurrent address was set to be desktop.");
            currentAddress = "C:\\Users\\erfan\\Desktop";
            upgradeFiles();
        }

//
    }


    public void renameFile(String prevName, String newName) {

        String dummy;
        File oldFile = new File(prevName);

        dummy = oldFile.getParentFile().getAbsolutePath() + "\\" + newName;
        File newFile = new File(dummy);

        if (!oldFile.renameTo(newFile))
            JOptionPane.showMessageDialog(null, "Renaming failed. Either file is secured or current name already exists or new name is invalid.", "Eror", 3);

    }

    //IDK why but deletion is not as robust as it should be ... it sometimes "misses" ....
    public void deleteFile(File f) {
        // JOptionPane.showMessageDialog(null,f.getAbsolutePath());
//        try {
//            if (!f.delete())
//                JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

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

    public void loadSettings() {
        int i = 0;
        try {
            Scanner scan = new Scanner(new File("JFileManager_Settings.txt"));
            while (scan.hasNextLine() && i < 8) {
                String line = scan.nextLine();

                switch (i) {
                    case 0:
                        initialAddress = new String(line);
                        break;

                    case 1:
                        receivedFileAddress = new String(line);
                        break;

                    case 2:
                        remoteComputerAddress = new String(line);
                        break;

                    case 3:
                        remoteComputerPort = new String(line);
                        break;

                    case 4:
                        lookAndFeel = new String(line);
                        break;

                    case 5:
                        isGridDisplay = Boolean.parseBoolean(line);
                        break;

                    case 6:
                        syncInterval = new String(line);
                        break;


                    case 7:
                        flashBackNumber = new String(line);
                        break;


                }
                i++;
                //Here you can manipulate the string the way you want
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to save settings", "Eror", 0);
        }
    }


    public void writeSettingsToFile() {
        try {

            File f = new File("JFileManager_Settings.txt");

            if (new File(f.getAbsolutePath()).exists())
                deleteFile(new File(f.getAbsolutePath()));

            FileWriter fw = new FileWriter("JFileManager_Settings.txt");


            fw.write(initialAddress + "\n");
            fw.write(receivedFileAddress + "\n");
            fw.write(remoteComputerAddress + "\n");
            fw.write(remoteComputerPort + "\n");
            fw.write(lookAndFeel + "\n");
            fw.write(isGridDisplay + "\n");
            fw.write(syncInterval + "\n");
            fw.write(flashBackNumber + "\n");

            fw.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Unable to save settings.", "Eror", 0);
        }


    }

    public void setSyncPath(String syncPath) {
        this.syncPath = syncPath;
    }

    public void setInitialAddress(String initialAddress) {
        this.initialAddress = initialAddress;
    }

    public void setReceivedFileAddress(String receivedFileAddress) {
        this.receivedFileAddress = receivedFileAddress;
    }

    public void setRemoteComputerAddress(String remoteComputerAddress) {
        this.remoteComputerAddress = remoteComputerAddress;
    }

    public void setRemoteComputerPort(String remoteComputerPort) {
        this.remoteComputerPort = remoteComputerPort;
    }

    public void setLookAndFeel(String lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }

    public void setDisplayFormat(String displayFormat) {
        this.displayFormat = displayFormat;
    }

    public void setSyncInterval(String syncInterval) {
        this.syncInterval = syncInterval;
    }

    public void setFlashBackNumber(String flashBackNumber) {
        this.flashBackNumber = flashBackNumber;
    }

    public String getSyncPath() {
        return syncPath;
    }

    public boolean isGridDisplay() {
        return isGridDisplay;
    }

    public String getInitialAddress() {
        return initialAddress;
    }

    public String getReceivedFileAddress() {
        return receivedFileAddress;
    }

    public String getRemoteComputerAddress() {
        return remoteComputerAddress;
    }

    public String getRemoteComputerPort() {
        return remoteComputerPort;
    }

    public String getLookAndFeel() {
        return lookAndFeel;
    }

    public String getDisplayFormat() {
        return displayFormat;
    }

    public String getSyncInterval() {
        return syncInterval;
    }

    public String getFlashBackNumber() {
        return flashBackNumber;
    }

    public static void pasteFile(String from, String to) throws IOException {
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
     //   JOptionPane.showMessageDialog(null,to);

        File sourceFolder = new File(from);


        File destinationFolder = new File(to);

        if (sourceFolder.isDirectory()) {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
                //  System.out.println("Directory created :: " + destinationFolder);
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
            //Copy the file content from one place to another
       //     JOptionPane.showMessageDialog(null,destinationFolder.getName());
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //    System.out.println("File copied :: " + destinationFolder);
        }
        //      Files.copy(src, dest, StandardCopyOption.COPY_ATTRIBUTES);

    }

    public void cutFile(String sourcePath, String destPath) {
        File source = new File(sourcePath);
        File destination = new File(destPath);

        if (!destination.exists()) {
            source.renameTo(destination);
        }
    }

    public void setAsSynchPath() {
        syncPath = currentAddress;
    }


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

    public void setGridDisplay(boolean gridDisplay) {
        isGridDisplay = gridDisplay;
    }

    public boolean getGridDisplay() {
        return isGridDisplay;
    }

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
        //   } catch (Exception e) {
        //      JOptionPane.showMessageDialog(null, "Unable to create File");
        //  }
    }

    public void goToParent() {
        File f = new File(currentAddress);

        try {

            f = f.getParentFile();
            currentAddress = f.getAbsolutePath();


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No parent directory exists", "Error", 2);
        } finally {
            upgradeFiles();
        }

    }


}
