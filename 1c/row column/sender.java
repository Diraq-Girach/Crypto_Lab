import java.util.*;
import java.io.*;
import java.net.*;

class sender {
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
    
    public static String encrypt(String text, int[] key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid key! Key must contain consecutive numbers from 1 to " + key.length);
        }
        
        String pt = "";
        for (char c : text.toCharArray()) {
            if (!Character.isAlphabetic(c) || Character.isWhitespace(c)) {
                continue;
            }
            pt += Character.toLowerCase(c);
        }
        
        int keyLen = key.length;
        int numRows = (int) Math.ceil((double) pt.length() / keyLen);
        
        char[][] matrix = new char[numRows][keyLen];
        int index = 0;
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < keyLen; j++) {
                if (index < pt.length()) {
                    matrix[i][j] = pt.charAt(index++);
                } else {
                    matrix[i][j] = 'X';  // Padding
                }
            }
        }
        
        System.out.println("The Matrix:...");
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j<keyLen; j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println();
        }

        StringBuilder ct = new StringBuilder();
        for (int col = 1; col <= keyLen; col++) {
            for (int j = 0; j < keyLen; j++) {
                if (key[j] == col) { 
                    for (int i = 0; i < numRows; i++) {
                        ct.append(matrix[i][j]);
                    }
                    break;
                }
            }
        }
        
        return ct.toString();
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
            System.out.println("Enter the private key length:");
            int klen = in.nextInt();
            System.out.println("Enter the private key one by one with space:");
            int k[] = new int[klen];
            for(int i = 0; i<klen; i++){
                k[i] = in.nextInt();
            }
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

