import java.util.*;
import java.math.*;
public class elgamal {
    static int q = 19;
    static int al = 10;

    static Random r = new Random();

    public static int[] keymaker(int q, int al){
        int Xa = r.nextInt(q-2)+1;
        BigInteger bigQ = BigInteger.valueOf(q);
        int Ya = BigInteger.valueOf(al).modPow(BigInteger.valueOf(Xa), bigQ).intValue();
        int [] res = {Xa,Ya};
        return res;
    }

    public static int[] encrypt(int Ya, int q, int al, int M){
        int k = r.nextInt(q-2)+1;
        BigInteger bigQ = BigInteger.valueOf(q);
        int K = BigInteger.valueOf(Ya).modPow(BigInteger.valueOf(k), bigQ).intValue();
        int C1 = BigInteger.valueOf(al).modPow(BigInteger.valueOf(k), bigQ).intValue();
        int C2 = (K*M)%q;
        int[] res = {C1,C2};
        return res;
    }

    public static int decrypt(int[] carr, int Xa, int q , int al){
        //int K = (int)Math.pow((double)carr[0], (double)Xa)%q;
        BigInteger Kbig = BigInteger.valueOf(carr[0]).modPow(BigInteger.valueOf(Xa), BigInteger.valueOf(q));
        BigInteger Kinv = Kbig.modInverse(BigInteger.valueOf(q));
        int Kin = Kinv.intValue();
        int M = (carr[1]*Kin)%q;
        return M;
    }

    public static void main(String[] args){
        System.out.println("Initialising...");
        System.out.printf("Q is : %d and the alpha value is %d\n", q, al);
        int[] keypair = keymaker(q,al);
        System.out.println("Enter Message: ");
        Scanner in = new Scanner(System.in);
        int m = in.nextInt();
        if(m > q) System.err.println("Message too Big");
        int[] cypher = encrypt(keypair[1], q ,al, m);
        System.out.println("Cypher sent is: " + cypher[0] + " and " + cypher[1]);
        int decm = decrypt(cypher, keypair[0], q, al);
        System.out.println("Decrypted text is: " + decm);
        System.out.printf("did the code work???? the answer is : %b", decm == m);
    }
}
