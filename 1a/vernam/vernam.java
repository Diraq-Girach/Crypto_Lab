import java.util.*;

public class vernam {
    public static String encrypt(String a, String b){
        // NOT handling spaces because not sure how to deal with that as the key needs to be the same length
        String m = a.toLowerCase();
        String k = b.toLowerCase();
        if(m.length() != k.length()){
            System.out.println("Wrong key detected: Length mismatch");
            return "ERROR!!";
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0; i<m.length(); i++){
            if(!Character.isAlphabetic(m.charAt(i)) || !Character.isAlphabetic(m.charAt(i))){
                System.out.println("Wrong message format or key detected: Found non alphabatic character");
                return "ERROR!!";
            }
            // performing xor modulo 26 lead to lossy encryption when i tried
            // There will be mapping to non alphabetical characters here 
            result.append((char)(((m.charAt(i) - 'a') ^ (k.charAt(i) - 'a'))+ 'a')); 
        }
        return result.toString();
    }

    public static String decrypt(String a, String b){
        String c = a.toLowerCase();
        String k = b.toLowerCase();
        if(c.length() != k.length()){
            System.out.println("Wrong key detected: Length mismatch");
            return "ERROR!!";
        }
        StringBuilder result = new StringBuilder();
        for(int i = 0; i<c.length(); i++){
            result.append((char)(((c.charAt(i) - 'a') ^ (k.charAt(i) - 'a'))+ 'a')); 
        }
        return result.toString();
    }
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the message:");
        String m = in.nextLine();
        System.out.printf("Enter the key of length %d:\n", m.length());
        String k = in.nextLine();

        String c = encrypt(m, k);
        System.out.println("The encrypted text is...: " + c);
        String pt = decrypt(c, k);
        System.out.println("The decrypted text is...: " + pt);


    }
}
