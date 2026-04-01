import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.security.MessageDigest;

public class receiver {
    public static void main(String[] args) throws Exception {
        System.out.println("--- Receiver (Server) ---");
        System.out.println("Waiting for connection on port 5000...");

        // Start server socket
        try (ServerSocket serverSocket = new ServerSocket(5000);
             Socket socket = serverSocket.accept();
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            
            System.out.println("Sender connected!\n");

            // 1. Receive DSS parameters, public key, message, and signature
            BigInteger p = (BigInteger) in.readObject();
            BigInteger q = (BigInteger) in.readObject();
            BigInteger g = (BigInteger) in.readObject();
            BigInteger y = (BigInteger) in.readObject();
            String message = (String) in.readObject();
            BigInteger r = (BigInteger) in.readObject();
            BigInteger s = (BigInteger) in.readObject();

            System.out.println("Received Message: " + message);
            System.out.println("Received Signature (r): " + r);
            System.out.println("Received Signature (s): " + s);

            // 2. Verify Signature
            boolean isValid = verify(p, q, g, y, message, r, s);
            
            if (isValid) {
                System.out.println("\nSUCCESS: Digital Signature Verified. The message is authentic.");
            } else {
                System.out.println("\nFAILURE: Digital Signature is INVALID.");
            }
        }
    }

    // Standard DSS Verification Logic
    public static boolean verify(BigInteger p, BigInteger q, BigInteger g, BigInteger y, 
                                 String message, BigInteger r, BigInteger s) throws Exception {
        // Step A: Checking bounds 0 < r < q and 0 < s < q
        if (r.compareTo(BigInteger.ZERO) <= 0 || r.compareTo(q) >= 0 ||
            s.compareTo(BigInteger.ZERO) <= 0 || s.compareTo(q) >= 0) {
            return false;
        }

        // Step B: Hashing the message (SHA-256)
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        BigInteger hash = new BigInteger(1, md.digest(message.getBytes()));

        // Step C: w = s^-1 mod q
        BigInteger w = s.modInverse(q);

        // Step D: u1 = (H(m) * w) mod q
        BigInteger u1 = hash.multiply(w).mod(q);

        // Step E: u2 = (r * w) mod q
        BigInteger u2 = r.multiply(w).mod(q);

        // Step F: v = ((g^u1 * y^u2) mod p) mod q
        BigInteger v1 = g.modPow(u1, p);
        BigInteger v2 = y.modPow(u2, p);
        BigInteger v = v1.multiply(v2).mod(p).mod(q);

        // Step G: Signature is valid if v == r
        return v.equals(r);
    }
}