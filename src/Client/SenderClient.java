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
    private String identity = "none";
    private PrintWriter bw;
    private BufferedReader br;
    private DataOutputStream dos;


    //  public SenderClient(String host, int port, ArrayList<String> addedFiles, ArrayList<String> nestedDirs) {
    public SenderClient(String host, int port, File base) {
        try {
            addedFiles = new ArrayList<>();
            socket = new Socket(host, port);
            output = socket.getOutputStream();
            input = socket.getInputStream();
            bw = new PrintWriter(new OutputStreamWriter(output));
            br = new BufferedReader(new InputStreamReader(input));
            dos = new DataOutputStream(output);
            dirs = new ArrayList<>();
            //   preInit(base);
            initialize(base,"");
        } catch (Exception e) {
            System.out.println("fuck");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        sendFile();
    }

    public void sendFile() {

        try {
            bw.println(identity);
            bw.flush();

            String turn = br.readLine();

            if (turn.equals("first"))
                identity = "first";

            if (turn.equals("second"))
                identity = "second";


            System.out.println("Pre identity");
            System.out.println(identity);

            for (int i = 0; i < addedFiles.size(); i++) {
                bw.println(new File(addedFiles.get(i)).getName());
                bw.flush();
            }

            bw.println(":END");
            bw.flush();

            //Deleted files must be upgraded
            bw.println("deleteA");
            bw.flush();


            bw.println(":END");
            bw.flush();

            bw.println(addedFiles.size() + "");
            bw.flush();

            for (int i = 0; i < addedFiles.size(); i++) {
                bw.println(dirs.get(i));
                bw.flush();
            }


            sendAFile("abc");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendAFile(String absoluteAddress) throws Exception {
        //    String directory = ...;
        //    String hostDomain = ...;
        //   int port = ...;

        //   File[] files = new File(directory).listFiles();

        //  Socket socket = new Socket(InetAddress.getByName(hostDomain), port);

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

    public static void main(String[] args) throws Exception {
        SenderClient sc = new SenderClient("127.0.0.1", 22000, new File("C:\\Users\\erfan\\Desktop\\base1"));
        sc.start();

        File F = new File("C:\\Users\\erfan\\Desktop\\first");


        System.out.println();

        try {
            sc.join();


            Scanner scanner = new Scanner(System.in);
            String A = scanner.nextLine();


            // System.out.println("after join");
            ReceiverClient rc = new ReceiverClient(F, "127.0.0.1", 22000);

            rc.start();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    void initialize(File directory, String aux) {
        File []F=directory.listFiles();
        //System.out.println(directory.getAbsolutePath() + " "+F.length + " " +aux);

        if(F!=null)
            for (int i=0;i<F.length;i++)
            {
                if(F[i].isFile() )
                {
                    addedFiles.add(F[i].getAbsolutePath());
                    dirs.add(aux);
                }

                else
                {

                    String temp=F[i].getName();
                    temp=aux+"\\"+temp;
                    initialize(F[i],temp);
                }
            }

//        if(F.length==0 && !directory.getName().equals("divert"))
//        {
//            System.out.println(directory.getAbsolutePath());
//            File divert=new File(directory.getAbsolutePath()+"\\"+"divert");
//            divert.mkdir();
//        }



    }

    private void preInit(File directory)
    {
        File [] F=directory.listFiles();

        if(F!=null)
        {
            if(F.length==0)
            {
                File divert=new File(directory.getAbsolutePath()+"\\"+"divert");
                try {
                    divert.createNewFile();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            else
            {
                for (int i=0;i<F.length;i++)
                    if(F[i].isDirectory())
                        preInit(F[i]);
            }

        }
    }

}

