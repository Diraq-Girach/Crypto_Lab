import java.util.*;
import java.security.*;
import java.math.*;
import java.io.*;
import java.net.*;

public class rsaserver {
    static BigInteger n,p,q,phi,e,d;
    static int bitlenth = 1024;
    static SecureRandom r = new SecureRandom();
    static void gk(){
        e = new BigInteger("65537");
        do {
            p = new BigInteger(bitlenth, 100, r);
            q = new BigInteger(bitlenth, 100, r);
            n = p.multiply(q);
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        }while(!e.gcd(phi).equals(BigInteger.ONE));
        
        d = e.modInverse(phi);
    }

    static BigInteger encrypt(BigInteger e, BigInteger n, String m) throws Exception{
        byte[] mbyte = m.getBytes();
        BigInteger mint = new BigInteger(1, mbyte);
        if(mint.compareTo(n)>= 0){
            throw new Exception("Too big");
        }
        return mint.modPow(e, n);
    }

    static String decrypt(BigInteger c) throws Exception{
        BigInteger dec = c.modPow(d, n);
        byte[] decbytes = dec.toByteArray();

        if(decbytes.length > 1 && decbytes[0] == 0){
            byte[] temp = new byte[decbytes.length - 1];
            System.arraycopy(decbytes, 1, temp, 0, temp.length);
            decbytes = temp;
        }

        return new String(decbytes);
    }


    public static void main(String[] args) throws Exception{
        gk();
        Scanner in = new Scanner(System.in);
        System.out.println("Server started");
        try(
            ServerSocket ss = new ServerSocket(5000);
            Socket s = ss.accept();
            DataInputStream din = new DataInputStream(s.getInputStream());
            DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        ){
            
            dout.writeUTF(e.toString());
            dout.writeUTF(n.toString());

            System.out.println("sent e and n to client for encryption");

            String c = din.readUTF();
            BigInteger cint = new BigInteger(c);

            String dec = decrypt(cint);

            System.out.println("Decrypted message:" + dec);

        }

    }
}
