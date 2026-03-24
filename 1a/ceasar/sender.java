import java.util.*;
import java.io.*;
import java.net.*;

class sender {
    public static String encrypt(String s, int k){
        s = s.toLowerCase();
        StringBuilder res = new StringBuilder();
        for(char c : s.toCharArray()){
            if(!Character.isAlphabetic(c)){
                res.append(c);
                continue;
            }
            res.append((char)((c - 'a' + k)%26 + 'a'));
        }
        return res.toString();
    }
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to receiver!");
            
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            System.out.println("Enter the message:");
            String m = in.nextLine();
            System.out.println("Enter the private key:");
            int k = in.nextInt();
            String c = encrypt(m, k);
            System.out.println("The encrypted text is...: " + c);
            output.writeUTF(c);
            System.out.println("Cypher sent.");
            output.flush();
        
            output.close();
            socket.close();
            System.out.println("Connection closed");
            
        } catch (IOException e) {
            System.out.println("Sender exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

