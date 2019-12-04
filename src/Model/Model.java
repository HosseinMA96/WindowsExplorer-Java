package Model;

import javax.swing.*;
import java.io.*;


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

        File oldFile = new File(prevName);
        File newFile = new File(newName);

        oldFile.renameTo(newFile);
    }

    public void deleteFile(File f) {

        if (!f.delete())
            JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 3);

    }

    public void copyFile(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
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
        if(!F.mkdir())
           JOptionPane.showMessageDialog(null,"Unable to create Folder","Eror",3);
     //   } catch (Exception e) {
      //      JOptionPane.showMessageDialog(null, "Unable to create File");
      //  }
    }
}
