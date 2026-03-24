import java.util.*; 
import java.math.*; 
public class bob { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
        Random rand = new Random(); 
        System.out.println("---- Bob ----"); 
        System.out.print("Enter prime number: "); 
        int p = sc.nextInt(); 
        System.out.print("Enter primitive root: "); 
        int g = sc.nextInt(); 
        int Xb = rand.nextInt(p - 2) + 1; 
        System.out.println("Private key (Xb): " + Xb); 
        BigInteger P = BigInteger.valueOf(p); 
        BigInteger G = BigInteger.valueOf(g); 
        BigInteger Yb = G.modPow(BigInteger.valueOf(Xb), P); 
        System.out.println("Public key (Yb): " + Yb); 
        System.out.print("Enter public key received from Attacker: "); 
        BigInteger Yd2 = sc.nextBigInteger(); 
        BigInteger Kb = Yd2.modPow(BigInteger.valueOf(Xb), P); 
        System.out.println("Common key between Bob and Attacker: " + Kb); 
    } 
}