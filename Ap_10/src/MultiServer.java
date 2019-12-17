import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MultiServer {
    private static File base;
    private static int clientCount = 0;
    //   private static ArrayList<ServerReader> serverReaders = new ArrayList<>();
    private static String tag;
    private String inputStr;
    private static ArrayList<String> deleted;

    private static void initializeServer() {

        base = new File("E:\\JFileManagerItems");
        if (base.exists())
            deleteFile(base);


        base = new File("E:\\JFileManagerItems");
        base.mkdir();

        //    JOptionPane.showMessageDialog(null,"times in E del");

        //     System.out.println("make " + base.mkdir());
    }

    public static void deleteFile(File f) {

        if (f.isFile()) {
            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error happened in deleting process", "Error", 1);
            }
        }

        if (f.isDirectory()) {
            File[] temp = f.listFiles();

            for (int i = 0; i < temp.length; i++)
                deleteFile(temp[i]);

            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {

            }
        }


    }


    public static void main(String[] args) throws Exception {

//        ServerReader fs = new ServerReader(22000);
        ServerSocket serverSocket = new ServerSocket(22000);
        initializeServer();

        while (true) {

            //BAYAD BA TAG BEGI KE KI VASL SHODE BE CHE MANZOORI. INO DOROST KON BA TIME NEMISHE BEGI
            deleted = new ArrayList<>();

            Socket socketReader1 = serverSocket.accept();
            ServerReader serverReader1 = new ServerReader(socketReader1, true, base);
            serverReader1.start();
            // serverReaders.add(tempServerReader);


            Socket socketReader2 = serverSocket.accept();
            ServerReader serverReader2 = new ServerReader(socketReader2, false, base);
            serverReader2.start();

//
            //     ArrayList<String> empty = serverReader1.getDeletedFilenames();
            //    generateDelArrayList(serverReader1.getDeletedFilenames(),ServerReader2.getDeletedFilenames());
            // generateDelArrayList(serverReader1.getDeletedFilenames(),empty);

                 Socket socketWriter1 = serverSocket.accept();
                 ServerWriter serverWriter1 = new ServerWriter(socketWriter1, base, null);
                 serverWriter1.start();


            Socket socketWriter2 = serverSocket.accept();
            ServerWriter serverWriter2=new ServerWriter(socketWriter2,base,null);
            serverWriter2.start();


            //    ServerReader2.join();

            //    serverWriter1.start();
            //    serverWriter2.start();


//            Socket socket1=serverSocket.accept();
//            tag=new BufferedReader(new InputStreamReader(socket1.getInputStream())).readLine();
//
//            if(tag.equals(":SEND"))
//            {
//                ServerReader serverReader1=new ServerReader(socket1,true,base);
//                serverReader1.start();
//            }
//
//            else
//            {
//                ServerWriter serverWriter1=new ServerWriter(socket1,base)
//            }
//
//            Socket socket2=serverSocket.accept();
//            tag=new BufferedReader(new InputStreamReader(socket2.getInputStream())).readLine();
//
//            if(tag.equals(":SEND"))
//            {
//                ServerReader serverReader2=new ServerReader(socket2,false,base);
//                serverReader2.start();
//            }
//


        }


    }

    private static void generateDelArrayList(ArrayList<String> A, ArrayList<String> B) {
        HashSet<String> temp = new HashSet<>();

        for (int i = 0; i < A.size(); i++) {
            deleted.add(A.get(i));
            temp.add(A.get(i));
        }

        for (int i = 0; i < B.size(); i++) {
            if (temp.contains(B.get(i)))
                continue;

            deleted.add(B.get(i));
        }

    }

}
