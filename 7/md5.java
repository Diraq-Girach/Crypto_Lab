import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class md5 {
    static int[] s = {
        7,12,17,22, 7,12,17,22, 7,12,17,22, 7,12,17,22,
        5,9,14,20, 5,9,14,20, 5,9,14,20, 5,9,14,20,
        4,11,16,23, 4,11,16,23, 4,11,16,23, 4,11,16,23,
        6,10,15,21, 6,10,15,21, 6,10,15,21, 6,10,15,21
    };

    static int[] K = new int[64];
    static {
        for (int i = 0; i < 64; i++) {
            K[i] = (int) ((long)(1L << 32) * Math.abs(Math.sin(i + 1)));
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Input:");
        String input = sc.nextLine();
        sc.close();

        System.out.println("Number of characters: " + input.length());
        byte[] message = input.getBytes(StandardCharsets.UTF_8);
        int originalLength = message.length;
        long bitLength = (long) originalLength * 8;

        int paddingLength = (56 - (originalLength + 1) % 64 + 64) % 64;
        byte[] padded = new byte[originalLength + 1 + paddingLength + 8];
        System.arraycopy(message, 0, padded, 0, originalLength);
        padded[originalLength] = (byte) 0x80;
        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (bitLength >>> (8 * i)); // little-endian length
        }

        int A = 0x67452301;
        int B = 0xefcdab89;
        int C = 0x98badcfe;
        int D = 0x10325476;

        int blocks = padded.length / 64;
        for (int i = 0; i < blocks; i++) {
            System.out.println("\nBlock " + (i + 1));
            int[] M = new int[16];
            for (int j = 0; j < 16; j++) {
                int index = i * 64 + j * 4;
                M[j] = ((padded[index] & 0xff)) |
                       ((padded[index + 1] & 0xff) << 8) |
                       ((padded[index + 2] & 0xff) << 16) |
                       ((padded[index + 3] & 0xff) << 24);
            }

            int a = A, b = B, c = C, d = D;

            for (int j = 0; j < 64; j++) {
                int F = 0, g = 0;
                int round = 0;

                if (j < 16) {
                    if (j == 0) System.out.println("  Round 1 (j = 0..15)");
                    round = 1;
                    F = (b & c) | (~b & d);
                    g = j;
                } else if (j < 32) {
                    if (j == 16) System.out.println("  Round 2 (j = 16..31)");
                    round = 2;
                    F = (d & b) | (~d & c); // equivalent to (b & d) | (c & ~d)
                    g = (5 * j + 1) % 16;
                } else if (j < 48) {
                    if (j == 32) System.out.println("  Round 3 (j = 32..47)");
                    round = 3;
                    F = b ^ c ^ d;
                    g = (3 * j + 5) % 16;
                } else {
                    if (j == 48) System.out.println("  Round 4 (j = 48..63)");
                    round = 4;
                    F = c ^ (b | ~d);
                    g = (7 * j) % 16;
                }

                int temp = d;
                d = c;
                c = b;
                // emulating Java's 32-bit rotate and overflow behavior via ints
                int toRotate = a + F + K[j] + M[g];
                int rotated = Integer.rotateLeft(toRotate, s[j]);
                b = b + rotated;
                a = temp;

                // Printing intermediate state after this operation (j)
                // Using Integer.toUnsignedLong(...) to format as 8-digit hex 
                String state =
                    String.format("    j=%2d (%s)  a=%08x  b=%08x  c=%08x  d=%08x",
                                  j,
                                  "R" + round,
                                  Integer.toUnsignedLong(a),
                                  Integer.toUnsignedLong(b),
                                  Integer.toUnsignedLong(c),
                                  Integer.toUnsignedLong(d));
                System.out.println(state);
            }

            A += a;
            B += b;
            C += c;
            D += d;
        }

        // Construct MD5 hex: little-endian bytes of A, B, C, D
        StringBuilder hex = new StringBuilder();
        appendLittleEndianHex(hex, A);
        appendLittleEndianHex(hex, B);
        appendLittleEndianHex(hex, C);
        appendLittleEndianHex(hex, D);

        System.out.println("\nFinal Hash Value: " + hex.toString());
    }

    private static void appendLittleEndianHex(StringBuilder sb, int value) {
        for (int i = 0; i < 4; i++) {
            int b = (value >>> (8 * i)) & 0xff;
            sb.append(String.format("%02x", b));
        }
    }
}