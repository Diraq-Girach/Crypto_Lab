import java.net.*;
import java.util.*;
import java.io.DataInputStream;
import java.math.*;
import java.security.*;

class server {
    static String decrypt(String m, int k){
        StringBuilder res = new StringBuilder();
        for(char c : m.toCharArray()){
            if(!Character.isAlphabetic(c)){
                res.append(c);
                continue;
            }
            res.append((char)((c - 'a' - k + 26) % 26 + 'a'));
        }
        return res.toString();
    }
    public static void main(String[] args) throws Exception{
        System.out.println("Waiting for connection on port 5000...");
        Scanner in = new Scanner(System.in);
        try(ServerSocket serverSocket = new ServerSocket(5000);
            Socket socket = serverSocket.accept();
            DataInputStream din = new DataInputStream(socket.getInputStream())){
                System.out.println("Sender connected!\nPlease enter the secret key: ");
                //in.nextLine();
                int k = in.nextInt();
                String s = din.readUTF();
                System.out.println("received: "+s);
                String dec = decrypt(s, k);
                System.out.println("Ceasar decrypted = " + dec);
            }
    }
}
