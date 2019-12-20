package Client;


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

    /////////////////////////


    private void initialize() {

        File F;

        F = new File(base.getAbsolutePath());

        if (!F.exists()) {
            JOptionPane.showMessageDialog(null, "Error, Sync file no longer exists, some information loss might happen");
            F = new File(base.getAbsolutePath());
            F.mkdir();
        }


        //     System.out.println("make " + base.mkdir());
    }

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

    ////////////////////

    @Override
    public void run() {
        initialize();
        identify();

        try {
            receiveDeletedFilesNames();
            manageDeletes();
            receiveFiles();
            //  trim(base);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   receiveDeleteArray();
        // System.out.println("in Receiver finished");

    }

    /**
     * This method prepares the new files to be loaded into Sync path. first of all, files whom their names is in deleted files must be deleted
     * Second, for any new file, if there already exists a file with the same name in sync path, we must first remove that file in the
     * Sync pass (overwrite it) so that two machines remain Synchronized
     */
    private void manageDeletes() {
        File[] F = base.listFiles();

        if (F != null && deletedFilesNames != null) {
            for (int i = 0; i < F.length; i++)
                for (int j = 0; j < deletedFilesNames.size(); j++)
                    if (F[i].getName().equals(deletedFilesNames.get(j))) {
                        deleteFile(F[i]);
                        break;
                    }
        }

    }

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

    private void identify() {
        System.out.println("in receiver client, tag is : " + tag);
        bw.println(tag);
        bw.flush();
    }

    private void receiveFiles() throws Exception {

        BufferedInputStream bis = new BufferedInputStream(input);
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

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
            System.out.println("bingo : " + base.getAbsolutePath() + "\t" + path + "\\" + "\t" + fileName);
            System.out.println(base.getAbsolutePath());
            System.out.println(path);
            System.out.println(fileName);
            System.out.println("totally together : " + files[i].getAbsolutePath());
            //Handle Overwrite


            checkOverWrite(files[i]);

            if (!tempFile.exists())
                tempFile.mkdirs();
            ///////////////////////////////////

            //    files[i] = new File(savingDir.getAbsolutePath() + "\\" + fileName);

            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
        }

        dis.close();

    }

    public ArrayList<String> getDeletedFilesNames() {
        return deletedFilesNames;
    }


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
        System.out.println("to be added getparent file: " + temp.getParentFile());
        System.out.println("to be added getparnt String: " + temp.getParent());

        //    File[] F = base.listFiles();
        while (!temp.getParentFile().equals(base)) {
            System.out.println("Temp is " + temp.getAbsolutePath());
            temp = temp.getParentFile();
        }


        System.out.println("after " + temp.getAbsolutePath());

        File[] files = base.listFiles();

        if (files != null)
            for (int i = 0; i < files.length; i++)
                if (files[i].getName().equals(temp.getName())) {
                    boolean shallDelete = true;

                    for (int j = 0; j < onceDeleted.size(); j++)
                        if (onceDeleted.get(j).equals(files[i].getName()))
                            return;

                    onceDeleted.add(files[i].getName());
                    deleteFile(files[i]);
                    break;
                }

    }


    void trim(File currentFile) {
        File F[] = currentFile.listFiles();

        if (F != null) {
            for (int i = 0; i < F.length; i++) {
                if (F[i].isFile() && (F[i].getName().equals("divert.txt") || F[i].getName().equals("divert"))) {
                    deleteFile(F[i]);

                }

                if (F[i].isDirectory())
                    trim(F[i]);
            }


        }
    }
}


