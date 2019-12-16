import javax.swing.*;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

public class MultiServer {
    private static File base;
    private static int clientCount=0;
    private static ArrayList<Server>servers=new ArrayList<>();
    private static void initializeServer() {

        base = new File("E:\\JFileManagerItems");
        if (base.exists())
            deleteFile(base);


        base = new File("E:\\JFileManagerItems");
        base.mkdir();


        //     System.out.println("make " + base.mkdir());
    }

    private static void deleteFile(File f) {

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

  private   static void accumulate(Set<String>deletes,ArrayList<String>added)
  {

  }
//    {
//        for asdsad
//    }

    public static void main(String[] args) throws  Exception{

//        Server fs = new Server(22000);
        ServerSocket serverSocket=new ServerSocket(22000);
        initializeServer();

//        while(true)
        {
            clientCount++;
            Socket socket=serverSocket.accept();
            Server tempServer=new Server(socket,clientCount==1,base);
            servers.add(tempServer);
            tempServer.start();
           // tempServer.wait();


            if(servers.size()>1)
            {

            }
        }

//        fs.start();

    }

}
