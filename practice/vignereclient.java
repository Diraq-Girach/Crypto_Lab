import java.util.*;
import java.math.*;
import java.security.*;
import java.net.*;
import java.io.*;

public class vignereclient {
    static String encrypt(String m, String k){
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
            res.append((char)(((c - 'a')+(fk.charAt(ind++)-'a') )%26 + 'a'));

        }
        return res.toString();
    }
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        try(Socket socket = new Socket("localhost", 5000);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream())){
                System.out.println("Connected to Server");
                System.out.println("Enter message");
                String m = in.nextLine();
                System.out.println("Enter key");
                String k = in.nextLine();
                String enc = encrypt(m, k);
                dos.writeUTF(enc);
                System.out.println("data sent!");
            }
    }
}
