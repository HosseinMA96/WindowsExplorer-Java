/**
 * A class to receive files and delete orders from server
 */

package Client;


import View.MyProgressBar;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ReceiverClient extends Thread {
    private File base;
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private PrintWriter bw;
    private BufferedReader br;
    private String tag;
    private DataInputStream dis;
    private boolean withdraw = false;
    private ArrayList<String> deletedFilesNames = new ArrayList<>();
    private ArrayList<String> onceDeleted = new ArrayList<>();
    MyProgressBar myProgressBar;

    /**
     * Constructor for this class
     *
     * @param saveAddress
     * @param host
     * @param port
     * @param tag
     * @throws Exception
     */
    public ReceiverClient(File saveAddress, String host, int port, String tag) throws Exception {
        base = saveAddress;
        socket = new Socket(host, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        bw = new PrintWriter(new OutputStreamWriter(output));
        br = new BufferedReader(new InputStreamReader(input));
        dis = new DataInputStream(input);
        this.tag = tag;

    }

    /**
     * A Method to initialize saving route
     */
    private void initialize() {

        File F;

        F = new File(base.getAbsolutePath());

        if (!F.exists()) {
            JOptionPane.showMessageDialog(null, "Error, Sync file no longer exists, some information loss might happen");
            F = new File(base.getAbsolutePath());
            F.mkdir();
        }

    }

    /**
     * A Method to delete a File without recording the action
     *
     * @param f
     */
    public static void deleteFile(File f) {

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
     * The run method
     */
    @Override
    public void run() {
        initialize();
        identify();

        try {
            receiveDeletedFilesNames();
            //   manageDeletes();
            receiveFiles();
            manageDeletes();
            //  trim(base);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method prepares the new files to be loaded into Sync path. first of all, files whom their names is in deleted files must be deleted
     * Second, for any new file, if there already exists a file with the same name in sync path, we must first remove that file in the
     * Sync pass (overwrite it) so that two machines remain Synchronized
     */
    private void manageDeletes() {
        File[] F = base.listFiles();


        for (int i = 0; i < deletedFilesNames.size(); i++) {
            if (F != null)
                for (int j = 0; j < F.length; j++)
                    if (F[j] != null & F[j].exists() && F[j].getName().equals(deletedFilesNames.get(i))) {
                        deleteFile(F[j]);
                        break;
                    }

            myProgressBar.oneDelete();
        }

    }

    /**
     * This method receives deleted files names
     *
     * @throws Exception
     */
    private void receiveDeletedFilesNames() throws Exception {
        String dummy;
        deletedFilesNames = new ArrayList<>();

        while (true) {
            dummy = br.readLine();

            if (dummy.equals(":END"))
                break;

            deletedFilesNames.add(dummy);
        }


    }

    /**
     * This method declares the turn of this pc which is constant
     */
    private void identify() {
        bw.println(tag);
        bw.flush();
    }

    /**
     * A Method to receive files from server
     *
     * @throws Exception
     */
    private void receiveFiles() throws Exception {

        BufferedInputStream bis = new BufferedInputStream(input);
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        myProgressBar = new MyProgressBar(0, deletedFilesNames.size(), filesCount - 1);

        for (int i = 0; i < filesCount; i++) {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            ////////////////////////////////////
            String path = dis.readUTF();
            String temp = base.getAbsolutePath() + path;
            File tempFile = new File(temp);

            if (!tempFile.exists())
                tempFile.mkdirs();


            if (path != null && path.length() != 0)
                path = "\\" + path;

            files[i] = new File(base.getAbsolutePath() + path + "\\" + fileName);
            checkOverWrite(files[i]);

            if (!tempFile.exists())
                tempFile.mkdirs();


            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
            myProgressBar.oneAdd();
        }

        dis.close();

    }

    /**
     * A Method to check and delete occurrence of these file name in the destination folder
     *
     * @param toBeAdded
     */
    void checkOverWrite(File toBeAdded) {

        if (toBeAdded.getParentFile().equals(base)) {
            File[] files = base.listFiles();

            if (files != null)
                for (int i = 0; i < files.length; i++)
                    if (files[i].getName().equals(toBeAdded.getName())) {
                        deleteFile(files[i]);
                        return;
                    }

        }
        File temp = toBeAdded;

        while (!temp.getParentFile().equals(base)) {
            temp = temp.getParentFile();
        }


        File[] files = base.listFiles();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                if (files[i].getName().equals(temp.getName())) {

                    for (int j = 0; j < onceDeleted.size(); j++)
                        if (onceDeleted.get(j).equals(files[i].getName()))
                            return;

                    onceDeleted.add(files[i].getName());
                    deleteFile(files[i]);
                    break;
                }

    }

}


