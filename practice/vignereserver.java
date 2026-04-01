import java.util.*;
import java.math.*;
import java.io.*;
import java.net.*;

public class vignereserver {
    static String decrypt(String m, String k){
        StringBuilder res = new StringBuilder();
        m = m.toLowerCase();
        k = k.toLowerCase();
        int mul = m.length()/k.length();
        String fk = k.repeat(mul+1);
        int ind = 0;
        for(char c:m.toCharArray()){
            if (!Character.isAlphabetic(c)){
                res.append(c);
                continue;
            }
            res.append((char)(((c - 'a')-(fk.charAt(ind++)-'a') +26) %26 + 'a'));

        }
        return res.toString();
    }
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        System.out.println("Listening to port 5000.....");
        try(ServerSocket serverSocket = new ServerSocket(5000);
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(socket.getInputStream())){
                System.out.println("Connected to client");
                String m = dis.readUTF();
                System.out.println("received: " + m);
                System.out.println("Enter the key for decryption:");
                String k = in.nextLine();
                String dec = decrypt(m, k);
                System.out.println("Decrypted text: " + dec);

        }
        
    }
}
