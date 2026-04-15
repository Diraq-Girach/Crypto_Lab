import java.util.Scanner;

public class playfair {

    private static final int size = 5;
    private static char[][] keymatrix = new char[size][size];
    private static boolean[] used = new boolean[26];

    private static String cleantext(String text) {
        text = text.toLowerCase().replaceAll("[^a-z]", "");
        text = text.replace('j', 'i');
        return text;
    }

    private static void generatekeymatrix(String key) {
        key = cleantext(key);

        for (int i = 0; i < 26; i++) {
            used[i] = false;
        }
        used['j' - 'a'] = true; // combine i and j

        int index = 0;

        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!used[ch - 'a']) {
                keymatrix[index / size][index % size] = ch;
                used[ch - 'a'] = true;
                index++;
            }
        }

        for (char ch = 'a'; ch <= 'z'; ch++) {
            if (ch == 'j') continue;
            if (!used[ch - 'a']) {
                keymatrix[index / size][index % size] = ch;
                used[ch - 'a'] = true;
                index++;
            }
        }
    }

    private static void printkeymatrix() {
        System.out.println("key matrix:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(keymatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static int[] findposition(char ch) {
        if (ch == 'j') ch = 'i';

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (keymatrix[i][j] == ch) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static String prepareplaintext(String text) {
        text = cleantext(text);

        StringBuilder sb = new StringBuilder();
        int i = 0;

        while (i < text.length()) {
            char a = text.charAt(i);
            char b = (i + 1 < text.length()) ? text.charAt(i + 1) : 'x';

            if (a == b) {
                sb.append(a).append('x');
                i++;
            } else {
                sb.append(a).append(b);
                i += 2;
            }
        }

        if (sb.length() % 2 != 0) {
            sb.append('x');
        }

        return sb.toString();
    }

    public static String encrypt(String plaintext) {
        plaintext = prepareplaintext(plaintext);
        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 2) {
            char a = plaintext.charAt(i);
            char b = plaintext.charAt(i + 1);

            int[] posa = findposition(a);
            int[] posb = findposition(b);

            if (posa[0] == posb[0]) {
                cipher.append(keymatrix[posa[0]][(posa[1] + 1) % size]);
                cipher.append(keymatrix[posb[0]][(posb[1] + 1) % size]);
            } else if (posa[1] == posb[1]) {
                cipher.append(keymatrix[(posa[0] + 1) % size][posa[1]]);
                cipher.append(keymatrix[(posb[0] + 1) % size][posb[1]]);
            } else {
                cipher.append(keymatrix[posa[0]][posb[1]]);
                cipher.append(keymatrix[posb[0]][posa[1]]);
            }
        }

        return cipher.toString();
    }

    public static String decrypt(String ciphertext) {
        ciphertext = cleantext(ciphertext);
        StringBuilder plain = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char a = ciphertext.charAt(i);
            char b = ciphertext.charAt(i + 1);

            int[] posa = findposition(a);
            int[] posb = findposition(b);

            if (posa[0] == posb[0]) {
                plain.append(keymatrix[posa[0]][(posa[1] - 1 + size) % size]);
                plain.append(keymatrix[posb[0]][(posb[1] - 1 + size) % size]);
            } else if (posa[1] == posb[1]) {
                plain.append(keymatrix[(posa[0] - 1 + size) % size][posa[1]]);
                plain.append(keymatrix[(posb[0] - 1 + size) % size][posb[1]]);
            } else {
                plain.append(keymatrix[posa[0]][posb[1]]);
                plain.append(keymatrix[posb[0]][posa[1]]);
            }
        }

        return removefillerx(plain.toString());
    }

    private static String removefillerx(String text) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);

            if (i > 0 && i < text.length() - 1) {
                if (current == 'x' && text.charAt(i - 1) == text.charAt(i + 1)) {
                    continue;
                }
            }

            result.append(current);
        }

        if (result.length() > 0 && result.charAt(result.length() - 1) == 'x') {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("enter key: ");
        String key = sc.nextLine();

        System.out.print("enter message: ");
        String message = sc.nextLine();

        generatekeymatrix(key);
        printkeymatrix();

        String encrypted = encrypt(message);
        System.out.println("encrypted text: " + encrypted);

        String decrypted = decrypt(encrypted);
        System.out.println("decrypted text: " + decrypted);

        sc.close();
    }
}