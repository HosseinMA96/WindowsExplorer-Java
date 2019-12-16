
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerReader extends Thread {

    private ServerSocket serverSocket;
    private int numberOfConnection = 0;
    private File base;
    Socket socket;
    private ArrayList<String> addedFiles = new ArrayList<>();
    //ArrayList<String>deletedFileNames=new ArrayList<>();
    private HashSet<String> deletedFilenames = new HashSet<>();
    private InputStream input;
    private OutputStream output;
    private boolean isFirst;
    private BufferedReader br;
    private PrintWriter bp;
    private String turn;
    private DataInputStream dis;


    //  public ServerReader(int port) {
    public ServerReader(Socket socket, boolean first, File location) {
        try {
            //    serverSocket = new ServerSocket(port);
            //    initializeServer();
            //   Socket socket=serverSocket.accept();
            this.socket = socket;
            isFirst = first;
            base = location;

            input = socket.getInputStream();
            output = socket.getOutputStream();
            br = new BufferedReader(new InputStreamReader(input));
            bp = new PrintWriter(new OutputStreamWriter(output));
            dis=new DataInputStream(input);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            initialize();
            saveFile(socket);
            System.out.println("Accept");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initialize() throws Exception {

        turn = br.readLine();


        if (turn.equals("none")) {
            if (isFirst) {
                bp.println("first");
                turn = "first";
            } else {
                bp.println("second");
                turn = "second";
            }


            bp.flush();

        } else {
            bp.println("unImportantMessage");
            bp.flush();
        }


        System.out.println("here");

        while (true) {
//            System.out.println("in while");
            String fileName = br.readLine();
            if (fileName.equals(":END"))
                break;


            System.out.println(fileName + " " + fileName.length());
            addedFiles.add(fileName);

            //      Scanner scanner=new Scanner(System.in);
            //     int b=scanner.nextInt();
         //   System.out.println("DOWN gggg");
        }


        while (true) {

            String delName = br.readLine();


            if (delName.equals(":END") || delName.equals("::END"))
                break;


      //      System.out.println(delName + " " + delName.length());
            deletedFilenames.add(delName);
      //      System.out.println("DOWN HERE");

        }



    }

    private void addFile(File temp) throws Exception
    {
    //    DataInputStream dis = new DataInputStream(input);
        FileOutputStream fos = new FileOutputStream(base.getAbsolutePath() + "\\" + temp.getName());


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

    private void saveFile(Socket clientSock) throws IOException {

        //اینجا شک دارم که دو خط پایین باید داخل وایل باشه یا نه؟ فعلا میزارم بیرون
//        DataInputStream dis = new DataInputStream(input);



        System.out.println("out here");

        for (int i = 0; i < addedFiles.size(); i++) {

            File temp = new File(addedFiles.get(i));
            //For first, save new files
            if (turn.equals("first")) {

//                FileOutputStream fos = new FileOutputStream(base.getAbsolutePath() + "\\" + temp.getName());
//
//
//                byte[] buffer = new byte[4096];
//
//                int filesize = 15123; // Send file size in separate msg
//                int read = 0;
//                int totalRead = 0;
//                int remaining = filesize;
//                while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
//                    totalRead += read;
//                    remaining -= read;
//          //          System.out.println("read " + totalRead + " bytes.");
//                    fos.write(buffer, 0, read);
//                }
//
//                fos.close();
                try {
                    addFile(temp);
                }
                catch (Exception e)
                {
                    JOptionPane.showMessageDialog(null,"unable to add file "+temp.getName()+" to server");
                }
            }

            //for second, only save if there's no file with current name
            else
            {
                boolean alreadyExist=false;

                File [] files=base.listFiles();

                for (int j=0;j<files.length;j++)
                    if(files[j].getName().equals(temp.getName()))
                    {
                        alreadyExist=true;
                        break;
                    }


                if(!alreadyExist)
                {
                    try {
                        addFile(temp);
                    }
                    catch (Exception e)
                    {
                        JOptionPane.showMessageDialog(null,"unable to add file "+temp.getName()+" to server");
                    }
                }
            }
        }


        dis.close();
    }

    public HashSet<String> getDeletedFilenames()
    {
        return this.deletedFilenames;
    }


}