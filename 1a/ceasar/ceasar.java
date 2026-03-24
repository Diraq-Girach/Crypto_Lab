import java.util.*;

class ceasar{
    public static String encrypt(String s, int k){
        s = s.toLowerCase();
        StringBuilder res = new StringBuilder();
        for(char c : s.toCharArray()){
            if(!Character.isAlphabetic(c)){
                res.append(c);
                continue;
            }
            res.append((char)((c - 'a' + k)%26 + 'a'));
        }
        return res.toString();
    }

    public static String decrypt(String s, int k){
        StringBuilder res = new StringBuilder();
        for(char c : s.toCharArray()){
            if(!Character.isAlphabetic(c)){
                res.append(c);
                continue;
            }
            res.append((char)((c - 'a' - k + 26 )%26 + 'a'));
        }
        return res.toString();
    }

    public static void main(String args[]){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the message: ");
        String pt = in.nextLine(); 
        System.out.println("Enter the key: ");
        int k = in.nextInt();
        String c = encrypt(pt, k);
        System.out.println("The encrypted text is... " + c);
        String pt1 = decrypt(c, k);
        System.out.println("The decrypted text is... " + pt1);
    }
}
