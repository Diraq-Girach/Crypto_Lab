import java.util.*;
import java.io.*;
import java.net.*;

class sender {
    private static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    private static final int[] P4 = {2, 4, 3, 1};
    private static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int[] IP_INV = {4, 1, 3, 5, 7, 2, 8, 6};
    private static final int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
    
    private static final int[][] S0 = {
        {1, 0, 3, 2},
        {3, 2, 1, 0},
        {0, 2, 1, 3},
        {3, 1, 3, 2}
    };
    
    private static final int[][] S1 = {
        {0, 1, 2, 3},
        {2, 0, 1, 3},
        {3, 0, 1, 0},
        {2, 1, 0, 3}
    };

    private static int[] fk(int[] left, int[] right, int[] subkey) {
        int[] expanded = permute(right, EP);
        
        int[] xorResult = new int[8];
        for (int i = 0; i < 8; i++) {
            xorResult[i] = expanded[i] ^ subkey[i];
        }
        
        int[] leftHalf = new int[4];
        int[] rightHalf = new int[4];
        System.arraycopy(xorResult, 0, leftHalf, 0, 4);
        System.arraycopy(xorResult, 4, rightHalf, 0, 4);
        
        int s0Output = sbox(leftHalf, S0);
        int s1Output = sbox(rightHalf, S1);
        
        int[] sboxResult = new int[4];
        sboxResult[0] = (s0Output >> 1) & 1;
        sboxResult[1] = s0Output & 1;
        sboxResult[2] = (s1Output >> 1) & 1;
        sboxResult[3] = s1Output & 1;
        
        int[] p4Result = permute(sboxResult, P4);
        
        int[] newRight = new int[4];
        for (int i = 0; i < 4; i++) {
            newRight[i] = left[i] ^ p4Result[i];
        }
        
        int[] result = new int[8];
        System.arraycopy(newRight, 0, result, 0, 4);
        System.arraycopy(right, 0, result, 4, 4);
        
        return result;
    }
    
    // S-box lookup logic
    private static int sbox(int[] input, int[][] sbox) {
        int row = (input[0] << 1) | input[3];
        int col = (input[1] << 1) | input[2];
        return sbox[row][col];
    }
    
    // Permutation function
    private static int[] permute(int[] input, int[] table) {
        int[] output = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            output[i] = input[table[i] - 1];
        }
        return output;
    }
    
    // Left shift function
    private static int[] leftShift(int[] input, int shifts) {
        int[] output = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = input[(i + shifts) % input.length];
        }
        return output;
    }
    
    // function to print binary array
    private static void printBinary(String label, int[] binary) {
        System.out.print(label + " ");
        for (int bit : binary) {
            System.out.print(bit);
        }
        System.out.println();
    }

    private static int[][] keyGeneration(int[] key) {
        int[] p10Result = permute(key, P10);

        int[] left = new int[5];
        int[] right = new int[5];
        System.arraycopy(p10Result, 0, left, 0, 5);
        System.arraycopy(p10Result, 5, right, 0, 5);
        
        int[] ls1Left = leftShift(left, 1);
        int[] ls1Right = leftShift(right, 1);
        
        int[] combined1 = new int[10];
        System.arraycopy(ls1Left, 0, combined1, 0, 5);
        System.arraycopy(ls1Right, 0, combined1, 5, 5);
        int[] k1 = permute(combined1, P8);
        
        // Shifting the LS1 result by 2 more 
        int[] ls3Left = leftShift(ls1Left, 2);
        int[] ls3Right = leftShift(ls1Right, 2);
        
        int[] combined2 = new int[10];
        System.arraycopy(ls3Left, 0, combined2, 0, 5);
        System.arraycopy(ls3Right, 0, combined2, 5, 5);
        int[] k2 = permute(combined2, P8);
        
        return new int[][]{k1, k2};
    }
    
    public static int[] encrypt(int[] plaintext, int[] key) {
        System.out.println("\nEncryption Process");

        int[][] keys = keyGeneration(key);
        int[] k1 = keys[0];
        int[] k2 = keys[1];
        printBinary("K1", k1);
        printBinary("K2", k2);

        int[] ip = permute(plaintext, IP);
        printBinary("After IP", ip);

        int[] left = new int[4];
        int[] right = new int[4];
        System.arraycopy(ip, 0, left, 0, 4);
        System.arraycopy(ip, 4, right, 0, 4);
        
        // Round 1
        System.out.println("\nRound 1");
        int[] fkResult1 = fk(left, right, k1);
        printBinary("After Round 1", fkResult1);
        
        //swapping
        int[] leftAfterSwap = new int[4];
        int[] rightAfterSwap = new int[4];
        System.arraycopy(fkResult1, 4, leftAfterSwap, 0, 4);
        System.arraycopy(fkResult1, 0, rightAfterSwap, 0, 4);
        
        int[] afterSwap = new int[8];
        System.arraycopy(leftAfterSwap, 0, afterSwap, 0, 4);
        System.arraycopy(rightAfterSwap, 0, afterSwap, 4, 4);
        printBinary("After Swap", afterSwap);
        

        // Round 2
        System.out.println("\nRound 2");
        int[] fkResult2 = fk(leftAfterSwap, rightAfterSwap, k2);
        printBinary("After Round 2", fkResult2);

        // Inverse initial permutation
        int[] ciphertext = permute(fkResult2, IP_INV);
        printBinary("After IP_INV", ciphertext);
        
        return ciphertext;
    }

    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;
        Scanner in = new Scanner(System.in);
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to receiver!");
            
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            System.out.println("Enter the plaintext in binary with spaces:");
            int []plaintext = new int[8];
            int []key = new int[10];

            for(int i = 0; i<8; i++){
                plaintext[i] = in.nextInt();
            }

            System.out.println("Enter the key in binary with spaces:");

            for(int i = 0; i<10; i++){
                key[i] = in.nextInt();
            }

            printBinary("Plaintext", plaintext);
            printBinary("Key", key);
            
            int[] c = encrypt(plaintext, key);
            printBinary("The encrypted text is...: ", c);
            output.writeUTF(Arrays.toString(c));
            System.out.println("Cypher sent.");
            output.flush();
        
            output.close();
            socket.close();
            System.out.println("Connection closed");
            
        } catch (IOException e) {
            System.out.println("Sender exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

