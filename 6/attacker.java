import java.util.*; 
import java.math.*; 
public class attacker { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
        Random rand = new Random(); 
        System.out.println("---- Attacker ----"); 
        System.out.print("Enter prime number: "); 
        int p = sc.nextInt(); 
        System.out.print("Enter primitive root: "); 
        int g = sc.nextInt(); 
        int Xd1 = rand.nextInt(p - 2) + 1; 
        int Xd2 = rand.nextInt(p - 2) + 1; 
        System.out.println("Private key with Alice (Xd1): " + Xd1); 
        System.out.println("Private key with Bob (Xd2): " + Xd2); 
        BigInteger P = BigInteger.valueOf(p); 
        BigInteger G = BigInteger.valueOf(g); 
        BigInteger Yd1 = G.modPow(BigInteger.valueOf(Xd1), P); 
        BigInteger Yd2 = G.modPow(BigInteger.valueOf(Xd2), P); 
        System.out.println("Public key sent to Alice (Yd1): " + Yd1); 
        System.out.println("Public key sent to Bob (Yd2): " + Yd2); 
        System.out.print("Enter Alice public key (Ya): "); 
        BigInteger Ya = sc.nextBigInteger(); 
        System.out.print("Enter Bob public key (Yb): "); 
        BigInteger Yb = sc.nextBigInteger(); 
        BigInteger K1 = Ya.modPow(BigInteger.valueOf(Xd1), P); 
        BigInteger K2 = Yb.modPow(BigInteger.valueOf(Xd2), P); 
        System.out.println("Common key between Alice and Attacker: " + K1); 
        System.out.println("Common key between Bob and Attacker: " + K2); 
        } 
} 