import java.util.*;
class vigenere{
    public static String encrypt(String a, String b){
        String m = a.toLowerCase();
        String k = b.toLowerCase();
        StringBuilder result = new StringBuilder();
        int mul = m.length()/k.length();
        String fk = k.repeat(mul+1);
        int keyindex = 0;  // i did this to preserve spaces/nums and not pass key characters when non alphabets happen
        for(int i = 0; i<m.length(); i++){
             if(!Character.isAlphabetic(m.charAt(i))){
                result.append(m.charAt(i));
                continue;
            }
            result.append((char)(((m.charAt(i) - 'a') + (fk.charAt(keyindex++) - 'a'))%26+'a'));
        }
        
        return result.toString();
    }

    public static String decrypt(String a, String b){
        String c = a.toLowerCase();
        String k = b.toLowerCase();
        StringBuilder result = new StringBuilder();
        int mul = c.length()/k.length();
        String fk = k.repeat(mul+1);
        int keyindex = 0;
        for(int i = 0; i<c.length(); i++){
            if(!Character.isAlphabetic(c.charAt(i))){
                result.append(c.charAt(i));
                continue;
            }
            result.append((char)(((c.charAt(i) - 'a') - (fk.charAt(keyindex++) - 'a') + 26 )%26+'a'));
        }
        
        return result.toString();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter the message: ");
        String m = in.nextLine();
        System.out.println("Please enter the secret key: ");
        String k = in.nextLine();

        String c = encrypt(m, k);
        System.out.println("Encrypted text: " + c);

        String pt = decrypt(c,k);
        System.out.println("Decrypted text: " + pt);
    }
}