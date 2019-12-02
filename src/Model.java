import javax.swing.*;
import java.io.File;

public class Model
{

    private  File[] allFiles;
    private  String currentAddress = "C:\\Users\\erfan\\Desktop";

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
        try
        {
            File f = new File(currentAddress);
            //    System.out.println(files.exists());

            if(f.isDirectory())
                allFiles = f.listFiles();

            else
                throw new Exception("Address is not a directory");
//
        }

        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,"Current address is not a directory.\nCurrent address was set to be desktop.");
            currentAddress="C:\\Users\\erfan\\Desktop";
            upgradeFiles();
        }

//
    }
}
