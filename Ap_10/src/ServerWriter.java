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
    private ArrayList<String> list = new ArrayList<>();
    ArrayList<String>delete;

    public ServerWriter(Socket socket, File baseFile,ArrayList<String> del) {
        try {
            this.socket = socket;
            input = socket.getInputStream();
            output = socket.getOutputStream();
            dos = new DataOutputStream(output);
            base = baseFile;
            bp = new PrintWriter(new OutputStreamWriter(output));
            delete=del;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendAllDirFiles(base,"");
        bp.println("END:FILE:SEND");
        bp.flush();
        sendDeleteString();
    }


    private  void sendAllDirFiles(File currentDir,String currentPath) {
//        File[] files = base.listFiles();
//
//        for (int i = 0; i < files.length; i++) {
//            if (files[i].isFile()) {
//
//            }
//
//        }

        File [] files=currentDir.listFiles();

        for (int i=0;i<files.length;i++)
        {
            if(files[i].isFile())
            {
                try{
                    String fileName=files[i].getName();
                    bp.println(fileName);
                    bp.flush();


                    sendFile(files[i],currentPath);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            else
            {
                String dirName=files[i].getName();

                bp.println(":DIR");
                bp.flush();
                bp.println(dirName);
                bp.flush();

                sendAllDirFiles(files[i],currentPath+"\\"+dirName);

            }

        }

    }


  private   void sendFile(File F,String pathToThisFile) throws Exception {
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

    private void sendDeleteString()
    {
        bp.println(delete.size());
        bp.flush();


        for (int i=0;i<delete.size();i++)
        {
            bp.println(delete.get(i));
            bp.flush();
        }
    }
}



