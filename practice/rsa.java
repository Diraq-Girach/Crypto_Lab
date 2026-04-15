import java.util.*;
import java.security.*;
import java.math.*;


public class rsa {
    static BigInteger n,p,q,phi,e,d;
    static int bitlenth = 1024;
    static SecureRandom r = new SecureRandom();
    static void gk(){
        e = new BigInteger("65537");
        do{
            p = new BigInteger(bitlenth, 100, r);
            q = new BigInteger(bitlenth, 100, r);

            n = p.multiply(q);
            phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        }while(!e.gcd(phi).equals(BigInteger.ONE));
        d = e.modInverse(phi);
    }

    static BigInteger encrypt(BigInteger e, BigInteger n, String m) throws Exception{
        byte[] mbytes = m.getBytes();
        BigInteger mint= new BigInteger(1,mbytes);
        if (mint.compareTo(n) >= 0){
            throw new Exception("Beeg");
        }
        return mint.modPow(e, n);
    }

    static String decrypt(BigInteger cypher) throws Exception{
        BigInteger dec = cypher.modPow(d, n);
        byte[] decbytes = dec.toByteArray();
        if(decbytes.length>1 && decbytes[0] == 0){
            byte[] temp = new byte[decbytes.length -1];
            System.arraycopy(decbytes, 1, temp, 0, temp.length);
            decbytes = temp;
        }
        return new String(decbytes);
    }
    public static void main(String[] args) throws Exception{
        gk();
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a message in a line: ");
        String m = in.nextLine();
        BigInteger c = encrypt(e,n,m);
        System.out.println("Cypher text is: " + c);
        String dec = decrypt(c);
        System.out.println("Decrypted Text is: " + dec);
    }    
}
