package Model;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Model {

    private File[] allFiles;
    private String currentAddress = "C:\\Users\\erfan\\Desktop";
    private String syncPath;
    private boolean isGridDisplay;


    public void setAllFiles(File[] allFiles) {
        this.allFiles = allFiles;
    }

    public File[] getAllFiles() {
        upgradeFiles();
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

    public void deleteFile(File f) {

        if (!f.delete())
            JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 3);

    }

    public static void pasteFile(String from, String to) throws IOException{
        Path src = Paths.get(from);
        Path dest = Paths.get(to);
        Files.copy(src, dest, StandardCopyOption.COPY_ATTRIBUTES);
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
        }

        finally {
            upgradeFiles();
        }


    }


}
