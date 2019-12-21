/**
 * A Class which acts as the servers, reads and manipulates files from sockets and sends them to the computers
 */

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


    /**
     * Initialize the process, make the place needed to save the incoming files
     */
    private static void initializeServer() {

        base = new File("E:\\JFileManagerItems");
        if (base.exists())
            deleteFile(base);


        base = new File("E:\\JFileManagerItems");
        base.mkdirs();

    }

    /**
     * A function to delete file f
     *
     * @param f
     */
    public static void deleteFile(File f) {

        if (f.isFile()) {
            try {
                f.delete();


            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error happened in deleting process", "Error", 1);
            }
        }

        if (f.isDirectory()) {
            File[] temp = f.listFiles();

            if (temp != null)
                for (int i = 0; i < temp.length; i++)
                    deleteFile(temp[i]);

            try {
                if (!f.delete())
                    JOptionPane.showMessageDialog(null, "Unable to delete", "Error", 1);

            } catch (Exception ex) {

            }
        }


    }

    /**
     * Main method, always runs the server
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(30000);
        initializeServer();


        while (true) {


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


            Socket socketWriter1 = serverSocket.accept();
            ServerWriter serverWriter1 = new ServerWriter(socketWriter1, base, toBeDeletedInFirst, firstDeleted);


            Socket socketWriter2 = serverSocket.accept();
            ServerWriter serverWriter2 = new ServerWriter(socketWriter2, base, toBeDeletedInFirst, firstDeleted);


            System.out.println("Both readers joined");
            System.out.println("Both writers started");


            serverWriter2.start();
            serverWriter1.start();


            serverWriter1.join();
            serverWriter2.join();

            initializeServer();


            System.out.println("Both writers joined");


        }


    }

    /**
     * A Method to merge two files read from two computers, taking into account their turns and also deleted files
     *
     * @param serverReader1
     * @param serverReader2
     */
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
        for (int i = 0; i < firstDeleted.size(); i++)
            System.out.println(firstDeleted.get(i));


        System.out.println("IN MULTI SERVER, SECOND DELETED ARRAYLIST IS :");
        for (int i = 0; i < secondDeleted.size(); i++)
            System.out.println(secondDeleted.get(i));

        File firstFile = new File("E:\\temp1");
        File secondFile = new File("E:\\temp2");


        File[] files = firstFile.listFiles();

        initializeServer();

        if (files != null)
            for (int i = 0; i < files.length; i++) {
                try {

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

    /**
     * A Method to paste file from one address to another
     *
     * @param from
     * @param to
     * @throws IOException
     */
    public static void pasteFile(String from, String to) throws IOException {

        File sourceFolder = new File(from);

        File destinationFolder = new File(to);

        if (sourceFolder.isDirectory()) {

            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();

            }


            String files[] = sourceFolder.list();


            for (String file : files) {
                File srcFile = new File(sourceFolder, file);
                File destFile = new File(destinationFolder, file);


                pasteFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            }
        } else {

            Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);

        }


    }


}
