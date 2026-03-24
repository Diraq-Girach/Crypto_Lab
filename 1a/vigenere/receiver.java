import java.util.*;
import java.io.*;
import java.net.*;

class receiver {

    public static String decrypt(String a, String b){
        String c = a.toLowerCase();
        String k = b.toLowerCase();
        StringBuilder result = new StringBuilder();
        int mul = c.length()/k.length();
        String fk = k.repeat(mul+1);
        int keyindex = 0;
        for(int i = 0; i<c.length(); i++){
            if(!Character.isAlphabetic(c.charAt(i))){
                result.append(c.charAt(i));
                continue;
            }
            result.append((char)(((c.charAt(i) - 'a') - (fk.charAt(keyindex++) - 'a') + 26 )%26+'a'));
        }
        
        return result.toString();
    }
    public static void main(String[] args) {
        int port = 5000;
        Scanner in = new Scanner(System.in);
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Receiver is listening on port " + port);
            
            Socket socket = serverSocket.accept();
            System.out.println("Sender connected!");
            
            DataInputStream input = new DataInputStream(socket.getInputStream());
                        
            String c = input.readUTF();
            System.out.println("Cypher recieved: " + c + "\nEnter the secret key for decryption:");
            String k = in.nextLine();
            String pt = decrypt(c, k);
            System.out.println("Decrypted text: "+ pt);
            input.close();
            socket.close();
            serverSocket.close();
            System.out.println("Connection closed");
            
        } catch (IOException e) {
            System.out.println("Receiver exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

