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

        //    bp.println("END:FILE:SEND");
        //      bp.flush();
        //  sendDeleteString();

    }

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

    private void identify() throws Exception{
        turn = br.readLine();
        System.out.println("In serverWriter, identity: "+turn);

        if(turn.equals("first"))
            inflictedDelete=toBeDeletedInFirst;

        else
            inflictedDelete=deletedOftTheFirst;
    }

    private void sendAllDirFiles() throws Exception {

        //  String directory = ...;
        //    String hostDomain = ...;
        //   int port = ...;

        File[] files = new File[addedFiles.size()];
        for (int i = 0; i < addedFiles.size(); i++)
            files[i] = new File(addedFiles.get(i));


        // Socket socket = new Socket(InetAddress.getByName(hostDomain), port);

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

//
//    private void sendFile(File F, String pathToThisFile) throws Exception {
//        //   DataOutputStream dos = new DataOutputStream(output);
//        FileInputStream fis = new FileInputStream(F);
//        bp.println(pathToThisFile);
//        bp.flush();
//
////            for (int i = 0; i < addedFiles.size(); i++) {
//        byte[] buffer = new byte[4096];
//
//        while (fis.read(buffer) > 0) {
//            dos.write(buffer);
//        }
//
////            }
//        fis.close();
//    }

//    private void sendDeleteString() {
//        String flsh = delete.size() + "";
//        System.out.println("faulty string is : " + flsh);
//        bp.println(delete.size() + "");
//        bp.flush();
//
//
//        for (int i = 0; i < delete.size(); i++) {
//            bp.println(delete.get(i));
//            bp.flush();
//        }
//    }

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
