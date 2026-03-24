import java.util.*;
import java.io.*;
import java.net.*;

class sender {
    public static String encrypt(String text, int key) {
        
        String pt = "";
        for(char c : text.toCharArray()){
            if(!Character.isAlphabetic(c) || Character.isWhitespace(c)){
                continue;
            }
            pt += Character.toLowerCase(c);
        }

        if (key <= 1 || key >= pt.length()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        int cycle = 2 * (key - 1);

        for (int rail = 0; rail < key; rail++) {
            int pos = rail;
            
            while (pos < pt.length()) {
                result.append(pt.charAt(pos));
                // cycle is uniform for first and last rail
                if (rail == 0 || rail == key - 1) {
                    pos += cycle;
                } else {
                    // here is just the +2/-2 logic 
                    int nextPos = pos + 2 * (key - 1 - rail);
                    if (nextPos < pt.length()) {
                        result.append(pt.charAt(nextPos));
                    }
                    pos += cycle;
                }
            }
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

