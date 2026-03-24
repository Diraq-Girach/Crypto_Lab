import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

public class rsa {
    private BigInteger p, q, n, phi, e, d;
    private int bitLength = 1024;
    private SecureRandom r;

    public rsa() {
        r = new SecureRandom();
        e = new BigInteger("65537");
        // We ensuring gcd(e, phi) == 1
        do {
            p = new BigInteger(bitLength, 100, r);
            q = new BigInteger(bitLength, 100, r);
            
            n = p.multiply(q);
            
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            
        } while (!e.gcd(phi).equals(BigInteger.ONE)); // Ensuring e and phi are coprime

        d = e.modInverse(phi);
    }

    // Encrypt function
    public BigInteger encrypt(String message) {
        //string -> bytes -> BigInteger
        byte[] messageBytes = message.getBytes();
        BigInteger messageInt = new BigInteger(1, messageBytes);
        
        // checking message length
        if (messageInt.compareTo(n) >= 0) {
            throw new IllegalArgumentException("Message is too long for the key size.");
        }
        
        return messageInt.modPow(e, n);
    }

    // Decrypt function
    public String decrypt(BigInteger ciphertext) {
        BigInteger decryptedInt = ciphertext.modPow(d, n);
        byte[] decryptedBytes = decryptedInt.toByteArray();
        
        // Removing the sign byte if present 
        if (decryptedBytes[0] == 0 && decryptedBytes.length > 1) {
             byte[] temp = new byte[decryptedBytes.length - 1];
             System.arraycopy(decryptedBytes, 1, temp, 0, temp.length);
             decryptedBytes = temp;
        }

        return new String(decryptedBytes);
    }

    public void printKeysAndOutput(String originalMessage, BigInteger encrypted, String decrypted) {
        System.out.println("\nO/p:\n");
        System.out.println("p value: " + p);
        System.out.println("---------------------------------------------------");
        System.out.println("q value: " + q);
        System.out.println("---------------------------------------------------");
        System.out.println("n=p.q: " + n);
        System.out.println("---------------------------------------------------");
        System.out.println("phi(n): " + phi);
        System.out.println("---------------------------------------------------");
        System.out.println("e: " + e);
        System.out.println("---------------------------------------------------");
        System.out.println("d: " + d);
        System.out.println("encrypted text: " + encrypted);
        System.out.println("decrypted text: " + decrypted);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        rsa rsa1 = new rsa();

        System.out.println("I/p");
        System.out.print("Message: ");
        String message = scanner.nextLine();

        BigInteger encryptedMsg = rsa1.encrypt(message);
        String decryptedMsg = rsa1.decrypt(encryptedMsg);

        rsa1.printKeysAndOutput(message, encryptedMsg, decryptedMsg);
        scanner.close();
    }
}