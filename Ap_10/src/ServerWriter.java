/**
 * A Method to send merged files and delete strings to clients over socket
 */

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerWriter extends Thread {
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    private BufferedReader br;
    private PrintWriter bp;
    private String turn;
    private DataOutputStream dos;
    private ArrayList<String> addedFiles, dirs;
    private ArrayList<String> toBeDeletedInFirst, deletedOftTheFirst,inflictedDelete;
    private File base;

    /**
     * Constructor of this class
     * @param socket
     * @param file
     * @param toBeDeletedInFirst
     * @param deletedOftTheFirst
     */
    public ServerWriter(Socket socket, File file, ArrayList<String> toBeDeletedInFirst, ArrayList<String> deletedOftTheFirst) {
        try {
            this.socket = socket;
            input = socket.getInputStream();
            output = socket.getOutputStream();
            dos = new DataOutputStream(output);

            addedFiles = new ArrayList<>();
            File[] dummy = file.listFiles();
            dirs = new ArrayList<>();
            base = file;
            this.toBeDeletedInFirst = toBeDeletedInFirst;
            this.deletedOftTheFirst = deletedOftTheFirst;


            bp = new PrintWriter(new OutputStreamWriter(output));
            br = new BufferedReader(new InputStreamReader(input));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Run method of this class
     */
    @Override
    public void run() {

        try {
            identify();
            broadcastPrepreation(base, "");
            broadcastDeletedString();
            sendAllDirFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * A Method to send merged deleted orders to the client
     */
    private void broadcastDeletedString()
    {
        if(inflictedDelete==null)
            inflictedDelete=new ArrayList<>();

        for (int i=0;i<inflictedDelete.size();i++)
        {
            bp.println(inflictedDelete.get(i));
            bp.flush();
        }

        bp.println(":END");
        bp.flush();
    }

    /**
     * A Method to detect the client's turn
     * @throws Exception
     */
    private void identify() throws Exception{
        turn = br.readLine();

        if(turn.equals("first"))
            inflictedDelete=toBeDeletedInFirst;

        else
            inflictedDelete=deletedOftTheFirst;
    }

    /**
     * A Method to send all files from the merged directory to the clients
     * @throws Exception
     */
    private void sendAllDirFiles() throws Exception {


        File[] files = new File[addedFiles.size()];
        for (int i = 0; i < addedFiles.size(); i++)
            files[i] = new File(addedFiles.get(i));



        BufferedOutputStream bos = new BufferedOutputStream(output);
        DataOutputStream dos = new DataOutputStream(bos);

        dos.writeInt(files.length);
        int k = -1;

        for (File file : files) {
            k++;
            long length = file.length();
            dos.writeLong(length);

            String name = file.getName();
            dos.writeUTF(name);
            String path = dirs.get(k);
            dos.writeUTF(path);


            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            int theByte = 0;
            while ((theByte = bis.read()) != -1) bos.write(theByte);

            bis.close();
        }

        dos.close();

    }

    /**
     * A Method to prepare files from the merged directory to be sent
     * @param directory
     * @param aux
     */
    private void broadcastPrepreation(File directory, String aux) {
        File[] F = directory.listFiles();

        if (F != null)
            for (int i = 0; i < F.length; i++) {
                if (F[i].isFile()) {
                    addedFiles.add(F[i].getAbsolutePath());
                    dirs.add(aux);
                } else {
                    String temp = F[i].getName();
                    temp = aux + "\\" + temp;

                    broadcastPrepreation(F[i], temp);
                }
            }

    }
}
