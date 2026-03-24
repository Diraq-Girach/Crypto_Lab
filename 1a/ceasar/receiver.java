import java.util.*;
import java.io.*;
import java.net.*;

class receiver {

    public static String decrypt(String s, int k){
        StringBuilder res = new StringBuilder();
        for(char c : s.toCharArray()){
            if(!Character.isAlphabetic(c)){
                res.append(c);
                continue;
            }
            res.append((char)((c - 'a' - k + 26 )%26 + 'a'));
        }
        return res.toString();
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
            int k = in.nextInt();
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

