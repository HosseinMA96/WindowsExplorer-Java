import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ReceiverClient extends Thread {
    private File reserve;
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
        reserve = saveAddress;
        socket = new Socket(host, port);
        output = socket.getOutputStream();
        input = socket.getInputStream();
        bw = new PrintWriter(new OutputStreamWriter(output));
        br = new BufferedReader(new InputStreamReader(input));
        dis = new DataInputStream(input);
        initialize();
        //  bw.println(":RECEIVE");
        //  bw.flush();
    }

    /////////////////////////


    private  void initialize() {

        File F=new File(reserve.getAbsolutePath());

        F = new File("E:\\JFileManagerItems");
        if (F.exists())
            deleteFile(F);


        F = new File(reserve.getAbsolutePath());
        F.mkdir();


        //     System.out.println("make " + base.mkdir());
    }

    private  void deleteFile(File f) {

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
        System.out.println("in Receiver run");
        receiveFiles(reserve);
        receiveDeleteArray();
        System.out.println("in Receiver finished");

    }


    private void receiveFiles(File savingDir) {
        try {
            while (!withdraw) {

                command = br.readLine();
                System.out.println("command is : " + command);

                if (command.equals("END:FILE:SEND")) {
                    withdraw = true;
                    return;
                }


                if (command.equals(":DIR:FINISHED"))
                    return;


                if (command.equals(":DIR")) {
                    String newDir = br.readLine();
                    newDir = savingDir.getAbsolutePath() + "\\" + newDir;
                    receiveFiles(new File(newDir));
                } else {
                    String fileName = br.readLine();
                    receiveAFile(savingDir, fileName);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void receiveAFile(File savingDir, String fileName) throws Exception {
        //    DataInputStream dis = new DataInputStream(input);
        FileOutputStream fos = new FileOutputStream(savingDir.getAbsolutePath() + "\\" + fileName);


        byte[] buffer = new byte[4096];

        int filesize = 15123; // Send file size in separate msg
        int read = 0;
        int totalRead = 0;
        int remaining = filesize;
        while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            fos.write(buffer, 0, read);
        }

        fos.close();
    }

    private void receiveDeleteArray() {
        try {
            String fuck = br.readLine();
            System.out.println("fuck is " + fuck);
            int arraySize = Integer.parseInt(fuck);
            System.out.println("arr size is " + arraySize);

            for (int i = 0; i < arraySize; i++)
                delte.add(br.readLine());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


