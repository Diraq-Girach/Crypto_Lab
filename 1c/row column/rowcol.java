import java.util.*;

public class rowcol {
    // here i am validating the key
    // a key 3451 is not considered valid as 2 is missing and 5 is outside the length 4
    public static boolean isValidKey(int[] key) {
        if (key == null || key.length == 0) {
            return false;
        }
        boolean[] seen = new boolean[key.length + 1];
        for (int k : key) {
            // cheking if number within length
            if (k < 1 || k > key.length) {
                return false;
            }
            if (seen[k]) {
                return false;
            }
            seen[k] = true;
        } 
        for (int i = 1; i <= key.length; i++) {
            if (!seen[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static String encrypt(String text, int[] key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid key! Key must contain consecutive numbers from 1 to " + key.length);
        }
        
        String pt = "";
        for (char c : text.toCharArray()) {
            if (!Character.isAlphabetic(c) || Character.isWhitespace(c)) {
                continue;
            }
            pt += Character.toLowerCase(c);
        }
        
        int keyLen = key.length;
        int numRows = (int) Math.ceil((double) pt.length() / keyLen);
        
        char[][] matrix = new char[numRows][keyLen];
        int index = 0;
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < keyLen; j++) {
                if (index < pt.length()) {
                    matrix[i][j] = pt.charAt(index++);
                } else {
                    matrix[i][j] = 'X';  // Padding
                }
            }
        }
        
        System.out.println("The Matrix:...");
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j<keyLen; j++){
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println();
        }

        StringBuilder ct = new StringBuilder();
        for (int col = 1; col <= keyLen; col++) {
            for (int j = 0; j < keyLen; j++) {
                if (key[j] == col) { 
                    for (int i = 0; i < numRows; i++) {
                        ct.append(matrix[i][j]);
                    }
                    break;
                }
            }
        }
        
        return ct.toString();
    }
    
    public static String decrypt(String ct, int[] key) {
        if (!isValidKey(key)) {
            throw new IllegalArgumentException("Invalid key! Key must contain consecutive numbers from 1 to " + key.length);
        }
        
        int keyLen = key.length;
        int numRows = (int) Math.ceil((double) ct.length() / keyLen);
        
        char[][] matrix = new char[numRows][keyLen];
        int index = 0;
        
        for (int col = 1; col <= keyLen; col++) {
            for (int j = 0; j < keyLen; j++) {
                if (key[j] == col) {
                    for (int i = 0; i < numRows; i++) {
                        if (index < ct.length()) {
                            matrix[i][j] = ct.charAt(index++);
                        }
                    }
                    break;
                }
            }
        }
        
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < keyLen; j++) {
                char c = matrix[i][j];
                plaintext.append(c);
            }
        }
        
        return plaintext.toString();
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        System.out.println("Enter plaintext: ");
        String plaintext = in.nextLine();
        
        System.out.println("Enter key length: ");
        int keyLen = in.nextInt();
        
        int[] key = new int[keyLen];
        System.out.println("Enter key values: ");
        for (int i = 0; i < keyLen; i++) {
            key[i] = in.nextInt();
        }
        
        System.out.println("Key is valid!");
        String encrypted = encrypt(plaintext, key);
        System.out.println("Plaintext: " + plaintext);
        System.out.print("Key: ");
        for (int k : key) System.out.print(k + " ");
        System.out.println("\nEncrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypt(encrypted, key));
           
    }
}
