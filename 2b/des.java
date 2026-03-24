import java.util.*;

public class des {
    // Initial Permutation (IP)
    private static final int[] IP = {
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    };
    
    // Final Permutation (IP^-1)
    private static final int[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    };
    
    // Expansion Permutation (E)
    private static final int[] E = {
        32, 1, 2, 3, 4, 5,
        4, 5, 6, 7, 8, 9,
        8, 9, 10, 11, 12, 13,
        12, 13, 14, 15, 16, 17,
        16, 17, 18, 19, 20, 21,
        20, 21, 22, 23, 24, 25,
        24, 25, 26, 27, 28, 29,
        28, 29, 30, 31, 32, 1
    };
    
    // Permutation function (P)
    private static final int[] P = {
        16, 7, 20, 21, 29, 12, 28, 17,
        1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9,
        19, 13, 30, 6, 22, 11, 4, 25
    };
    
    // Permuted Choice 1 (PC-1)
    private static final int[] PC1 = {
        57, 49, 41, 33, 25, 17, 9,
        1, 58, 50, 42, 34, 26, 18,
        10, 2, 59, 51, 43, 35, 27,
        19, 11, 3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
        7, 62, 54, 46, 38, 30, 22,
        14, 6, 61, 53, 45, 37, 29,
        21, 13, 5, 28, 20, 12, 4
    };
    
    // Permuted Choice 2 (PC-2)
    private static final int[] PC2 = {
        14, 17, 11, 24, 1, 5,
        3, 28, 15, 6, 21, 10,
        23, 19, 12, 4, 26, 8,
        16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };
    
    // The standard DES algo uses a different way of shifting where double and single shifts are combined
    // I have implemented a only single shifts for key generation, as discussed in theory class
    // To use standard DES, change it back to: {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1}
    private static final int[] SHIFTS = {
        1, 1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1
    };
    
    // S-Boxes
    private static final int[][][] SBOXES = {
        // S1
        {
            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
        },
        // S2
        {
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
        },
        // S3
        {
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
        },
        // S4
        {
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
        },
        // S5
        {
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
        },
        // S6
        {
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
        },
        // S7
        {
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
        },
        // S8
        {
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
        }
    };
    
    // Converting text to binary array 
    private static int[] textToBinary(String text) {
        int[] binary = new int[64];
        for (int i = 0; i < 8 && i < text.length(); i++) {
            int ascii = (int) text.charAt(i);
            for (int j = 0; j < 8; j++) {
                binary[i * 8 + j] = (ascii >> (7 - j)) & 1;
            }
        }
        return binary;
    }
    
    // Converting binary array to text
    private static String binaryToText(int[] binary) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binary.length; i += 8) {
            int ascii = 0;
            for (int j = 0; j < 8; j++) {
                ascii = (ascii << 1) | binary[i + j];
            }
            if (ascii != 0) {
                text.append((char) ascii);
            }
        }
        return text.toString();
    }
    
    // Converting binary array to hex
    private static String binaryToHex(int[] binary) {
        StringBuilder hex = new StringBuilder();
        for (int i = 0; i < binary.length; i += 4) {
            int digit = 0;
            for (int j = 0; j < 4; j++) {
                digit = (digit << 1) | binary[i + j];
            }
            hex.append(Integer.toHexString(digit).toUpperCase());
        }
        return hex.toString();
    }
    
    // Key generation 
    private static int[][] keyGeneration(int[] key) {
        int[] permutedKey = permute(key, PC1);
        
        int[] C = new int[28];
        int[] D = new int[28];
        System.arraycopy(permutedKey, 0, C, 0, 28);
        System.arraycopy(permutedKey, 28, D, 0, 28);
        
        int[][] subkeys = new int[16][48];
        
        for (int round = 0; round < 16; round++) {
            C = leftShift(C, SHIFTS[round]);
            D = leftShift(D, SHIFTS[round]);
            
            int[] combined = new int[56];
            System.arraycopy(C, 0, combined, 0, 28);
            System.arraycopy(D, 0, combined, 28, 28);
            
            subkeys[round] = permute(combined, PC2);
        }
        
        return subkeys;
    }
    
    public static int[] encrypt(int[] plaintext, int[] key) {
        System.out.println("\n=== Encryption Process ==="); // for seperation purposes
        
        int[][] subkeys = keyGeneration(key);
        
        int[] permuted = permute(plaintext, IP);
        printHex("After IP", permuted);
        
        int[] L = new int[32];
        int[] R = new int[32];
        System.arraycopy(permuted, 0, L, 0, 32);
        System.arraycopy(permuted, 32, R, 0, 32);
        
        for (int round = 0; round < 16; round++) {
            System.out.println("\n--- Round " + (round + 1) + " ---");
            printHex("Subkey K" + (round + 1), subkeys[round]);
            
            int[] newR = feistel(R, subkeys[round]);
            
            int[] temp = new int[32];
            for (int i = 0; i < 32; i++) {
                temp[i] = L[i] ^ newR[i];
            }
            
            L = R;
            R = temp;
            
            int[] combined = new int[64];
            System.arraycopy(L, 0, combined, 0, 32);
            System.arraycopy(R, 0, combined, 32, 32);
            printHex("After Round " + (round + 1), combined);
        }
        
        int[] preOutput = new int[64];
        System.arraycopy(R, 0, preOutput, 0, 32);
        System.arraycopy(L, 0, preOutput, 32, 32);
        
        int[] ciphertext = permute(preOutput, FP);
        printHex("After FP", ciphertext);
        
        return ciphertext;
    }
    
    public static int[] decrypt(int[] ciphertext, int[] key) {
        System.out.println("\n=== Decryption Process ===");
        
        int[][] subkeys = keyGeneration(key);
        
        int[] permuted = permute(ciphertext, IP);
        printHex("After IP", permuted);
        
        int[] L = new int[32];
        int[] R = new int[32];
        System.arraycopy(permuted, 0, L, 0, 32);
        System.arraycopy(permuted, 32, R, 0, 32);
        
        for (int round = 15; round >= 0; round--) {
            System.out.println("\n--- Round " + (16 - round) + " ---");
            printHex("Subkey K" + (round + 1), subkeys[round]);
            
            int[] newR = feistel(R, subkeys[round]);
            
            int[] temp = new int[32];
            for (int i = 0; i < 32; i++) {
                temp[i] = L[i] ^ newR[i];
            }
            
            L = R;
            R = temp;
            
            int[] combined = new int[64];
            System.arraycopy(L, 0, combined, 0, 32);
            System.arraycopy(R, 0, combined, 32, 32);
            printHex("After Round " + (16 - round), combined);
        }
        
        int[] preOutput = new int[64];
        System.arraycopy(R, 0, preOutput, 0, 32);
        System.arraycopy(L, 0, preOutput, 32, 32);
        
        int[] plaintext = permute(preOutput, FP);
        printHex("After FP", plaintext);
        
        return plaintext;
    }
    
    // Feistel function
    private static int[] feistel(int[] R, int[] subkey) {
        int[] expanded = permute(R, E);
        
        int[] xored = new int[48];
        for (int i = 0; i < 48; i++) {
            xored[i] = expanded[i] ^ subkey[i];
        }
        
        int[] sboxOutput = new int[32];
        for (int i = 0; i < 8; i++) {
            int[] block = new int[6];
            System.arraycopy(xored, i * 6, block, 0, 6);
            
            int row = (block[0] << 1) | block[5];
            int col = (block[1] << 3) | (block[2] << 2) | (block[3] << 1) | block[4];
            
            int sboxValue = SBOXES[i][row][col];
            
            for (int j = 0; j < 4; j++) {
                sboxOutput[i * 4 + j] = (sboxValue >> (3 - j)) & 1;
            }
        }
        
        return permute(sboxOutput, P);
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
    
    // Print in hexadecimal format
    private static void printHex(String label, int[] binary) {
        System.out.println(label + ": " + binaryToHex(binary));
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        System.out.println("=== DES Encryption/Decryption ===\n");
        
        System.out.println("Enter the plaintext (will be padded/truncated to 8 characters):");
        String plaintextStr = in.nextLine();
        
        if (plaintextStr.length() < 8) {
            plaintextStr = String.format("%-8s", plaintextStr);
        } else if (plaintextStr.length() > 8) {
            System.out.println("\nProcessing text in 8-character blocks...\n");
        }
        
        System.out.println("Enter the key (exactly 8 characters):");
        String keyStr = in.nextLine();
        
        if (keyStr.length() < 8) {
            keyStr = String.format("%-8s", keyStr);
        } else if (keyStr.length() > 8) {
            keyStr = keyStr.substring(0, 8);
        }
        
        int[] key = textToBinary(keyStr);
        
        System.out.println("\n=== Input ===");
        System.out.println("Key (Text): " + keyStr);
        System.out.println("Key (Hex): " + binaryToHex(key));
        
        List<int[]> ciphertextBlocks = new ArrayList<>();
        
        for (int blockIndex = 0; blockIndex < plaintextStr.length(); blockIndex += 8) {
            String block = plaintextStr.substring(blockIndex, Math.min(blockIndex + 8, plaintextStr.length()));
            
            // Padding
            if (block.length() < 8) {
                block = String.format("%-8s", block);
            }
            
            System.out.println("\n========================================");
            System.out.println("Processing Block " + ((blockIndex / 8) + 1) + ": \"" + block + "\"");
            System.out.println("========================================");
            
            int[] plaintext = textToBinary(block);
            
            System.out.println("\nPlaintext (Text): " + block);
            System.out.println("Plaintext (Hex): " + binaryToHex(plaintext));
            
            int[] ciphertext = encrypt(plaintext, key);
            ciphertextBlocks.add(ciphertext);
            
            System.out.println("\n=== Final Encryption Result ===");
            System.out.println("Ciphertext (Hex): " + binaryToHex(ciphertext));
            
            // Decrypt
            int[] decrypted = decrypt(ciphertext, key);
            
            System.out.println("\n=== Final Decryption Result ===");
            System.out.println("Decrypted (Hex): " + binaryToHex(decrypted));
            System.out.println("Decrypted (Text): " + binaryToText(decrypted));
            
            // Verify
            boolean match = true;
            for (int i = 0; i < plaintext.length; i++) {
                if (plaintext[i] != decrypted[i]) {
                    match = false;
                    break;
                }
            }
            System.out.println("\nVerification: " + (match ? "Success - Decryption matches original plaintext" : "Failed"));
        }
        
        // Displaying complete ciphertext
        if (ciphertextBlocks.size() > 1) {
            System.out.println("\n========================================");
            System.out.println("Complete Ciphertext (All Blocks):");
            System.out.println("========================================");
            for (int i = 0; i < ciphertextBlocks.size(); i++) {
                System.out.println("Block " + (i + 1) + ": " + binaryToHex(ciphertextBlocks.get(i)));
            }
        }
        
        in.close();
    }
}
