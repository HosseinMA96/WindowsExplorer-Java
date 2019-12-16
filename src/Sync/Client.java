//import java.io.*;
//import java.net.Socket;
//import java.util.Scanner;
//
//public class Client {
//    public static void main(String[] args) throws Exception {
//        Socket s = new Socket("127.0.0.1", 18000);
//
//        OutputStream output = s.getOutputStream();
//        PrintWriter bw = new PrintWriter(new OutputStreamWriter(output));
//        Scanner scanner = new Scanner(System.in);
//        String st = scanner.nextLine();
//
//        bw.println(st);
//        bw.flush();
//
//        InputStream input = s.getInputStream();
//        BufferedReader br = new BufferedReader(new InputStreamReader(input));
//        String sz = br.readLine();
//
//        System.out.println("sz is : "+sz);
//        int num = Integer.parseInt(sz);
//
//        for (int i = 0; i < num; i++) {
//            input = s.getInputStream();
//            System.out.println(br.readLine());
//        }
//    }
//
//}
//
