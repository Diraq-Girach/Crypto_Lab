import java.util.*;
import java.io.*;
import java.net.*;

class sender {
    public static String encrypt(String a, String b){
        String m = a.toLowerCase();
        String k = b.toLowerCase();
        StringBuilder result = new StringBuilder();
        int mul = m.length()/k.length();
        String fk = k.repeat(mul+1);
        int keyindex = 0; 
        for(int i = 0; i<m.length(); i++){
             if(!Character.isAlphabetic(m.charAt(i))){
                result.append(m.charAt(i));
                continue;
            }
            result.append((char)(((m.charAt(i) - 'a') + (fk.charAt(keyindex++) - 'a'))%26+'a'));
        }
        
        return result.toString();
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
            String k = in.nextLine();
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

