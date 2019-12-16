//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.ArrayList;
//
//public class Server {
//    public static void main(String[] args) throws Exception {
//        ArrayList<String> A = new ArrayList<>();
//
//        ServerSocket serverSocket = new ServerSocket(18000);
//
//        while (true) {
//            Socket socket = serverSocket.accept();
//
//            InputStream input = socket.getInputStream();
//            BufferedReader br = new BufferedReader(new InputStreamReader(input));
//            String s = br.readLine();
//            System.out.println(s);
//            A.add(s);
//
//            OutputStream op = socket.getOutputStream();
//            PrintWriter bp = new PrintWriter(new OutputStreamWriter(op));
//            bp.println(A.size()+"");
//            bp.flush();
//       //     bp.close();
//
//            for (int i = 0; i < A.size(); i++) {
//                bp.println(A.get(i));
//                bp.flush();
//
//
//            }
//
//        }
//
//
//    }
//}