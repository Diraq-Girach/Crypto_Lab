import java.util.*;
import java.security.SecureRandom;
import java.sql.Savepoint;
import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class elgamalserver {
    static SecureRandom r = new SecureRandom();

    static int[] km(int q, int alpha){
        int Xa = r.nextInt(q-2)+1;
        BigInteger bq = BigInteger.valueOf(q);
        BigInteger bal = BigInteger.valueOf(alpha);
        int Ya = bal.modPow(BigInteger.valueOf(Xa), bq).intValue();
        return new int[]{Xa, Ya};
    }

    public static String decrypt(int C1, int C2, int Xa, int q){
        BigInteger bq = BigInteger.valueOf(q);
        BigInteger K = BigInteger.valueOf(C1).modPow(BigInteger.valueOf(Xa), bq);
        BigInteger Kinv = K.modInverse(bq);
        BigInteger mint = Kinv.multiply(BigInteger.valueOf(C2)).mod(bq);
        byte[] mbyte = mint.toByteArray();
        if (mbyte.length > 1 && mbyte[0] == 0){
            byte[] temp = new byte[mbyte.length-1];
            System.arraycopy(mbyte, 1, temp , 0 , temp.length);
            mbyte = temp;
        }
        return new String(mbyte);
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Server online...");
        try(
            ServerSocket ss = new ServerSocket(5000);
            Socket s = ss.accept();
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ){
            System.out.println("client connected");
            int q = dis.readInt();
            int alpha = dis.readInt();

            System.out.println("recieved alpha and q from client");

            int kp[] = km(q,alpha);
            int Xa = kp[0];
            int Ya = kp[1];

            System.out.println("generated key pairs");

            dos.writeInt(Ya);
            dos.flush();
            System.out.println("sent Ya to client");

            int C1 = dis.readInt();
            int C2 = dis.readInt();

            System.out.println("recieved C1 and C2");

            String m = decrypt(C1, C2, Xa, q);

            System.out.println("decrypted text: " + m);
        }
    }
}
