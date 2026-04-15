import java.util.*;
import java.security.SecureRandom;
import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class elgamalclient{
    static SecureRandom r = new SecureRandom();
    static int fpr(int p) throws Exception{
        BigInteger bp = BigInteger.valueOf(p);
        int phi = p-1;
        int n = phi;
        List<Integer> factors = new ArrayList<>();
        for (int f = 2; f*f <=n; f++){
            if(n % f == 0){
                factors.add(f);
                while (n%f == 0) n/=f;
            }
        }
        if(n>1) factors.add(n);

        for(int g = 2; g<p; g++){
            boolean ok = true;
            BigInteger bg = BigInteger.valueOf(g);
            for (int factor : factors){
                BigInteger exp = BigInteger.valueOf(phi/factor);
                if(bg.modPow(exp, bp).equals(BigInteger.ONE)){
                    ok = false;
                    break;
                }
            }
            if (ok) return g;
        }
        throw new Exception("No pimitive roots");
    }

    static int[] encrypt(int Ya, int q, int alpha, String m) throws Exception{
        byte[] mbyte = m.getBytes();
        BigInteger bm = new BigInteger(1,mbyte);
        if (bm.compareTo(BigInteger.valueOf(q)) >= 0){
            throw new Exception("give smaller m or larger q");
        }

        int k = r.nextInt(q-2)+1;
        BigInteger bk = BigInteger.valueOf(k);
        BigInteger bq = BigInteger.valueOf(q);
        BigInteger bal = BigInteger.valueOf(alpha);
        BigInteger bYa = BigInteger.valueOf(Ya);

        BigInteger K = bYa.modPow(bk, bq);
        int C1 = bal.modPow(bk, bq).intValue();
        int C2 = K.multiply(bm).mod(bq).intValue();

        return new int[]{C1, C2};
    }

    static boolean isprime(int p){
        if(p<=1){return false;}
        return BigInteger.valueOf(p).isProbablePrime(100);
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        try(
            Socket s = new Socket("localhost", 5000);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ){
            System.out.println("connected to server...\nPlease enter a valid prime number:");
            int q = in.nextInt();
            if(!isprime(q)){
                throw new Exception("Lode");
            }
            int alpha = fpr(q);
            System.out.println("found primitive root as: "+alpha);

            dos.writeInt(q);
            dos.writeInt(alpha);
            dos.flush();

            int Ya = dis.readInt();
            System.out.println("Received the Ya: " + Ya + "\nwrite the message");
            in.nextLine();
            String m = in.nextLine();

            int[] c = encrypt(Ya, q, alpha, m);

            dos.writeInt(c[0]);
            dos.writeInt(c[1]);

            System.out.println("sent C to server\nC1: " + c[0] + "\nC2: " + c[1]);
        }
    }
}