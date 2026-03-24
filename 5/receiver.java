import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;

public class receiver {
    static Random r = new Random();

    // keymaker
    public static int[] keymaker(int q, int alpha) {
        int Xa = r.nextInt(q - 2) + 1; 
        BigInteger bigQ = BigInteger.valueOf(q);
        int Ya = BigInteger.valueOf(alpha).modPow(BigInteger.valueOf(Xa), bigQ).intValue();
        return new int[]{Xa, Ya};
    }

    // decryption
    public static int decrypt(int C1, int C2, int Xa, int q) {
        BigInteger bigQ = BigInteger.valueOf(q);
        BigInteger Kbig = BigInteger.valueOf(C1).modPow(BigInteger.valueOf(Xa), bigQ);
        BigInteger Kinv = Kbig.modInverse(bigQ);
        BigInteger Mbig = BigInteger.valueOf(C2).multiply(Kinv).mod(bigQ);
        System.out.println("---- DECRYPTION INTERMEDIATE ----");
        System.out.println("Computed K = C1^Xa mod q = " + Kbig.intValue());
        System.out.println("K inverse mod q = " + Kinv.intValue());
        System.out.println("---------------------------------");
        return Mbig.intValue();
    }

    public static void main(String[] args) {
        int port = 5000;
        if (args.length >= 1) port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Receiver: listening on port " + port + " ...");
            try (Socket client = serverSocket.accept();
                 DataInputStream din = new DataInputStream(client.getInputStream());
                 DataOutputStream dout = new DataOutputStream(client.getOutputStream())) {

                System.out.println("Sender connected from " + client.getRemoteSocketAddress());

                // read q and alpha
                int q = din.readInt();
                int alpha = din.readInt();
                System.out.println("Received q = " + q + ", alpha = " + alpha + " from sender.");

                int[] keypair = keymaker(q, alpha);
                int Xa = keypair[0];
                int Ya = keypair[1];
                System.out.println("Generated secret Xa = " + Xa + " and public Ya = " + Ya);

                // sending Ya to client
                dout.writeInt(Ya);
                dout.flush();
                System.out.println("Sent Ya to sender.");

                // waiting for cipher
                int C1 = din.readInt();
                int C2 = din.readInt();
                System.out.println("Received cipher from sender: (" + C1 + ", " + C2 + ")");

                // decrypt
                int M = decrypt(C1, C2, Xa, q);
                System.out.println("Decrypted message M = " + M);

                System.out.println("Receiver done. Closing connection.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
