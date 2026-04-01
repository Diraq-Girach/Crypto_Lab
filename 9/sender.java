import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

public class sender {
    public static void main(String[] args) throws Exception {
        SecureRandom random = new SecureRandom();

        // 1. Generate DSS Parameters
        BigInteger q = new BigInteger(160, 50, random); // 160-bit prime
        
        BigInteger p, kMultiplier;
        do {
            kMultiplier = new BigInteger(512 - 160, random);
            p = kMultiplier.multiply(q).add(BigInteger.ONE);
        } while (!p.isProbablePrime(50)); // 512-bit prime

        BigInteger h, g;
        do {
            h = new BigInteger(511, random);
            g = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        } while (g.compareTo(BigInteger.ONE) <= 0); // Generator

        // 2. Generate Keys
        BigInteger x = new BigInteger(159, random); // Private key
        BigInteger y = g.modPow(x, p);              // Public key

        // 3. Define the Message
        System.out.println("enter message:");
        Scanner in = new Scanner(System.in);
        String message = in.nextLine();
        System.out.println("select 1 to simulate authentic message, 0 for otherwise:");
        int ch = in.nextInt();
        if(ch == 0){
            x = x.add(BigInteger.ONE);
        }

        System.out.println("--- Sender (Client) ---");
        System.out.println("Message: " + message);

        // 4. Call the explicit sign function
        // Returns an array where index 0 is 'r' and index 1 is 's'
        BigInteger[] signature = sign(message, p, q, g, x);
        BigInteger r = signature[0];
        BigInteger s = signature[1];

        System.out.println("Generated Signature (r): " + r);
        System.out.println("Generated Signature (s): " + s);

        // 5. Connect to Receiver and transmit data
        try (Socket socket = new Socket("localhost", 5000);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            
            out.writeObject(p);
            out.writeObject(q);
            out.writeObject(g);
            out.writeObject(y);
            out.writeObject(message);
            out.writeObject(r);
            out.writeObject(s);
            
            System.out.println("\nPayload securely sent to the receiver.");
        }
    }

    public static BigInteger[] sign(String message, BigInteger p, BigInteger q, BigInteger g, BigInteger x) throws Exception {
        SecureRandom random = new SecureRandom();
        
        // Hashing the message (SHA-256)
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger hash = new BigInteger(1, md.digest(message.getBytes()));

        BigInteger k, r, s;
        do {
            // Random per-message key (0 < k < q)
            k = new BigInteger(159, random);
            
            // r = (g^k mod p) mod q
            r = g.modPow(k, p).mod(q);
            
            // s = (k^-1 * (H(m) + x*r)) mod q
            BigInteger kInv = k.modInverse(q);
            s = kInv.multiply(hash.add(x.multiply(r))).mod(q);
            
        // Ensure neither r nor s are zero (extremely rare, but required by DSS standard)
        } while (r.equals(BigInteger.ZERO) || s.equals(BigInteger.ZERO));

        return new BigInteger[]{r, s};
    }
}