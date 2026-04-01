import java.util.*;
import java.net.*;
import java.io.*;

class vernamclient{
    static String encrypt(String m, String k){
        StringBuilder res = new StringBuilder();
        m = m.toLowerCase();
        k = k.toLowerCase();
        if(m.length() != k.length()){
            System.err.println("Teri ma ka bhosda");
        }
        for(int i = 0; i<m.length(); i++){
            if((!Character.isAlphabetic(m.charAt(i))) || (!Character.isAlphabetic(k.charAt(i)))){
                System.err.println("Teri ma ka doble bhosda");
            }
            res.append((char)(((m.charAt(i) - 'a') ^ (k.charAt(i) - 'a')) + 'a' ));
        }
        return res.toString();
    }
    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        try(
            Socket socket = new Socket("localhost", 5000);
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream())
        ){
            String m = in.nextLine();
            String k = in.nextLine();
            String enc = encrypt(m, k);
            dos.writeUTF(enc);
        }
    }
}