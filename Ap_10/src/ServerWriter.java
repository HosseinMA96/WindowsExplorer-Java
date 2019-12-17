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
    private File base;
    //  private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> delete;

    public ServerWriter(Socket socket, File baseFile, ArrayList<String> del) {
        try {


            this.socket = socket;
            input = socket.getInputStream();
            output = socket.getOutputStream();
            dos = new DataOutputStream(output);
            base = baseFile;

            bp = new PrintWriter(new OutputStreamWriter(output));
            delete = del;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            sendAllDirFiles();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    //    bp.println("END:FILE:SEND");
  //      bp.flush();
      //  sendDeleteString();

    }


    private void sendAllDirFiles() throws Exception{

            //  String directory = ...;
            //    String hostDomain = ...;
            //   int port = ...;

            File[] files = base.listFiles();

            // Socket socket = new Socket(InetAddress.getByName(hostDomain), port);

            BufferedOutputStream bos = new BufferedOutputStream(output);
            DataOutputStream dos = new DataOutputStream(bos);

            dos.writeInt(files.length);

            for (File file : files) {
                long length = file.length();
                dos.writeLong(length);

                String name = file.getName();
                dos.writeUTF(name);

                FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);

                int theByte = 0;
                while ((theByte = bis.read()) != -1) bos.write(theByte);

                bis.close();
            }

            dos.close();

    }


    private void sendFile(File F, String pathToThisFile) throws Exception {
        //   DataOutputStream dos = new DataOutputStream(output);
        FileInputStream fis = new FileInputStream(F);
        bp.println(pathToThisFile);
        bp.flush();

//            for (int i = 0; i < addedFiles.size(); i++) {
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }

//            }
        fis.close();
    }

    private void sendDeleteString() {
        String flsh = delete.size() + "";
        System.out.println("faulty string is : " + flsh);
        bp.println(delete.size() + "");
        bp.flush();


        for (int i = 0; i < delete.size(); i++) {
            bp.println(delete.get(i));
            bp.flush();
        }
    }
}
