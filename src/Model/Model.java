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

        if(f.isFile())
        {
            try
            {
                if(!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            }
            catch (Exception ex)
            {

            }
        }

        if(f.isDirectory())
        {
            File [] temp=f.listFiles();

            for (int i=0;i<temp.length;i++)
                deleteFile(temp[i]);

            try
            {
                if(!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            }
            catch (Exception ex)
            {

            }
        }


    }

    public static void pasteFile(String from, String to) throws IOException {
        Path src = Paths.get(from);
        Path dest = Paths.get(to);

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
