/**
 * A Class to handle sending files and  deletes operations
 */

package Client;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;

public class SenderClient extends Thread {

    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private ArrayList<String> addedFiles, dirs;
    private ArrayList<String> deletedFiles;
    private String identity;
    private PrintWriter bw;
    private BufferedReader br;
    private DataOutputStream dos;
    private File base;


    /**
     * Constructor for this class
     *
     * @param host
     * @param port
     * @param theBase
     * @param deletedFiles
     * @param tag
     */
    public SenderClient(String host, int port, File theBase, ArrayList<String> deletedFiles, String tag) {
        try {
            this.deletedFiles = deletedFiles;
            addedFiles = new ArrayList<>();
            socket = new Socket(host, port);
            output = socket.getOutputStream();
            input = socket.getInputStream();
            bw = new PrintWriter(new OutputStreamWriter(output));
            br = new BufferedReader(new InputStreamReader(input));
            dos = new DataOutputStream(output);
            dirs = new ArrayList<>();
            base = theBase;
            identity = tag;


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run method
     */
    @Override
    public void run() {
        identify();
        broadcastDeletedFiles();

        try {
            sendAllFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A Method to send deleted file names from current pc
     */
    private void broadcastDeletedFiles() {
        if (deletedFiles != null)
            for (int i = 0; i < deletedFiles.size(); i++) {
                bw.println(deletedFiles.get(i));
                bw.flush();
            }

        bw.println(":END");
        bw.flush();

    }

    /**
     * A method to identify the turn of this pc which remains constant
     */
    private void identify() {
        try {
            bw.println(identity);
            bw.flush();

            String turn = br.readLine();

            if (turn.equals("first"))
                identity = "first";

            if (turn.equals("second"))
                identity = "second";


            System.out.println(identity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A Method to send all newly added files from this pc
     *
     * @throws Exception
     */
    private void sendAllFiles() throws Exception {
        BufferedOutputStream bos = new BufferedOutputStream(output);
        DataOutputStream dos = new DataOutputStream(bos);
        File[] files = new File[addedFiles.size()];

        for (int i = 0; i < addedFiles.size(); i++)
            files[i] = new File(addedFiles.get(i));

        dos.writeInt(files.length);

        int k = -1;

        for (File file : files) {
            k++;
            long length = file.length();
            dos.writeLong(length);

            String name = file.getName();
            dos.writeUTF(name);
            dos.writeUTF(dirs.get(k));

            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            while ((theByte = bis.read()) != -1) bos.write(theByte);

            bis.close();
        }

        dos.close();
        socket.close();
    }

    /**
     * Getter for identity
     *
     * @return identity
     */
    public String getIdentity() {
        return identity;
    }

}

