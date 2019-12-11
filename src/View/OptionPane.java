
/**
 * A Class for displaying properties for files and folders
 */
package View;

import javax.swing.*;
import java.util.Date;

public class OptionPane
{
    boolean isFile=false;

    /**
     * Cunstructor for this class
     * @param type
     * @param address
     * @param size
     * @param created
     * @param filesContained
     * @param foldersContained
     */
    public OptionPane(String type, String address, long size, Date created, int filesContained, int foldersContained)
    {
        isFile=(type.equals("File") || type.equals("file"));

        if(isFile)
            JOptionPane.showMessageDialog(null,"Type:\t\t"+type+"\nLocation:\t\t"+address+"\n"+"Size:\t\t"+size/1024+" KB\n"+"Created:\t\t"+created+"\n");

        else
            JOptionPane.showMessageDialog(null,"Type:\t\t"+type+"\nLocation:\t\t"+address+"\n"+"Size:\t\t"+size+" bytes\n"+"Created:\t\t"+created+"\nContained:\t\t"+"files : "+filesContained+" folders : "+foldersContained);

    }
}
