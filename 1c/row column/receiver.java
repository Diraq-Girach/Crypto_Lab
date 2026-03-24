import java.util.*;
import java.io.*;
import java.net.*;

class receiver {
    public static boolean isValidKey(int[] key) {
        if (key == null || key.length == 0) {
            return false;
        }
        boolean[] seen = new boolean[key.length + 1];
        for (int k : key) {
            // cheking if number within length
            if (k < 1 || k > key.length) {
                return false;
            }
            if (seen[k]) {
                return false;
            }
            seen[k] = true;
        } 
        for (int i = 1; i <= key.length; i++) {
            if (!seen[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static String decrypt(String ct, int[] key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid key! Key must contain consecutive numbers from 1 to " + key.length);
        }
        
        int keyLen = key.length;
        int numRows = (int) Math.ceil((double) ct.length() / keyLen);
        
        char[][] matrix = new char[numRows][keyLen];
        int index = 0;
        
        for (int col = 1; col <= keyLen; col++) {
            for (int j = 0; j < keyLen; j++) {
                if (key[j] == col) {
                    for (int i = 0; i < numRows; i++) {
                        if (index < ct.length()) {
                            matrix[i][j] = ct.charAt(index++);
                        }
                    }
                    break;
                }
            }
        }
        
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < keyLen; j++) {
                char c = matrix[i][j];
                plaintext.append(c);
            }
        }
        return plaintext.toString();
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
            System.out.println("Cypher recieved: " + c + "\nEnter the secret key for length decryption:");
            int klen = in.nextInt();
            System.out.println("Enter the private key one by one with space:");
            int k[] = new int[klen];
            for(int i = 0; i<klen; i++){
                k[i] = in.nextInt();
            }
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

