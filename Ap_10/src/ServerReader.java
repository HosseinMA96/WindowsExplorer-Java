/**
 * A Class to read and save files from socket
 */

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerReader extends Thread {

    private ServerSocket serverSocket, ss;
    private int numberOfConnection = 0;
    private File base;
    Socket socket;
    private ArrayList<String> addedFiles = new ArrayList<>();
    private ArrayList<String> deletedFilenames = new ArrayList<>(), dirs = new ArrayList<>();
    private InputStream input;
    private OutputStream output;
    private boolean isFirst;
    private BufferedReader br;
    private PrintWriter bp;
    private String turn;
    private DataInputStream dis;


    /**
     * Constructor for this class
     * @param socket
     * @param first
     * @param location
     */
    public ServerReader(Socket socket, boolean first, File location) {
        try {

            this.socket = socket;
            isFirst = first;
            base = location;

            input = socket.getInputStream();
            output = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(input));
            bp = new PrintWriter(new OutputStreamWriter(output));
            dis = new DataInputStream(input);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run method
     */
    @Override
    public void run() {

        try {
            initialize();
            identify();
            receiveDeletetFileNames();
            saveFiles();
        } catch (Exception e) {

        }

    }

    /**
     * A Method to receive deleted files names
     * @throws Exception
     */
    private void receiveDeletetFileNames() throws Exception {

        deletedFilenames = new ArrayList<>();

        while (true) {

            String delName = br.readLine();

            if (delName.equals(":END"))
                break;

            deletedFilenames.add(delName);
        }
    }

    /**
     * A Method to detect the turn of the connected pc
     * @throws Exception
     */
    private void identify() throws Exception {
        turn = br.readLine();


        if (turn.equals("none")) {
            if (isFirst) {
                bp.println("first");
                turn = "first";
            } else {
                bp.println("second");
                turn = "second";
            }

            bp.flush();

        } else {
            bp.println("unImportantMessage");
            bp.flush();
        }

    }

    /**
     * Initialize where the deleted fiels are saved
     * @throws Exception
     */
    private void initialize() throws Exception {

        File del = new File("E:\\DELS");
        if (del.exists())
            MultiServer.deleteFile(del);

        del.mkdir();
    }


    /**
     * A Method to save files read from the socket
     * @throws Exception
     */
    private void saveFiles() throws Exception {
        boolean withdraw = false;

        BufferedInputStream bis = new BufferedInputStream(input);
        DataInputStream dis = new DataInputStream(bis);


            int filesCount = dis.readInt();




        if (filesCount <1) {
            filesCount = 10;
            withdraw = true;

        }

        File[] files = new File[filesCount];

        String dummy = base.getAbsolutePath();

        if (turn.equals("first")) {
            base = new File("E:\\temp1");

            if (base.exists())
                MultiServer.deleteFile(base);

            base = new File("E:\\temp1");

            base.mkdirs();
        } else {
            base = new File("E:\\temp2");

            if (base.exists())
                MultiServer.deleteFile(base);

            base = new File("E:\\temp2");
            base.mkdirs();
        }

        if (withdraw) {
            dis.close();
            return;
        }

        for (int i = 0; i < filesCount; i++) {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            String path = dis.readUTF();



            String temp = base.getAbsolutePath() + path;
            File tempFile = new File(temp);

            if (!tempFile.exists())
                tempFile.mkdirs();

            if (path != null && path.length() != 0)
                path = "\\" + path;


            files[i] = new File(base.getAbsolutePath() + path + "\\" + fileName);



            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
        }
        dis.close();
    }

    /**
     * Getter for deletedFilenames
     * @return deletedFilenames
     */
    public ArrayList<String> getDeletedFilenames() {
        return this.deletedFilenames;
    }

    /**
     * Getter for turn
     * @return turn
     */
    public String getTurn() {
        return turn;
    }
}