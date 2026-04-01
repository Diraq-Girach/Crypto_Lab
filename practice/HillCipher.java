import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class HillCipher {

    private static final int MOD = 26;

    // Encrypts the plaintext using the key matrix
    public static String encrypt(String plaintext, int[][] keyMatrix) {
        int n = keyMatrix.length;
        plaintext = plaintext.replaceAll("\\s", "").toLowerCase();
        
        List<Integer> plainVector = new ArrayList<>();
        for (char c : plaintext.toCharArray()) {
            plainVector.add(c - 'a');
        }

        // Padding with 'x' (23)
        while (plainVector.size() % n != 0) {
            plainVector.add(23);
        }

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plainVector.size(); i += n) {
            for (int row = 0; row < n; row++) {
                int sum = 0;
                for (int col = 0; col < n; col++) {
                    sum += keyMatrix[row][col] * plainVector.get(i + col);
                }
                ciphertext.append((char) ((sum % MOD) + 'a'));
            }
        }
        return ciphertext.toString();
    }

    // Decrypts the ciphertext
    public static String decrypt(String ciphertext, int[][] keyMatrix) {
        int n = keyMatrix.length;
        int[][] invMatrix = invertMatrixMod(keyMatrix, MOD);

        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += n) {
            for (int row = 0; row < n; row++) {
                int sum = 0;
                for (int col = 0; col < n; col++) {
                    int cipherVal = ciphertext.charAt(i + col) - 'a';
                    sum += invMatrix[row][col] * cipherVal;
                }
                // Ensure result is positive before modulo
                plaintext.append((char) (((sum % MOD + MOD) % MOD) + 'a'));
            }
        }
        return plaintext.toString();
    }

    // Finds the modular inverse of the matrix (The "Sympy" part)
    private static int[][] invertMatrixMod(int[][] matrix, int m) {
        int n = matrix.length;
        int det = determinant(matrix) % m;
        if (det < 0) det += m;

        int invDet = modInverse(det, m);
        int[][] adjugate = adjugate(matrix);
        int[][] inverse = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = (adjugate[i][j] * invDet) % m;
                if (inverse[i][j] < 0) inverse[i][j] += m;
            }
        }
        return inverse;
    }

    // --- Helper Math Methods ---

    private static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if (((a % m) * (x % m)) % m == 1) return x;
        }
        throw new ArithmeticException("Modular inverse does not exist");
    }

    private static int determinant(int[][] matrix) {
        int n = matrix.length;
        if (n == 1) return matrix[0][0];
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        int det = 0;
        for (int i = 0; i < n; i++) {
            det += Math.pow(-1, i) * matrix[0][i] * determinant(getSubmatrix(matrix, 0, i));
        }
        return det;
    }

    private static int[][] adjugate(int[][] matrix) {
        int n = matrix.length;
        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = sign * determinant(getSubmatrix(matrix, i, j));
            }
        }
        return adj;
    }

    private static int[][] getSubmatrix(int[][] matrix, int row, int col) {
        int n = matrix.length;
        int[][] sub = new int[n - 1][n - 1];
        int r = -1;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            r++;
            int c = -1;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                sub[r][++c] = matrix[i][j];
            }
        }
        return sub;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String text = sc.nextLine();

        System.out.print("Enter size of key matrix (n): ");
        int n = sc.nextInt();
        int[][] key = new int[n][n];

        System.out.println("Enter matrix elements:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = sc.nextInt();
            }
        }

        String cipher = encrypt(text, key);
        System.out.println("\nCiphertext: " + cipher);
        System.out.println("Decrypted: " + decrypt(cipher, key));
    }
}