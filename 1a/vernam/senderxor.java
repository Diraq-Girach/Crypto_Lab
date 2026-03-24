import java.util.*;
import java.io.*;
import java.net.*;

class senderxor {
    public static String encrypt(String a, String b){
        // NOT handling spaces because not sure how to deal with that as the key needs to be the same length
        String m = a.toLowerCase();
        String k = b.toLowerCase();
        if(m.length() != k.length()){
            System.out.println("Wrong key detected: Length mismatch");
            return "ERROR!!";
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0; i<m.length(); i++){
            if(!Character.isAlphabetic(m.charAt(i)) || !Character.isAlphabetic(m.charAt(i))){
                System.out.println("Wrong message format or key detected: Found non alphabatic character");
                return "ERROR!!";
            }
            // performing xor modulo 26 lead to lossy encryption when i tried
            // There will be mapping to non alphabetical characters here 
            result.append((char)(((m.charAt(i) - 'a') ^ (k.charAt(i) - 'a')) + 'a')); 
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
            System.out.println("Enter the private key of length " + m.length() + ": ");
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

