import java.io.*;
import java.util.*;
import java.net.*;
import java.math.*;
import java.security.*;

class client{
    public static String encrypt(String m, int k){
        m = m.toLowerCase();
        StringBuilder res = new StringBuilder();
        for(char c: m.toCharArray()){
            if(!Character.isAlphabetic(c)){
                res.append((char)c);
                continue;
            }
            res.append((char)((c - 'a' + k)%26+ 'a'));
        }
        return res.toString();
    }
    public static void main(String args[]) throws Exception{
        try(Socket socket = new Socket("localhost",5000);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream())){
                System.out.println("Connected to Server... \nEnter message:");
                Scanner in = new Scanner(System.in);
                String s = in.nextLine();
                System.out.println("Enter secret key:");
                int k = in.nextInt();
                String enc = encrypt(s, k);
                out.writeUTF(enc);
                System.out.println("Sent data: "+ enc);
            }
    }
}