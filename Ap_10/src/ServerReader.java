import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class ServerReader extends Thread {

    private ServerSocket serverSocket,ss;
    private int numberOfConnection = 0;
    private File base;
    Socket socket;
    private ArrayList<String> addedFiles = new ArrayList<>();
    //ArrayList<String>deletedFileNames=new ArrayList<>();
    private ArrayList<String> deletedFilenames = new ArrayList<>();
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
            saveFile();
            manageDels();
            System.out.println("saveFiles finished, in server reader . ");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initialize() throws Exception {

        File del=new File("E:\\DELS");
        if(del.exists())
            MultiServer.deleteFile(del);

        del.mkdir();

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


            System.out.println(fileName + "  was file name " );
            addedFiles.add(fileName);

            //      Scanner scanner=new Scanner(System.in);
            //     int b=scanner.nextInt();
            //   System.out.println("DOWN gggg");
        }


        while (true) {

            String delName = br.readLine();
            System.out.println(delName + "  was del name " );

            if (delName.equals(":END") || delName.equals("::END"))
                break;


            //      System.out.println(delName + " " + delName.length());
            deletedFilenames.add(delName);
            //      System.out.println("DOWN HERE");

        }

        //    JOptionPane.showMessageDialog(null,deletedFilenames.size());



    }

    private void manageDels()
    {
        if(deletedFilenames==null || deletedFilenames.size()==0)
            return;

        File [] files=base.listFiles();

        for (int i=0;i<deletedFilenames.size();i++)
            for(int j=0;j<files.length;j++)
            {
                if(files[j]==null)
                    continue;

                if(files[j].getName().equals(deletedFilenames.get(i)))
                {
                    File F=files[j];
                    MultiServer.deleteFile(F);
                    files[j]=null;
                }
            }
    }


    private void saveFile() throws IOException {

        BufferedInputStream bis = new BufferedInputStream(input);
        DataInputStream dis = new DataInputStream(bis);

        int filesCount = dis.readInt();
        File[] files = new File[filesCount];

        for(int i = 0; i < filesCount; i++)
        {
            boolean mustBeDeleted=false;
            long fileLength = dis.readLong();
            String fileName = dis.readUTF();

            if(turn.equals("second"))
            {
                File [] checkFiles=base.listFiles();

                for (int j=0;j<checkFiles.length;j++)
                    if(checkFiles[j].getName().equals(fileName))
                    {
                        mustBeDeleted=true;
                        break;
                    }
            }

            if(!mustBeDeleted)
            files[i] = new File(base.getAbsolutePath()+"\\"+fileName);

            else
                files[i] = new File(base.getParentFile()+"\\"+"DELS"+"\\"+fileName);

            FileOutputStream fos = new FileOutputStream(files[i]);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            for(int j = 0; j < fileLength; j++) bos.write(bis.read());

            bos.close();
        }

        File del=new File("E:\\DELS");
        if(del.exists())
            MultiServer.deleteFile(del);



        dis.close();
    }

    public ArrayList<String> getDeletedFilenames()
    {
        return this.deletedFilenames;
    }




}