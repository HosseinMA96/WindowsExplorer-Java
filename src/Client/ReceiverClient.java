package Client;


import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ReceiverClient extends Thread {
    private File base;
    private Socket socket;
    private OutputStream output;
    private InputStream input;
    private PrintWriter bw;
    private BufferedReader br;
    private String command;
    private DataInputStream dis;
    private boolean withdraw = false;
    private ArrayList<String> delte = new ArrayList<>();


    public ReceiverClient(File saveAddress, String host, int port) throws Exception {
        base = saveAddress;
        socket = new Socket(host, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        bw = new PrintWriter(new OutputStreamWriter(output));
        br = new BufferedReader(new InputStreamReader(input));
        dis = new DataInputStream(input);
        initialize();
    }

    /////////////////////////


    private void initialize() {

        File F;

        F = new File(base.getAbsolutePath());

        if (F.exists())
            deleteFile(F);


        F = new File(base.getAbsolutePath());
        F.mkdir();


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

    ////////////////////

    @Override
    public void run() {
        //   System.out.println("in Receiver run");
        try {
            receiveFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   receiveDeleteArray();
        // System.out.println("in Receiver finished");

    }


    private void receiveFiles() throws Exception {

        BufferedInputStream bis = new BufferedInputStream(input);
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        for (int i = 0; i < filesCount; i++) {
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();
            ////////////////////////////////////
            String path = dis.readUTF();
            String temp = base.getAbsolutePath() + path;
            File tempFile = new File(temp);

            if (!tempFile.exists())
                tempFile.mkdirs();

            files[i] = new File(base.getAbsolutePath() + "\\" + path + fileName);

            ///////////////////////////////////

            //    files[i] = new File(savingDir.getAbsolutePath() + "\\" + fileName);

            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for (int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
        }

        dis.close();

    }


//    private void receiveDeleteArray() {
//        try {
//            String fuck = br.readLine();
//            System.out.println("fuck is " + fuck);
//            int arraySize = Integer.parseInt(fuck);
//            System.out.println("arr size is " + arraySize);
//
//            for (int i = 0; i < arraySize; i++)
//                delte.add(br.readLine());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}


