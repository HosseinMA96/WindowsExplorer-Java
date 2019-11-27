import javax.swing.*;

/**
 * A Class for displaying
 */
public class OptionPane
{
    boolean isFile=false;

    public OptionPane(String type,String address,int size,String created,int filesContained,int foldersContained)
    {
        isFile=(type.equals("File") || type.equals("file"));

        if(isFile)
            JOptionPane.showMessageDialog(null,"Type:\t\t"+type+"\nLocation:\t\t"+address+"\n"+"Size:\t\t"+size+" bytes\n"+"Created:\t\t"+created+"\n");

        else
            JOptionPane.showMessageDialog(null,"Type:\t\t"+type+"\nLocation:\t\t"+address+"\n"+"Size:\t\t"+size+" bytes\n"+"Created:\t\t"+created+"\nContained:\t\t"+filesContained+", files "+foldersContained+", folders");

    }
}
