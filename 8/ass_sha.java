import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class ass_sha {

    private static final long[] K = {
            0x428a2f98d728ae22L, 0x7137449123ef65cdL, 0xb5c0fbcfec4d3b2fL, 0xe9b5dba58189dbbcL,
            0x3956c25bf348b538L, 0x59f111f1b605d019L, 0x923f82a4af194f9bL, 0xab1c5ed5da6d8118L,
            0xd807aa98a3030242L, 0x12835b0145706fbeL, 0x243185be4ee4b28cL, 0x550c7dc3d5ffb4e2L,
            0x72be5d74f27b896fL, 0x80deb1fe3b1696b1L, 0x9bdc06a725c71235L, 0xc19bf174cf692694L,
            0xe49b69c19ef14ad2L, 0xefbe4786384f25e3L, 0x0fc19dc68b8cd5b5L, 0x240ca1cc77ac9c65L,
            0x2de92c6f592b0275L, 0x4a7484aa6ea6e483L, 0x5cb0a9dcbd41fbd4L, 0x76f988da831153b5L,
            0x983e5152ee66dfabL, 0xa831c66d2db43210L, 0xb00327c898fb213fL, 0xbf597fc7beef0ee4L,
            0xc6e00bf33da88fc2L, 0xd5a79147930aa725L, 0x06ca6351e003826fL, 0x142929670a0e6e70L,
            0x27b70a8546d22ffcL, 0x2e1b21385c26c926L, 0x4d2c6dfc5ac42aedL, 0x53380d139d95b3dfL,
            0x650a73548baf63deL, 0x766a0abb3c77b2a8L, 0x81c2c92e47edaee6L, 0x92722c851482353bL,
            0xa2bfe8a14cf10364L, 0xa81a664bbc423001L, 0xc24b8b70d0f89791L, 0xc76c51a30654be30L,
            0xd192e819d6ef5218L, 0xd69906245565a910L, 0xf40e35855771202aL, 0x106aa07032bbd1b8L,
            0x19a4c116b8d2d0c8L, 0x1e376c085141ab53L, 0x2748774cdf8eeb99L, 0x34b0bcb5e19b48a8L,
            0x391c0cb3c5c95a63L, 0x4ed8aa4ae3418acbL, 0x5b9cca4f7763e373L, 0x682e6ff3d6b2b8a3L,
            0x748f82ee5defb2fcL, 0x78a5636f43172f60L, 0x84c87814a1f0ab72L, 0x8cc702081a6439ecL,
            0x90befffa23631e28L, 0xa4506cebde82bde9L, 0xbef9a3f7b2c67915L, 0xc67178f2e372532bL,
            0xca273eceea26619cL, 0xd186b8c721c0c207L, 0xeada7dd6cde0eb1eL, 0xf57d4f7fee6ed178L,
            0x06f067aa72176fbaL, 0x0a637dc5a2c898a6L, 0x113f9804bef90daeL, 0x1b710b35131c471bL,
            0x28db77f523047d84L, 0x32caab7b40c72493L, 0x3c9ebe0a15c9bebcL, 0x431d67c49c100d4cL,
            0x4cc5d4becb3e42b6L, 0x597f299cfc657e2aL, 0x5fcb6fab3ad6faecL, 0x6c44198c4a475817L
    };

    private static final long[] H0 = {
            0x6a09e667f3bcc908L, 0xbb67ae8584caa73bL, 0x3c6ef372fe94f82bL, 0xa54ff53a5f1d36f1L,
            0x510e527fade682d1L, 0x9b05688c2b3e6c1fL, 0x1f83d9abfb41bd6bL, 0x5be0cd19137e2179L
    };

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("Enter input text:");
        String input = sc.hasNextLine() ? sc.nextLine() : "";
        sc.close();

        byte[] message = input.getBytes(StandardCharsets.UTF_8);

        System.out.println("no of characters in the input: " + input.length());

        byte[] padded = padMessage(message);
        int blockCount = padded.length / 128;

        long[] h = H0.clone();

        for (int block = 0; block < blockCount; block++) {
            System.out.println("Block " + (block + 1));
            processBlock(padded, block, h);
        }

        StringBuilder finalHash = new StringBuilder();
        for (long value : h) {
            finalHash.append(toHex64(value));
        }

        System.out.println("Final Hash Value");
        System.out.println(finalHash.toString());
    }

    private static void processBlock(byte[] padded, int blockIndex, long[] h) {
        long[] w = new long[80];

        int base = blockIndex * 128;

        for (int i = 0; i < 16; i++) {
            int offset = base + i * 8;
            w[i] = ((long) (padded[offset] & 0xff) << 56)
                    | ((long) (padded[offset + 1] & 0xff) << 48)
                    | ((long) (padded[offset + 2] & 0xff) << 40)
                    | ((long) (padded[offset + 3] & 0xff) << 32)
                    | ((long) (padded[offset + 4] & 0xff) << 24)
                    | ((long) (padded[offset + 5] & 0xff) << 16)
                    | ((long) (padded[offset + 6] & 0xff) << 8)
                    | ((long) (padded[offset + 7] & 0xff));
        }

        for (int i = 16; i < 80; i++) {
            long s0 = smallSigma0(w[i - 15]);
            long s1 = smallSigma1(w[i - 2]);
            w[i] = add(w[i - 16], s0, w[i - 7], s1);
        }

        long a = h[0];
        long b = h[1];
        long c = h[2];
        long d = h[3];
        long e = h[4];
        long f = h[5];
        long g = h[6];
        long hh = h[7];

        for (int round = 0; round < 80; round++) {
            long[] state = doRound(a, b, c, d, e, f, g, hh, w[round], K[round], round + 1);
            a = state[0];
            b = state[1];
            c = state[2];
            d = state[3];
            e = state[4];
            f = state[5];
            g = state[6];
            hh = state[7];
        }

        h[0] = add(h[0], a);
        h[1] = add(h[1], b);
        h[2] = add(h[2], c);
        h[3] = add(h[3], d);
        h[4] = add(h[4], e);
        h[5] = add(h[5], f);
        h[6] = add(h[6], g);
        h[7] = add(h[7], hh);
    }

    private static long[] doRound(long a, long b, long c, long d, long e, long f, long g, long h,
                                  long w, long k, int roundNo) {
        long t1 = add(h, bigSigma1(e), ch(e, f, g), k, w);
        long t2 = add(bigSigma0(a), maj(a, b, c));

        long newA = add(t1, t2);
        long newB = a;
        long newC = b;
        long newD = c;
        long newE = add(d, t1);
        long newF = e;
        long newG = f;
        long newH = g;

        System.out.println("Round " + roundNo);
        System.out.printf("a=%s b=%s c=%s d=%s e=%s f=%s g=%s h=%s%n",
                toHex64(newA), toHex64(newB), toHex64(newC), toHex64(newD),
                toHex64(newE), toHex64(newF), toHex64(newG), toHex64(newH));

        return new long[]{newA, newB, newC, newD, newE, newF, newG, newH};
    }

    private static byte[] padMessage(byte[] message) {
        int originalLength = message.length;
        long bitLength = (long) originalLength * 8L;

        int paddedLength = originalLength + 1 + 16;
        while (paddedLength % 128 != 0) {
            paddedLength++;
        }

        byte[] padded = new byte[paddedLength];
        System.arraycopy(message, 0, padded, 0, originalLength);

        padded[originalLength] = (byte) 0x80;

        int lengthPosition = paddedLength - 16;

        for (int i = 0; i < 8; i++) {
            padded[lengthPosition + i] = 0x00;
        }

        for (int i = 0; i < 8; i++) {
            padded[lengthPosition + 8 + i] = (byte) (bitLength >>> (56 - 8 * i));
        }

        return padded;
    }

    private static long ch(long x, long y, long z) {
        return (x & y) ^ (~x & z);
    }

    private static long maj(long x, long y, long z) {
        return (x & y) ^ (x & z) ^ (y & z);
    }

    private static long bigSigma0(long x) {
        return Long.rotateRight(x, 28) ^ Long.rotateRight(x, 34) ^ Long.rotateRight(x, 39);
    }

    private static long bigSigma1(long x) {
        return Long.rotateRight(x, 14) ^ Long.rotateRight(x, 18) ^ Long.rotateRight(x, 41);
    }

    private static long smallSigma0(long x) {
        return Long.rotateRight(x, 1) ^ Long.rotateRight(x, 8) ^ (x >>> 7);
    }

    private static long smallSigma1(long x) {
        return Long.rotateRight(x, 19) ^ Long.rotateRight(x, 61) ^ (x >>> 6);
    }

    private static long add(long... values) {
        long sum = 0L;
        for (long v : values) {
            sum += v;
        }
        return sum;
    }

    private static String toHex64(long value) {
        String s = Long.toUnsignedString(value, 16);
        StringBuilder sb = new StringBuilder(16);
        for (int i = s.length(); i < 16; i++) {
            sb.append('0');
        }
        sb.append(s);
        return sb.toString();
    }
}