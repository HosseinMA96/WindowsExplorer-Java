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


    File dir;
    protected File[] files;

    protected String[] columnNames = new String[]{"icon", "name", "size(KB)", "last modified", "type"};

    protected Class[] columnClassses = new Class[]{
            String.class, Long.class, Date.class, Icon.class
    };

    public int getColumnCount() {
        return 5;
    }


    public int getRowCount() {
        return files.length;
    }


    public FileTableModel(File dir,int order,int feature) {
        this.dir = dir;
        this.files = dir.listFiles();

        if(feature<4 && feature!=0 )
        sort(order,feature);
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnsClass(int col) {
        return columnClassses[col];
    }


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

//    class mcr extends DefaultTableCellRenderer {
//        public Class getColumnClass(int c) {
//            return ImageIcon.class;
//        }
//    }

    public    Class getColumnClass(int column) {
        return (column == 0) ? Icon.class : Object.class;
    }

//    File f = new File(dir, filenames[row]);

    private void sort(int order,int feature) {

        int index = 0;

        for (int i = 0; i < files.length; i++) {
            for (int j = 1; j < files.length - i ; j++) {
                if (compare(files[j-1], files[j], feature)) {
                    String S=files[j-1].getAbsolutePath();

                    files[j-1]=new File(files[j].getAbsolutePath());
                    files[j]=new File(S);
                }

            }
        }

        if(order==0)
        {

            for(int i=0; i<files.length/2; i++){
                String temp = files[i].getAbsolutePath();
                files[i]=new File(files[files.length -i -1].getAbsolutePath());
                files[files.length -i -1]=new File(temp);
            }
        }
    }

    private boolean compare(File A, File B, int feature) {
        /*
        status=1:sort By name
        status=2:sort By size
        status=3:sort By Date
        status=column number
         */



        switch (feature) {
            case 1:
                //if string a is les than b, a.copmareTo(b)is negative
                String a = A.getName(), b = B.getName();


                return (a.compareTo(b) > 0);


            case 2:
                //For date, it is same. if A occurs before B A.compareTo(B) is <0
                Date aa = new Date(A.lastModified()), bb = new Date(B.lastModified());
                return (aa.compareTo(bb) > 0);


            case 3:
                if(A.isDirectory())
                    return true;

                if(B.isDirectory())
                    return false;

                return (A.length() < B.length());

        }
        return false;

    }


}

