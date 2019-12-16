


import javax.swing.*;
import javax.xml.ws.soap.Addressing;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends Thread {

    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private ArrayList<String> addedFiles;
    private ArrayList<String> deletedFiles;
    private String identity="none";


    public Client(String host, int port, ArrayList<String> addedFiles) {
        try {
            this.addedFiles = addedFiles;
            socket = new Socket(host, port);
            output=socket.getOutputStream();
            input=socket.getInputStream();

//            InputStream input = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(input));
//            String sz = br.readLine();
//            System.out.println(sz);

            //   sendFile(addedFiles);
            sendFile();

        } catch (Exception e) {
            System.out.println("fuck");
            e.printStackTrace();
        }
    }

    public void sendFile() throws IOException {

        PrintWriter bw = new PrintWriter(new OutputStreamWriter(output));
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
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

       String turn= br.readLine();

        if(turn.equals("first"))
            identity="first";

        if(turn.equals("second"))
            identity="second";


        System.out.println(identity);

        for (int i = 0; i < addedFiles.size(); i++) {
            bw.println(new File(addedFiles.get(i)).getName());
            bw.flush();
        }

        bw.println(":END");
        bw.flush();

        bw.println("deleteA");


        bw.println(":END");
        bw.flush();

        DataOutputStream dos = new DataOutputStream(output);
//        FileInputStream fis = new FileInputStream(file);

        for (int j = 0; j < addedFiles.size(); j++) {
         //   DataOutputStream dos = new DataOutputStream(output);
            FileInputStream fis = new FileInputStream(addedFiles.get(j));

            for (int i = 0; i < addedFiles.size(); i++) {
                byte[] buffer = new byte[4096];

                while (fis.read(buffer) > 0) {
                    dos.write(buffer);
                }

            }
            fis.close();
        }

//        Thread.asd
        dos.close();
    }

    public static void main(String[] args) {
        ArrayList<String> temp = new ArrayList<>();
        temp.add("C:\\Users\\erfan\\Desktop\\ds2.cpp");
        temp.add("C:\\Users\\erfan\\Desktop\\nanbaj.pdf");
        temp.add("C:\\Users\\erfan\\Desktop\\Royal_UML.pdf");

        Client fc = new Client("localhost", 22000, temp);
    }

}


///////////////
//
//
//
//import javax.swing.*;
//        import javax.xml.ws.soap.Addressing;
//        import java.io.*;
//        import java.net.Socket;
//        import java.util.ArrayList;
//        import java.util.Scanner;
//
//public class Client extends Thread {
//
//    private Socket socket;
//    private OutputStream output;
//    private ArrayList<String>addedFiles;
//    private ArrayList<String >deletedFiles;
//
//    public Client(String host, int port, String file) {
//        try {
//            socket = new Socket(host, port);
//
////            InputStream input = socket.getInputStream();
////            BufferedReader br = new BufferedReader(new InputStreamReader(input));
////            String sz = br.readLine();
////            System.out.println(sz);
//
//            sendFile(file);
//
//        } catch (Exception e) {
//            System.out.println("fuck");
//            e.printStackTrace();
//        }
//    }
//
//    public void sendFile(String file) throws IOException {
//        OutputStream output = socket.getOutputStream();
//        PrintWriter bw = new PrintWriter(new OutputStreamWriter(output));
//        //   Scanner scanner = new Scanner(System.in);
//        String st = new File(file).getName();
//
////        bw.println(st);
////        bw.flush();
////        bw.println("1");
////        bw.flush();
////        bw.println("2");
////        bw.flush();
////        bw.println("3");
////        bw.flush();
////        bw.println(":END");
////        bw.flush();
////
//
//        for (int i=0;i< addedFiles.size();i++)
//        {
//            bw.println(addedFiles);
//            bw.flush();
//        }
//
//
//        DataOutputStream dos = new DataOutputStream(output);
//        FileInputStream fis = new FileInputStream(file);
//
//        for (int i=0;i<addedFiles.size();i++)
//        {
//            byte[] buffer = new byte[4096];
//
//            while (fis.read(buffer) > 0) {
//                dos.write(buffer);
//            }
//
//        }
//
//
//        fis.close();
//        dos.close();
//    }
//
//    public static void main(String[] args) {
//        Client fc = new Client("localhost", 22000, "C:\\Users\\erfan\\Desktop\\ds2.cpp");
//    }
//
//}
