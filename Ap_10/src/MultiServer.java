import javafx.scene.control.ProgressBar;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class MultiServer {
    private static File base;
    private static ArrayList<String> toBeDeletedInFirst = new ArrayList<>();
    private static ArrayList<String> firstDeleted, secondDeleted;


    private static void initializeServer() {

        base = new File("E:\\JFileManagerItems");
        if (base.exists())
            deleteFile(base);


        base = new File("E:\\JFileManagerItems");
        base.mkdirs();

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

            if(temp != null)
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
        ServerSocket serverSocket = new ServerSocket(32000);
        initializeServer();




        while (true) {


            //BAYAD BA TAG BEGI KE KI VASL SHODE BE CHE MANZOORI. INO DOROST KON BA TIME NEMISHE BEGI

            Socket socketReader1 = serverSocket.accept();
            ServerReader serverReader1 = new ServerReader(socketReader1, true, base);

            System.out.println("first reader accepted");

            Socket socketReader2 = serverSocket.accept();
            System.out.println("second reader accepted");
            ServerReader serverReader2 = new ServerReader(socketReader2, false, base);


            serverReader1.start();
            serverReader2.start();

            System.out.println("both reader started");



            serverReader1.join();
            serverReader2.join();


            mergeFile(serverReader1, serverReader2);

            //ESHTEB NASHE DILITA :|
            Socket socketWriter1 = serverSocket.accept();
            ServerWriter serverWriter1 = new ServerWriter(socketWriter1, base, toBeDeletedInFirst,firstDeleted);


            Socket socketWriter2 = serverSocket.accept();
            ServerWriter serverWriter2 = new ServerWriter(socketWriter2, base, toBeDeletedInFirst,firstDeleted);



            System.out.println("Both readers joined");


            serverWriter2.start();
            serverWriter1.start();


            System.out.println("Both writers started");


        }


    }


    private static void mergeFile(ServerReader serverReader1, ServerReader serverReader2) {
        firstDeleted = new ArrayList<>();
        secondDeleted = new ArrayList<>();

        if (serverReader1.getTurn().equals("first")) {
            firstDeleted = serverReader1.getDeletedFilenames();
            secondDeleted = serverReader2.getDeletedFilenames();
        } else {
            secondDeleted = serverReader1.getDeletedFilenames();
            firstDeleted = serverReader2.getDeletedFilenames();
        }


        System.out.println("IN MULTI SERVER, FIRST DELETED ARRAYLIST IS :");
        for (int i=0;i<firstDeleted.size();i++)
            System.out.println(firstDeleted.get(i));


        System.out.println("IN MULTI SERVER, SECOND DELETED ARRAYLIST IS :");
        for (int i=0;i<secondDeleted.size();i++)
            System.out.println(secondDeleted.get(i));

        File firstFile = new File("E:\\temp1");
        File secondFile = new File("E:\\temp2");


        File[] files = firstFile.listFiles();

        initializeServer();

        if (files != null)
            for (int i = 0; i < files.length; i++) {
                try {
                    System.out.println("base absolote " + base.getAbsolutePath());
                    System.out.println("file path " + files[i].getAbsolutePath());
                    pasteFile(files[i].getAbsolutePath(), base.getAbsolutePath() + "\\" + files[i].getName());

                    for (int j = 0; j < secondDeleted.size(); j++)
                        if (files[i].getName().equals(secondDeleted.get(j))) {
                            secondDeleted.remove(j + 0);
                            break;
                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        files = secondFile.listFiles();
        File[] dummyFiles = base.listFiles();

        if (files != null)
            for (int i = 0; i < files.length; i++) {
                boolean coppy = true;

                for (int j = 0; j < firstDeleted.size(); j++)
                    if (firstDeleted.get(j).equals(files[i].getName())) {
                        coppy = false;
                        break;
                    }

                if (dummyFiles != null)
                    for (int j = 0; j < dummyFiles.length; j++) {
                        if (dummyFiles[j].getName().equals(files[i].getName())) {
                            //In case of owerriten new file, first delete it from second cache then add it from first one
                            firstDeleted.add(dummyFiles[j].getName());
                            coppy = false;
                            break;
                        }
                    }

                if (coppy) {
                    try {
                        pasteFile(files[i].getAbsolutePath(), base.getAbsolutePath() + "\\" + files[i].getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }

        toBeDeletedInFirst = secondDeleted;
    }


    public static void pasteFile(String from, String to) throws IOException {

        File sourceFolder = new File(from);

        File destinationFolder = new File(to);

        if (sourceFolder.isDirectory()) {
            //Verify if destinationFolder is already present; If not then create it
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();

            }

            //Get all files from source directory
            String files[] = sourceFolder.list();

            //Iterate over all files and copy them to destinationFolder one by one
            for (String file : files) {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);

                //Recursive function call
                pasteFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            }
        } else {
            //Copy the file content from one place to another;
            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //   Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.COPY_ATTRIBUTES);

        }


    }

}
