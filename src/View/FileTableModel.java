/**
 * A Class for building table, naming it's columns and assigning values to it's rows. Also assigning icons to the first column
 */

package View;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.io.File;
import java.util.Date;


public class FileTableModel extends AbstractTableModel  {


    private File[] files;

    private String[] columnNames = new String[]{"icon", "name", "size(KB)", "last modified", "type"};

    private Class[] columnClassses = new Class[]{
            String.class, Long.class, Date.class, Icon.class
    };

    public int getColumnCount() {
        return 5;
    }


    public int getRowCount() {
        return files.length;
    }


    /**
     * Cunstructor for this class
     * @param files
     */
    public FileTableModel(File [] files) {
        this.files=files;
    }

    /**
     * Overriden method for getColumnsName
     * @param col
     * @return columnNames[col]
     */
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
     * Overriden method for getValue, to assing values to cells of the table
     * @param row
     * @param col
     * @return entries in cells
     */

    @Override
    public Object getValueAt(int row, int col) {
        File f = files[row];
        //    {"icon","name","size","last modified","type"};
        switch (col) {
            case 0:
                return FileSystemView.getFileSystemView().getSystemIcon(f);
            //   return new ImageIcon("C:\\Users\\erfan\\Desktop\\WindowsExplorer\\images\\fileIcon.png");
            case 1:
                return f.getName();

            case 2:
                return f.length();
            //return calculateSize(f);


            case 4:
                if (f.isFile())
                    return "File";

                return "Folder";

            case 3:
                return new Date(f.lastModified());

            default:
                return null;
        }
    }

    /**
     * Overriden method for get columns classes
     * @param column
     * @return column class
     */
    @Override
    public    Class getColumnClass(int column) {
        return (column == 0) ? Icon.class : Object.class;
    }


}

