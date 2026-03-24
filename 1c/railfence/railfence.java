import java.util.*;
public class railfence {
    // I tried implementing a purely mathematical method here which saves space
    // there is a cycle that is followed with a +2 and -2 pattern to it as we go down the rails.
    // here i cannot generate an intermediate "rail" because of that
    public static String encrypt(String text, int key) {
        
        String pt = "";
        for(char c : text.toCharArray()){
            if(!Character.isAlphabetic(c) || Character.isWhitespace(c)){
                continue;
            }
            pt += Character.toLowerCase(c);
        }

        if (key <= 1 || key >= pt.length()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        int cycle = 2 * (key - 1);

        for (int rail = 0; rail < key; rail++) {
            int pos = rail;
            
            while (pos < pt.length()) {
                result.append(pt.charAt(pos));
                // cycle is uniform for first and last rail
                if (rail == 0 || rail == key - 1) {
                    pos += cycle;
                } else {
                    // here is just the +2/-2 logic 
                    int nextPos = pos + 2 * (key - 1 - rail);
                    if (nextPos < pt.length()) {
                        result.append(pt.charAt(nextPos));
                    }
                    pos += cycle;
                }
            }
        }
        
        return result.toString();
    }
    
    public static String decrypt(String ct, int key) {
        if (key <= 1 || key >= ct.length()) {
            return ct;
        }
        
        int len = ct.length();
        int cycle = 2 * (key - 1);
        char[] result = new char[len];
        int index = 0;
        
        for (int rail = 0; rail < key; rail++) {
            int pos = rail;
            
            while (pos < len) {
                result[pos] = ct.charAt(index++);
                
                if (rail == 0 || rail == key - 1) {
                    pos += cycle;
                } else {
                    int nextPos = pos + 2 * (key - 1 - rail);
                    if (nextPos < len && index < ct.length()) {
                        result[nextPos] = ct.charAt(index++);
                    }
                    pos += cycle;
                }
            }
        }
        
        return new String(result);
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter Plaintext: ");
        String plaintext = in.nextLine();
        System.out.println("Key: ");
        int key = in.nextInt();
        String encrypted = encrypt(plaintext, key);
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Key: " + key);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypt(encrypted, key));
        System.out.println();
    }
}
