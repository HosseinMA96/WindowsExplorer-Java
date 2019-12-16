
import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Server extends Thread {

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


    //  public Server(int port) {
    public Server(Socket socket, boolean first, File location) {
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
           bp= new PrintWriter(new OutputStreamWriter(output));


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


        String turn = br.readLine();



        if (turn.equals("none")) {
            if (isFirst)
                bp.println("first");

            else
                bp.println("second");

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
        }


        while (true) {

            String delName = br.readLine();


            if (delName.equals(":END") || delName.equals("::END"))
                break;


            System.out.println(delName + " " + delName.length());
            deletedFilenames.add(delName);

        }

        System.out.println();
    }

    private void saveFile(Socket clientSock) throws IOException {

        //اینجا شک دارم که دو خط پایین باید داخل وایل باشه یا نه؟ فعلا میزارم بیرون
        DataInputStream dis = new DataInputStream(input);


        System.out.println("out here");

        for (int i = 0; i < addedFiles.size(); i++) {


            File temp = new File(addedFiles.get(i));
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


        dis.close();
    }

    public void setDeletedFilenames(HashSet<String> deletedFilenames) {
        this.deletedFilenames = deletedFilenames;
    }


}