import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Scanner;

public class rsaclient {
    public static BigInteger encrypt(BigInteger e, BigInteger n, String m) throws Exception{
        byte[] mbyte = m.getBytes();
        BigInteger mint = new BigInteger(mbyte);

        if(mint.compareTo(n)>= 0){
            throw new Exception("Loda");
        }
        return mint.modPow(e, n);
    }

    public static void main(String[] args) throws Exception{
        Scanner in = new Scanner(System.in);
        try(
            Socket s = new Socket("localhost", 5000);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        ){
            System.out.println("conncted ro server");
            String es = dis.readUTF();
            String ns = dis.readUTF();

            BigInteger e = new BigInteger(es);
            BigInteger n = new BigInteger(ns);

            System.out.println("recieved e and n");

            String m = in.nextLine();
            BigInteger c = encrypt(e, n, m);

            System.out.println("cyphertext is: " + c);

            dos.writeUTF(c.toString());
            System.out.println("cypher sent to server");
        }
    }
}
