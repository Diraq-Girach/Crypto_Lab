import java.util.*;
import java.io.*;
import java.net.*;

public class vernamserver {
    static String decrypt(String m, String k){
        StringBuilder res = new StringBuilder();
        m = m.toLowerCase();
        k = k.toLowerCase();
        if(m.length() != k.length()){
            System.err.println("Teri ma ka bhosda");
        }
        for(int i = 0; i<m.length(); i++){
            res.append((char)(((m.charAt(i) - 'a') ^ (k.charAt(i) - 'a')) + 'a' ));
        }
        return res.toString();
    }
    public static void main(String[] args) throws Exception{
        System.out.println("listening to 5000...");
        Scanner in = new Scanner(System.in);
        try(
            ServerSocket serverSocket = new ServerSocket(5000);
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
        ){
            String s = dis.readUTF();
            String k = in.nextLine();
            String dec = decrypt(s, k);
            System.out.println(dec);
        }
    }
}
