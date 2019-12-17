


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
    private ArrayList<String> addedFiles;
    private ArrayList<String> deletedFiles;
    private String identity = "none";
    private PrintWriter bw;
    private BufferedReader br;
    DataOutputStream dos;


    public SenderClient(String host, int port, ArrayList<String> addedFiles) {
        try {
            this.addedFiles = addedFiles;
            socket = new Socket(host, port);
            output = socket.getOutputStream();
            input = socket.getInputStream();
            bw = new PrintWriter(new OutputStreamWriter(output));
            br = new BufferedReader(new InputStreamReader(input));
            dos = new DataOutputStream(output);
            //    bw.println(":SEND");
            //  bw.flush();

//            InputStream input = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(input));
//            String sz = br.readLine();
//            System.out.println(sz);

            //   sendFile(addedFiles);


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
//            PrintWriter bw = new PrintWriter(new OutputStreamWriter(output));
//            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            //   Scanner scanner = new Scanner(System.in);
            //   String st = new File(addedFiles).getName();

//        bw.println(st);
//        bw.flush();
//        bw.println("1");
//        bw.flush();
//        bw.println("2");
//        bw.flush();
//        bw.println("3");
//        bw.flush();
//        bw.println(":END");
//        bw.flush();
//
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

//            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
//            FileInputStream fis = new FileInputStream(file);
//            byte[] buffer = new byte[4096];
//
//            while (fis.read(buffer) > 0) {
//                dos.write(buffer);
//            }
//
//            fis.close();
//            dos.close();

//        FileInputStream fis = new FileInputStream(file);
//
//            for (int j = 0; j < addedFiles.size(); j++) {
//                //    dos=new DataOutputStream(socket.getOutputStream());
//                System.out.println("i is "+j);
//                //   DataOutputStream dos = new DataOutputStream(output);
//                File F=new File(addedFiles.get(j));
//                FileInputStream fis = new FileInputStream(F);
//                bw.println(100000+"");
//                bw.flush();
//
////            for (int i = 0; i < addedFiles.size(); i++) {
//                byte[] buffer = new byte[4096];
//
//                while (fis.read(buffer) > 0) {
//                    dos.write(buffer);
//                }
//
//
//
////            }
//                fis.close();
//                //       dos.close();
//            }
//            System.out.println("OUT after sending everything");
////        Thread.asd
//            dos.close();

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
        socket.close();
    }

    public static void main(String[] args) throws  Exception{
        ArrayList<String> temp = new ArrayList<>();
        temp.add("C:\\Users\\erfan\\Desktop\\ds2.cpp");
        temp.add("C:\\Users\\erfan\\Desktop\\Capture.PNG");
        temp.add("C:\\Users\\erfan\\Desktop\\nanbaj.pdf");
        //      temp.add("C:\\Users\\erfan\\Desktop\\New folder");


        // temp.add("C:\\Users\\erfan\\Desktop\\Royal_UML.pdf");
        //  temp.add("C:\\Users\\erfan\\Desktop\\ML.iml");


        SenderClient sc = new SenderClient("127.0.0.1", 22000, temp);
        sc.start();

        File F=new File("C:\\Users\\erfan\\Desktop\\first");



        System.out.println();

        try {
            sc.join();


            Scanner scanner=new Scanner(System.in);
            String A=scanner.nextLine();



            // System.out.println("after join");
            ReceiverClient rc=new ReceiverClient(F,"127.0.0.1",22000);
            rc.start();


        } catch (Exception e) {
            e.printStackTrace();
        }


//
//        try {
//            sc.join();
//            File F=new File("C:\\Users\\erfan\\Desktop\\first");
//
//            ReceiverClient rc = new ReceiverClient(F,"127.0.0.1",22000);
//            rc.start();
//
//
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//

    }

}

