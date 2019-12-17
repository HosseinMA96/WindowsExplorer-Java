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
            System.out.println("in sw consturctor , del size : " + del.size());

            this.socket = socket;
            input = socket.getInputStream();
            output = socket.getOutputStream();
            dos = new DataOutputStream(output);
            base = new File(baseFile.getAbsolutePath());

            bp = new PrintWriter(new OutputStreamWriter(output));
            delete = del;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        System.out.println("in run " + base.getAbsolutePath());
        base=new File("in run " +base.getAbsolutePath()+" "+base.exists());
        System.out.println("in server writer");
//        System.out.println("virgin length: "+base.listFiles().length + base.exists());
        sendAllDirFiles(base, "");
        bp.println("END:FILE:SEND");
        bp.flush();
        sendDeleteString();
        System.out.println("sw finished");
    }


    private void sendAllDirFiles(File currentDir, String currentPath) {
//        File[] files = base.listFiles();
//
//        for (int i = 0; i < files.length; i++) {
//            if (files[i].isFile()) {
//
//            }
//
//        }

        System.out.println("curr Dir is : "+currentDir);

        System.out.println("crashed");
        System.out.println("This dir.exist: "+currentDir.exists());
//        System.out.println(currentDir.listFiles().length);
        File []files = currentDir.listFiles();
//        System.out.println(files.length);



        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                try {
                    String fileName = files[i].getName();
                    bp.println(fileName);
                    bp.flush();


                    sendFile(files[i], currentPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                String dirName = files[i].getName();

                bp.println(":DIR");
                bp.flush();
                bp.println(dirName);
                bp.flush();

                sendAllDirFiles(files[i], currentPath + "\\" + dirName);

                bp.println(":DIR:FINISHED");

            }

        }

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
