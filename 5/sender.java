import java.io.*;
import java.net.*;
import java.math.*;
import java.util.*;

public class sender {
    static Random r = new Random();

    public static int findPrimitiveRoot(int p) {
        BigInteger bigP = BigInteger.valueOf(p);
        int phi = p - 1;
        // factorize phi
        List<Integer> factors = new ArrayList<>();
        int n = phi;
        for (int f = 2; f * f <= n; f++) {
            if (n % f == 0) {
                factors.add(f);
                while (n % f == 0) n /= f;
            }
        }
        if (n > 1) factors.add(n);

        for (int g = 2; g < p; g++) {
            boolean ok = true;
            BigInteger bigG = BigInteger.valueOf(g);
            for (int factor : factors) {
                BigInteger exp = BigInteger.valueOf(phi / factor);
                if (bigG.modPow(exp, bigP).equals(BigInteger.ONE)) {
                    ok = false;
                    break;
                }
            }
            if (ok) return g;
        }
        throw new RuntimeException("No primitive root found.");
    }

    // Encryption
    public static int[] encrypt(int Ya, int q, int alpha, int M) {
        int k = r.nextInt(q - 2) + 1; 
        BigInteger bigQ = BigInteger.valueOf(q);

        BigInteger Kbig = BigInteger.valueOf(Ya).modPow(BigInteger.valueOf(k), bigQ);
        int K = Kbig.intValue();
        int C1 = BigInteger.valueOf(alpha).modPow(BigInteger.valueOf(k), bigQ).intValue();
        int C2 = (int)( (BigInteger.valueOf(K).multiply(BigInteger.valueOf(M)).mod(bigQ)).intValue() );

        System.out.println("---- ENCRYPTION INTERMEDIATE ----");
        System.out.println("chosen ephemeral k = " + k);
        System.out.println("K = Ya^k mod q = " + K);
        System.out.println("C1 = alpha^k mod q = " + C1);
        System.out.println("C2 = (K * M) mod q = " + C2);
        System.out.println("---------------------------------");

        return new int[]{C1, C2};
    }

    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        return BigInteger.valueOf(n).isProbablePrime(20);
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;
        if (args.length >= 1) host = args[0];
        if (args.length >= 2) port = Integer.parseInt(args[1]);

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter prime q: ");
            int q = sc.nextInt();
            if (!isPrime(q)) {
                System.err.println("Error: q must be prime. Exiting.");
                return;
            }

            int alpha = findPrimitiveRoot(q);
            System.out.println("Selected primitive root alpha = " + alpha);

            System.out.printf("Connecting to server %s:%d ...\n", host, port);
            try (Socket socket = new Socket(host, port);
                 DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
                 DataInputStream din = new DataInputStream(socket.getInputStream())) {

                // sending q and alpha to server
                dout.writeInt(q);
                dout.writeInt(alpha);
                dout.flush();
                System.out.println("Sent q and alpha to server.");

                // waiting for Ya
                int Ya = din.readInt();
                System.out.println("Received Ya from server: " + Ya);

                System.out.print("Enter message M (< q): ");
                int M = sc.nextInt();
                if (M < 0 || M >= q) {
                    System.err.println("Message out of range. Exiting.");
                    return;
                }

                // encrypt and send cipher
                int[] cipher = encrypt(Ya, q, alpha, M);
                dout.writeInt(cipher[0]); // C1
                dout.writeInt(cipher[1]); // C2
                dout.flush();
                System.out.println("Cipher sent to server: (" + cipher[0] + ", " + cipher[1] + ")");

                // optionally wait for server ack or decrypted result printed by server
                System.out.println("Done. Closing connection.");
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (InputMismatchException ime) {
            System.err.println("Invalid input.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
