import java.util.*; 
import java.math.*; 
public class alice { 
    public static void main(String[] args) { 
        Scanner sc = new Scanner(System.in); 
        Random rand = new Random(); 
        System.out.println("---- Alice ----"); 
        System.out.print("Enter prime number: "); 
        int p = sc.nextInt(); 
        System.out.print("Enter primitive root: "); 
        int g = sc.nextInt(); 
        int Xa = rand.nextInt(p - 2) + 1; 
        System.out.println("Private key (Xa): " + Xa); 
        BigInteger P = BigInteger.valueOf(p); 
        BigInteger G = BigInteger.valueOf(g); 
        BigInteger Ya = G.modPow(BigInteger.valueOf(Xa), P); 
        System.out.println("Public key (Ya): " + Ya); 
        System.out.print("Enter public key received from Attacker: "); 
        BigInteger Yd1 = sc.nextBigInteger(); 
        BigInteger Ka = Yd1.modPow(BigInteger.valueOf(Xa), P); 
        System.out.println("Common key between Alice and Attacker: " + Ka); 
    } 
}