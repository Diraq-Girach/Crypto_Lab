import java.util.*;
import java.io.*;
import java.net.*;

class receiver {
    public static String decrypt(String ct, int key) {
        if (key <= 1 || key >= ct.length()) {
            return ct;
        }
        
        int len = ct.length();
        int cycle = 2 * (key - 1);
        char[] result = new char[len];
        int index = 0;
        
        for (int rail = 0; rail < key; rail++) {
            int pos = rail;
            
            while (pos < len) {
                result[pos] = ct.charAt(index++);
                
                if (rail == 0 || rail == key - 1) {
                    pos += cycle;
                } else {
                    int nextPos = pos + 2 * (key - 1 - rail);
                    if (nextPos < len && index < ct.length()) {
                        result[nextPos] = ct.charAt(index++);
                    }
                    pos += cycle;
                }
            }
        }
        
        return new String(result);
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

